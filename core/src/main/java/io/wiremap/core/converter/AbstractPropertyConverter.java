package io.wiremap.core.converter;

import static java.util.Objects.nonNull;

/**
 * @author anatolii vakaliuk
 */
public abstract class AbstractPropertyConverter<S, R> implements PropertyConverter<S, R> {
    private ConversionMetadata conversionMetadata;

    public AbstractPropertyConverter(final Class<S> sourceClass, final Class<R> targetClass) {
        this.conversionMetadata = new ConversionMetadata(sourceClass, targetClass);
    }

    ConversionMetadata getConversionMetadata() {
        return conversionMetadata;
    }

    @Override
    public boolean canConvert(final Object source, final Class<R> to) {
        return nonNull(source)
                && conversionMetadata.getSource().equals(source.getClass())
                && conversionMetadata.getTarget().equals(to);
    }

}
