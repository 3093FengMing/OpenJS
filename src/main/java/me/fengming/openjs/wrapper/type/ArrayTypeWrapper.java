package me.fengming.openjs.wrapper.type;

import me.fengming.openjs.utils.Cast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author ZZZank
 */
public final class ArrayTypeWrapper<T> implements TypeWrapper<T[]> {

    public final TypeWrapper<T> componentWrapper;
    public final Class<T> componentTarget;
    public final T[] empty;
    public final Class<T[]> target;

    public ArrayTypeWrapper(TypeWrapper<T> componentWrapper, Class<T> componentTarget) {
        this.componentWrapper = componentWrapper;
        this.componentTarget = componentTarget;
        this.empty = Cast.to(Array.newInstance(this.componentTarget, 0));
        this.target = Cast.to(empty.getClass());
    }

    @Override
    public boolean canWrap(Object from, Class<?> to) {
        return from instanceof Collection<?> c
            && (c.isEmpty() || componentWrapper.canWrap(c.iterator().next(), to.componentType()));
    }

    @Override
    public T[] wrap(Object from, Class<?> to) {
        final var casted = Cast.<Collection<?>>to(from);
        if (casted.isEmpty()) {
            return empty;
        }

        final var toComponent = to.componentType();

        final var size = casted.size();
        var index = 0;

        final var wrapped = Cast.<T[]>to(Array.newInstance(target, size));
        for (final var o : casted) {
            if (componentWrapper.canWrap(from, toComponent)) {
                wrapped[index++] = componentWrapper.wrap(o, toComponent);
            }
        }

        return index == 0
            ? empty
            : Arrays.copyOf(wrapped, index);
    }
}
