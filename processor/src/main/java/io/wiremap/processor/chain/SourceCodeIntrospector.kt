package io.wiremap.processor.chain

import io.wiremap.core.view.meta.EntityResponseView
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException

/**
 *
 * @author anatolii vakaliuk
 */

fun extractTypeElement(responseView: EntityResponseView, env: ProcessingEnvironment) : TypeElement {
    try {
        responseView.value
    } catch (e: MirroredTypeException) {
        return env.typeUtils.asElement(e.typeMirror) as TypeElement
    }

    throw IllegalStateException("o_O")
}