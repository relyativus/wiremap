package io.wiremap.processor.reader

import java.util.*
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Types

/**
 * @author anatolii vakaliuk
 */

class GetterValueReader(private val objectField: String, private val types: Types, private val field: VariableElement)
    : ValueReader {

    override fun generate(): String {
        return StringJoiner(".", "", "()")
                .add(objectField)
                .add(Accessors.getAccessorNameForField(types, field))
                .toString()
    }

    override fun expressionType(types: Types): TypeElement  = types.asElement(field.asType()) as TypeElement
}
