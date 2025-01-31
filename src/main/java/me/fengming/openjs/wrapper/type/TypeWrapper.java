package me.fengming.openjs.wrapper.type;

/**
 * @author ZZZank
 */
public interface TypeWrapper<T> {

    Simple<?> PASS_THROUGH = (from, to) -> from;

    boolean canWrap(Object from, Class<?> to);

    T wrap(Object from, Class<?> to);

    @FunctionalInterface
    interface Simple<T> extends TypeWrapper<T> {

        @Override
        default boolean canWrap(Object from, Class<?> to) {
            return true;
        }
    }
}
