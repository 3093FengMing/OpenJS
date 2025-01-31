package me.fengming.openjs.wrapper.type;

import me.fengming.openjs.utils.Cast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author FengMing
 */
public class TypeWrappers {
    private static final Map<Class<?>, TypeWrapper<?>> ALL = new IdentityHashMap<>();

    public static <T> void register(Class<T> target, TypeWrapper<T> wrapper) {
        preCondition(target, wrapper);
        if (ALL.containsKey(target)) {
            throw new IllegalArgumentException("Wrapper for class " + target.getName() + " already exists");
        }
        ALL.put(target, wrapper);

        final var wrapper1D = new ArrayTypeWrapper<>(wrapper, target);
        ALL.put(wrapper1D.target, wrapper1D);

        final var wrapper2D = new ArrayTypeWrapper<>(wrapper1D, wrapper1D.target);
        ALL.put(wrapper2D.target, wrapper2D);

        final var wrapper3D = new ArrayTypeWrapper<>(wrapper2D, wrapper2D.target);
        ALL.put(wrapper3D.target, wrapper3D);

        // 4D?
    }

    public static <T> void register(
        Class<T> target,
        ProxiedWrapper.Validator validator,
        ProxiedWrapper.Wrapper<T> wrapper
    ) {
        register(target, new ProxiedWrapper<>(validator, wrapper));
    }

    public static <T> void registerSimple(Class<T> target, TypeWrapper.Simple<T> wrapper) {
        register(target, wrapper);
    }

    private static <T> void preCondition(Class<T> target, TypeWrapper<T> wrapper) {
        if (target == null) {
            throw new IllegalArgumentException("target is null");
        } else if (wrapper == null) {
            throw new IllegalArgumentException("type wrapper to be registered is null");
        } else if (target == Object.class) {
            throw new IllegalArgumentException("target can't be Object.class");
        } else if (target.isArray()) {
            throw new IllegalArgumentException("target can't be an array");
        } else if (target.isPrimitive()) {
            throw new IllegalArgumentException("target can't be a primitive class");
        }
    }

    @Nullable
    public static <T> TypeWrapper<T> get(Class<T> target) {
        var got = ALL.get(target);
        if (got != null) {
            return Cast.to(got);
        }
        if (Enum.class.isAssignableFrom(target)) {
            EnumTypeWrapper<?> result;
            synchronized (EnumTypeWrapper.CACHE) {
                result = EnumTypeWrapper.CACHE.computeIfAbsent(target, EnumTypeWrapper::new);
            }
            return Cast.to(result);
        }
        return null;
    }

    @Nullable
    public static <T> TypeWrapper<T> getValidated(Class<T> target, Object from, Class<?> to) {
        final var got = get(target);
        return got != null && got.canWrap(from, to) ? got : null;
    }

    @NotNull
    public static <T> TypeWrapper<T> getValidatedOrNone(Class<T> target, Object from, Class<?> to) {
        final var got = get(target);
        return got != null && got.canWrap(from, to) ? got : Cast.to(TypeWrapper.PASS_THROUGH);
    }

    public static boolean hasWrapper(Class<?> target) {
        return ALL.containsKey(target);
    }

    public static boolean canWrap(Class<?> target, Object from, Class<?> to) {
        final var got = ALL.get(target);
        return got != null && got.canWrap(from, to);
    }

    public static Map<Class<?>, TypeWrapper<?>> getAll() {
        return Collections.unmodifiableMap(ALL);
    }
}
