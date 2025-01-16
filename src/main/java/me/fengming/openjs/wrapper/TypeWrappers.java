package me.fengming.openjs.wrapper;

import me.fengming.openjs.utils.Cast;
import org.mozilla.javascript.Context;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author FengMing
 */
public class TypeWrappers {
    public static final Map<Class<?>, TypeWrapper<?>> wrappers = new IdentityHashMap<>();

    public static <T> void register(Class<T> target, TypeWrapper<T> wrapper) {
        wrappers.put(target, wrapper);
    }

    public static <T> void registerSimple(Class<T> target, SimpleTypeWrapper<T> wrapper) {
        register(target, wrapper);
    }

    public static <T> TypeWrapper<T> get(Class<T> target) {
        return Cast.to(wrappers.get(target));
    }

    public static boolean canWrap(Class<?> target, Object from) {
        return hasWrapper(target) && wrap(target, from).getClass() == target;
    }

    public static boolean hasWrapper(Class<?> target) {
        return wrappers.containsKey(target);
    }

    public static <T> T wrap(Class<T> target, Object from) {
        TypeWrapper<T> wrapper = get(target);
        if (wrapper instanceof SimpleTypeWrapper<T> simple) {
            return simple.wrap(from);
        }
        throw new IllegalStateException(String.format("Cannot wrap %s to %s.", target, wrapper));
    }

    public static <T> T wrap(Class<T> target, Object from, Context context) {
        return get(target).wrap(context, from);
    }

    @FunctionalInterface
    public interface SimpleTypeWrapper<T> extends TypeWrapper<T> {
        T wrap(Object from);

        @Override
        default T wrap(Context context, Object from) {
            return wrap(from);
        }
    }
}
