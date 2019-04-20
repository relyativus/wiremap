package io.wiremap.processor.chain

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import java.io.IOException
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

/**
 *
 * @author anatolii vakaliuk
 */
object GeneratedClassWriter {

    fun write(className: ClassName?, classDef: TypeSpec?, env: ProcessingEnvironment) {
        if (className != null && classDef != null) {
            val output = env.messager
            try {
                JavaFile.builder(className.packageName(), classDef)
                        .build()
                        .writeTo(env.filer)
                output.printMessage(Diagnostic.Kind.OTHER,
                        "Written generated class into ${className.packageName()}")
            } catch (e: IOException) {
                output.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Could not write generated class into %s. Reason: %s",
                                className.packageName(), e.message))
            }
        }
    }
}