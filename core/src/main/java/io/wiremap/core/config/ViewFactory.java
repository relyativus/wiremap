package io.wiremap.core.config;


/**
 * @author anatolii vakaliuk
 */
public interface ViewFactory {

    <T, E> T viewForEntity(final Class<T> viewClass, final E entity);
}
