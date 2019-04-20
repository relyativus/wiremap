package io.wiremap.processor.view

import io.wiremap.processor.CONVERSION_REGISTRY_VARIABLE
import javax.lang.model.element.TypeElement

/**
 *
 * @author anatolii vakaliuk
 */
class ConversionRegistryValueProcessor(private val expressionType: TypeElement, private val returnType: TypeElement)
    : PropertyValueProcessor {

    override fun process(source: String): String =
            if (expressionType == returnType) source
            else "$source == null ? null : $CONVERSION_REGISTRY_VARIABLE.lookupConverter($source, ${returnType.qualifiedName}.class).convert($source)"

}