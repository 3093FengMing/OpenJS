package me.fengming.openjs.utils.eventbus.dispatch;

import me.fengming.openjs.utils.eventbus.impl.dispatch.EventDispatchKeyImpl;

import java.util.function.Function;

/**
 * @author ZZZank
 */
public interface EventDispatchKey<E, K> {
    static <E, K> EventDispatchKey<E, K> create(
        Class<K> keyType,
        Function<E, K> toKey,
        Function<Object, K> inputTransformer
    ) {
        return new EventDispatchKeyImpl<>(keyType, toKey, inputTransformer);
    }

    static <E, K> EventDispatchKey<E, K> create(Class<K> keyType, Function<E, K> toKey) {
        return create(keyType, toKey, o -> keyType.isInstance(o) ? (K) o : null);
    }

    static <E, K> EventDispatchKey<E, K> create(Class<K> keyType) {
        return create(keyType, (ignored) -> null);
    }

    Class<K> keyType();

    K toKey(E event);

    K transformInput(Object input);
}
