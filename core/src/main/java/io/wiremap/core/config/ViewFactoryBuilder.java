package io.wiremap.core.config;

import io.wiremap.core.converter.ConversionRegistry;

import java.lang.reflect.Constructor;

/**
 * @author anatolii vakaliuk
 */
public class ViewFactoryBuilder {

    private ConversionRegistry conversionRegistry;

    public ViewFactoryBuilder withRegistry(final ConversionRegistry registry) {
        this.conversionRegistry = registry;
        return this;
    }

    public ViewFactory build() {
        try {
            final Class<?> factoryImplementationClass = Class.forName(ViewFactory.class.getPackageName() + ".Default"
                    + ViewFactory.class.getSimpleName());
            final Constructor<?> constructor = factoryImplementationClass.getConstructor(ConversionRegistry.class);
            return (ViewFactory) constructor.newInstance(conversionRegistry);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Could not find generated view factory implementation");
        }
    }
}
