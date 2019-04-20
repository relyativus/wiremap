package io.wiremap.processor.chain

import com.squareup.javapoet.*
import io.wiremap.core.converter.ConversionRegistry
import io.wiremap.core.view.meta.EntityResponseView
import io.wiremap.processor.CONVERSION_REGISTRY_VARIABLE
import io.wiremap.processor.ENTITY_VARIABLE
import io.wiremap.processor.OUTPUT_VALUE_SUFFIX
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

/**
 *
 * @author anatolii vakaliuk
 */
private const val VIEW_TYPE_VARIABLE = "T"

class ViewFactoryGenerationListener(private val env: ProcessingEnvironment) : ViewElementGenerationListener {

    private var viewBranches: MutableList<CodeBlock> = arrayListOf()

    override fun accept(element: Element) {
        val responseView = element.getAnnotation(EntityResponseView::class.java)
        if (element is TypeElement && responseView != null) {
            val entityTypeElement = extractTypeElement(responseView, env)
            val responseViewClass = "${element.qualifiedName}$OUTPUT_VALUE_SUFFIX"
            val viewInstantiationBranch = CodeBlock.builder()
                    .beginControlFlow("if (viewClass.isAssignableFrom($responseViewClass.class))")
                    .addStatement("return ($VIEW_TYPE_VARIABLE) " +
                            "new $responseViewClass((${entityTypeElement.qualifiedName})$ENTITY_VARIABLE, $CONVERSION_REGISTRY_VARIABLE)")
                    .endControlFlow()
                    .build()
            viewBranches.add(viewInstantiationBranch)
        }
    }

    override fun complete() {
        val defaultBranch = CodeBlock.builder().addStatement("""throw new IllegalStateException
            ("Could not find corresponding view implementation for view class"+viewClass)""".trimMargin())
                .build()
        val factoryClassName = ClassName.get(io.wiremap.core.config.ViewFactory::class.java.packageName, "DefaultViewFactory")
        val factoryMethodSpec = MethodSpec.methodBuilder(io.wiremap.core.config.ViewFactory::class.java.methods.first().name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addTypeVariable(TypeVariableName.get(VIEW_TYPE_VARIABLE))
                .addTypeVariable(TypeVariableName.get("E"))
                .addParameter(ParameterizedTypeName.get(ClassName.get(Class::class.java), TypeVariableName.get(VIEW_TYPE_VARIABLE)), "viewClass")
                .addParameter(ParameterSpec.builder(TypeVariableName.get("E"), ENTITY_VARIABLE).build())
                .returns(TypeVariableName.get(VIEW_TYPE_VARIABLE))
        viewBranches.forEach { factoryMethodSpec.addCode(it) }
        factoryMethodSpec.addCode(defaultBranch)

        val factoryClass = TypeSpec.classBuilder(factoryClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(io.wiremap.core.config.ViewFactory::class.java)
                .addField(ConversionRegistry::class.java, CONVERSION_REGISTRY_VARIABLE, Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ConversionRegistry::class.java, CONVERSION_REGISTRY_VARIABLE, Modifier.FINAL)
                        .addCode(CodeBlock.builder().addStatement("this.$CONVERSION_REGISTRY_VARIABLE=$CONVERSION_REGISTRY_VARIABLE").build())
                        .build())
                .addMethod(factoryMethodSpec.build())
                .build()

        GeneratedClassWriter.write(factoryClassName, factoryClass, env)
    }
}