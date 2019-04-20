package io.wiremap.processor;

import io.wiremap.core.view.meta.EntityResponseView;

/**
 * @author anatolii vakaliuk
 */
@EntityResponseView(SampleEntity.class)
public interface SampleResponseView {

    String value();

    String count();
}
