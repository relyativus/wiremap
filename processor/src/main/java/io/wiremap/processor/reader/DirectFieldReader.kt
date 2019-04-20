package io.wiremap.processor.reader

import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Types

/**
 * @author anatolii vakaliuk
 */
class DirectFieldReader(private val objectField: String, private val field: VariableElement) : ValueReader {

    override fun generate(): String {
        return arrayOf(objectField, field.simpleName.toString()).joinToString(".")
    }

    override fun expressionType(types: Types): TypeElement = types.asElement(field.asType()) as TypeElement
}
