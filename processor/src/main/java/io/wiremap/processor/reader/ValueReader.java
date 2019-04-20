package io.wiremap.processor.reader;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;

/**
 * @author anatolii vakaliuk
 */
public interface ValueReader {

    String generate();

    TypeElement expressionType(Types types);
}
