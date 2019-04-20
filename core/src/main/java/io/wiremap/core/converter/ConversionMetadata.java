package io.wiremap.core.converter;

import lombok.Value;

/**
 * @author anatolii vakaliuk
 */
@Value
public class ConversionMetadata {
    private Class<?> source;
    private Class<?> target;
}
