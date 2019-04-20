package io.wiremap.processor;


import io.wiremap.core.config.ViewFactory;
import io.wiremap.core.config.ViewFactoryBuilder;
import io.wiremap.core.converter.AbstractPropertyConverter;
import io.wiremap.core.converter.ConversionRegistry;


/**
 * @author anatolii vakaliuk
 */
public class Demo {

    public static void main(String[] args) {
        final ViewFactory viewFactory = new ViewFactoryBuilder()
                .withRegistry(new ConversionRegistry()
                        .with(new IntegerToStringConverter()))
                .build();
        final SampleResponseView sampleResponseView = viewFactory
                .viewForEntity(SampleResponseView.class, new SampleEntity("Hello", 12));
        System.out.println(sampleResponseView.value());
        System.out.println(sampleResponseView.count());
    }

    private static final class IntegerToStringConverter extends AbstractPropertyConverter<Integer, String> {
        private IntegerToStringConverter() {
            super(Integer.class, String.class);
        }

        @Override
        public String convert(final Integer source) {
            return String.valueOf(source);
        }
    }
}
