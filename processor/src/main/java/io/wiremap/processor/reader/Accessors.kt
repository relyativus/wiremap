package io.wiremap.processor.reader

import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Types

/**
 * @author anatolii vakaliuk
 */
object Accessors {

    fun getAccessorNameForField(types: Types, field: VariableElement): String {
        val typeElement = types.asElement(field.asType()) as TypeElement
        val isBoolean = typeElement.simpleName.toString() == "boolean"
        val prefix = if (isBoolean) "is" else "get"
        val name = field.simpleName.toString()
        return prefix + name.substring(0, 1).toUpperCase() + name.substring(1)
    }

}
