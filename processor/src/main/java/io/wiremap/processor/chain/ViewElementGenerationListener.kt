package io.wiremap.processor.chain

import javax.lang.model.element.Element

/**
 *
 * @author anatolii vakaliuk
 */
interface ViewElementGenerationListener {

    fun accept(element: Element)

    fun complete()
}