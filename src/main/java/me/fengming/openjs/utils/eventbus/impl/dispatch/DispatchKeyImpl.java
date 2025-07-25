package me.fengming.openjs.utils.eventbus.impl.dispatch;

import me.fengming.openjs.utils.eventbus.dispatch.DispatchKey;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public record DispatchKeyImpl<E, K>(
    Class<K> keyType,
    Function<E, K> toKey
) implements DispatchKey<E, K> {

    public DispatchKeyImpl(Class<K> keyType, Function<E, K> toKey) {
        this.keyType = Objects.requireNonNull(keyType);
        this.toKey = Objects.requireNonNull(toKey);
    }

    @Override
    public K eventToKey(E event) {
        return toKey.apply(event);
    }
}
