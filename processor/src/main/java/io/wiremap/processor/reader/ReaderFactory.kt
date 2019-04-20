package io.wiremap.processor.reader

import javax.lang.model.element.*
import javax.lang.model.util.Types

/**
 * @author anatolii vakaliuk
 */
object ReaderFactory {
    private const val OBJECT_REF = "entity"

    fun forObjectField(targetClass: TypeElement, field: VariableElement, types: Types): ValueReader {
        val accessorNameForField = Accessors.getAccessorNameForField(types, field)
        val hasGetter = targetClass.enclosedElements.any { element: Element ->
            val modifiers: Set<Modifier> = element.modifiers
            ElementKind.METHOD == element.kind &&
                    element.simpleName.toString() == accessorNameForField
                    && (element as ExecutableElement).parameters.isEmpty()
                    && !modifiers.contains(Modifier.STATIC)
        }
        return if (hasGetter) GetterValueReader(OBJECT_REF, types, field) else DirectFieldReader(OBJECT_REF, field)

    }
}
