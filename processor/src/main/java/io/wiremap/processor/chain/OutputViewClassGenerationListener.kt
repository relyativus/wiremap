package io.wiremap.processor.chain

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import io.wiremap.core.converter.ConversionRegistry
import io.wiremap.core.view.meta.EntityResponseView
import io.wiremap.processor.CONVERSION_REGISTRY_VARIABLE
import io.wiremap.processor.ENTITY_VARIABLE
import io.wiremap.processor.OUTPUT_VALUE_SUFFIX
import io.wiremap.processor.reader.ReaderFactory
import io.wiremap.processor.reader.ValueReader
import io.wiremap.processor.view.ConversionRegistryValueProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.*
import javax.tools.Diagnostic

/**
 *
 * @author anatolii vakaliuk
 */
class OutputViewClassGenerationListener(private val env: ProcessingEnvironment) : ViewElementGenerationListener {

    var className: ClassName? = null
    var classDef: TypeSpec.Builder? = null

    override fun accept(element: Element) {
        if (element is TypeElement) {
            val entityElement = extractTypeElement(element.getAnnotation(EntityResponseView::class.java), env)
            val entityClassName = ClassName.get(entityElement)
            val classBuilder = createClassWithConstructor(element, entityClassName)
            env.messager.printMessage(Diagnostic.Kind.NOTE, "Created implementation with constructor")
            element.enclosedElements
                    .filter { ElementKind.METHOD == it.kind }
                    .map { it as ExecutableElement }
                    .map { method -> createMethodSpec(entityElement, method) }
                    .forEach { classBuilder.addMethod(it) }

            this.className = entityClassName
            this.classDef = classBuilder
        }
    }

    override fun complete() = GeneratedClassWriter.write(className, classDef?.build(), env)

    private fun createClassWithConstructor(element: TypeElement, entityClassName: ClassName?): TypeSpec.Builder {
        return TypeSpec.classBuilder(element.simpleName.toString() + OUTPUT_VALUE_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ClassName.get(element))
                .addField(entityClassName, ENTITY_VARIABLE, Modifier.PRIVATE, Modifier.FINAL)
                .addField(ConversionRegistry::class.java, CONVERSION_REGISTRY_VARIABLE, Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(entityClassName, ENTITY_VARIABLE, Modifier.FINAL)
                        .addParameter(ConversionRegistry::class.java, CONVERSION_REGISTRY_VARIABLE, Modifier.FINAL)
                        .addStatement(CodeBlock.builder().add("this.${ENTITY_VARIABLE}=${ENTITY_VARIABLE}")
                                .build())
                        .addStatement(CodeBlock.builder().add("this.${CONVERSION_REGISTRY_VARIABLE}=${CONVERSION_REGISTRY_VARIABLE}")
                                .build())
                        .build())
    }

    private fun createMethodSpec(entityClass: TypeElement, executableElement: ExecutableElement): MethodSpec {
        val targetField = getFieldByMethodName(entityClass, executableElement)
        val valueReader = ReaderFactory.forObjectField(entityClass, targetField,
                env.typeUtils)
        val code = CodeBlock.builder()
                .addStatement("return " + applyProcessors(executableElement, valueReader)).build()
        return MethodSpec.overriding(executableElement)
                .addCode(code)
                .build()
    }

    private fun applyProcessors(executableElement: ExecutableElement, valueReader: ValueReader): String {
        val expressionType = valueReader.expressionType(env.typeUtils)
        val methodReturnType = env.typeUtils.asElement(executableElement.returnType) as TypeElement
        return ConversionRegistryValueProcessor(expressionType, methodReturnType).process(valueReader.generate())
    }

    private fun getFieldByMethodName(entityClass: TypeElement,
                                     executableElement: ExecutableElement): VariableElement {
        val correspondingField = entityClass.enclosedElements
                .filter { ElementKind.FIELD == it.kind }
                .map { it as VariableElement }
                .filter { !it.modifiers.contains(Modifier.STATIC) }
                .firstOrNull { it.simpleName == executableElement.simpleName }
        if (correspondingField == null) {
            env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Cannot lookup target field in class ${entityClass.simpleName} " +
                            "by mapped method name ${executableElement.simpleName}"
            )
            throw IllegalArgumentException("Could not find field matching method name")
        }
        return correspondingField
    }
}