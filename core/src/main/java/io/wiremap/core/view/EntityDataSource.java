package io.wiremap.core.view;

import io.wiremap.core.view.meta.EntityRequestView;

/**
 * @author anatolii vakaliuk
 */
@EntityRequestView
public interface EntityDataSource<T> {

    void apply(T entity);
}
