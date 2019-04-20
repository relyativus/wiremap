package io.wiremap.core.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.String.format;

/**
 * @author anatolii vakaliuk
 */
public class ConversionRegistry {
    private final Map<ConversionMetadata, AbstractPropertyConverter> classBasedConverters = new HashMap<>();
    private final List<PropertyConverter> sophisticatedConverters = new ArrayList<>();

    public ConversionRegistry with(final PropertyConverter converter) {
        if (AbstractPropertyConverter.class.isAssignableFrom(converter.getClass())) {
            final AbstractPropertyConverter classBasedConverter = AbstractPropertyConverter.class.cast(converter);
            classBasedConverters.put(classBasedConverter.getConversionMetadata(), classBasedConverter);
        } else {
            sophisticatedConverters.add(converter);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public <S, R> PropertyConverter<S, R> lookupConverter(final Object from, final Class<R> to) {
        return Optional.ofNullable(from)
                .map(object -> new ConversionMetadata(object.getClass(), to))
                .map(metadata -> (PropertyConverter<S, R>) classBasedConverters.get(metadata))
                .or(() -> lookupSophisticated(from, to))
                .orElseThrow(() -> new NoSuchElementException(format("Could not find suitable converter for %s to %s",
                        from, to)));
    }

    @SuppressWarnings("unchecked")
    private <S, R> Optional<PropertyConverter<S, R>> lookupSophisticated(final Object conversionCandidate,
                                                                         final Class<R> targetClass) {
        return sophisticatedConverters.stream()
                .filter(converter -> converter.canConvert(conversionCandidate, targetClass))
                .findAny()
                .map(converter -> (PropertyConverter<S, R>) converter);
    }
}
