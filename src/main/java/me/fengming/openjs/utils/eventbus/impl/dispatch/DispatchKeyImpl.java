package me.fengming.openjs.utils.eventbus.impl.dispatch;

import me.fengming.openjs.utils.eventbus.dispatch.DispatchKey;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public record DispatchKeyImpl<E, K>(
    Class<K> keyType,
    Function<E, K> toKey,
    Function<Object, K> inputTransformer
) implements DispatchKey<E, K> {

    public DispatchKeyImpl(Class<K> keyType, Function<E, K> toKey, Function<Object, K> inputTransformer) {
        this.keyType = Objects.requireNonNull(keyType);
        this.toKey = Objects.requireNonNull(toKey);
        this.inputTransformer = Objects.requireNonNull(inputTransformer);
    }

    @Override
    public K toKey(E event) {
        return toKey.apply(event);
    }

    @Override
    public K transformInput(Object input) {
        return inputTransformer.apply(input);
    }
}
