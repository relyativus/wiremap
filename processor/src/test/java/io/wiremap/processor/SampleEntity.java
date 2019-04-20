package io.wiremap.processor;

/**
 * @author anatolii vakaliuk
 */

public class SampleEntity {

    private String value;

    Integer count;

    public SampleEntity(final String value, final Integer count) {
        this.value = value;
        this.count = count;
    }

    public String getValue() {
        return value;
    }
}
