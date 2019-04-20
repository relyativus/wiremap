package io.wiremap.processor;

import io.wiremap.core.view.EntityDataSource;

/**
 * @author anatolii vakaliuk
 */
public interface SampleRequestView extends EntityDataSource<SampleEntity> {

    String value1();

    String value2();

}
