package io.wiremap.processor

import io.wiremap.core.view.meta.EntityResponseView
import io.wiremap.processor.chain.OutputViewClassGenerationListener
import io.wiremap.processor.chain.ViewFactoryGenerationListener
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * @author anatolii vakaliuk
 */
@SupportedAnnotationTypes("io.wiremap.core.view.meta.EntityResponseView")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
class EntityResponseViewAnnotationProcessor : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val output = processingEnv.messager
        val elements = roundEnv.getElementsAnnotatedWith(EntityResponseView::class.java)
        output.printMessage(Diagnostic.Kind.NOTE, "Found elements ${elements.size}")
        val processors = sequenceOf(
                OutputViewClassGenerationListener(processingEnv),
                ViewFactoryGenerationListener(processingEnv)
        )
        elements.map { it as TypeElement }.forEach { element -> processors.forEach { it.accept(element) } }
        if (elements.isNotEmpty()) processors.forEach { it.complete() }

        return true
    }


}
