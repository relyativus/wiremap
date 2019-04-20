package io.wiremap.core.converter;

/**
 * @author anatolii vakaliuk
 */
public interface PropertyConverter<S, R> {

    boolean canConvert(final Object source, final Class<R> targetClass);

    R convert(S source);
}
