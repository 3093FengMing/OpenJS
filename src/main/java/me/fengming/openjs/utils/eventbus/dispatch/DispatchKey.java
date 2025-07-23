package me.fengming.openjs.utils.eventbus.dispatch;

import me.fengming.openjs.utils.eventbus.impl.dispatch.DispatchKeyImpl;

import java.util.function.Function;

/**
 * @author ZZZank
 */
public interface DispatchKey<E, K> {
    static <E, K> DispatchKey<E, K> create(
        Class<K> keyType,
        Function<E, K> toKey,
        Function<Object, K> inputTransformer
    ) {
        return new DispatchKeyImpl<>(keyType, toKey, inputTransformer);
    }

    static <E, K> DispatchKey<E, K> create(Class<K> keyType, Function<E, K> eventToKey) {
        return create(keyType, eventToKey, o -> keyType.isInstance(o) ? (K) o : null);
    }

    static <E, K> DispatchKey<E, K> create(Class<K> keyType) {
        return create(keyType, (ignored) -> null);
    }

    Class<K> keyType();

    K toKey(E event);

    K transformInput(Object input);
}
