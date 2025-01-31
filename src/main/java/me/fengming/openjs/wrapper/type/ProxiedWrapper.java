package me.fengming.openjs.wrapper.type;

import java.util.Objects;

/**
 * @author ZZZank
 */
public final class ProxiedWrapper<T> implements TypeWrapper<T> {
    private final Validator validator;
    private final Wrapper<T> wrapper;

    public ProxiedWrapper(Validator validator, Wrapper<T> wrapper) {
        this.validator = Objects.requireNonNull(validator);
        this.wrapper = Objects.requireNonNull(wrapper);
    }

    @Override
    public boolean canWrap(Object from, Class<?> to) {
        return validator.canWrap(from, to);
    }

    @Override
    public T wrap(Object from, Class<?> to) {
        return wrapper.wrap(from, to);
    }

    @FunctionalInterface
    public interface Validator {
        boolean canWrap(Object from, Class<?> to);
    }

    @FunctionalInterface
    public interface Wrapper<T> {
        T wrap(Object from, Class<?> to);
    }
}
