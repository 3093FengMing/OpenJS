package me.fengming.openjs.wrapper.type;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author ZZZank
 */
public final class EnumTypeWrapper<T extends Enum<T>> implements TypeWrapper<T> {

    static final Map<Class<?>, EnumTypeWrapper<?>> CACHE = new IdentityHashMap<>();

    private final T[] all;

    EnumTypeWrapper(Class<?> type) {
        all = ((Class<T>) type).getEnumConstants();
    }

    @Override
    public boolean canWrap(Object from, Class<?> to) {
        return from instanceof CharSequence || from instanceof Number;
    }

    @Override
    public T wrap(Object from, Class<?> to) {
        if (from instanceof CharSequence) {
            final var name = from.toString();
            for (final var enu : all) {
                if (enu.name().equalsIgnoreCase(name)) {
                    return enu;
                }
            }
        } else if (from instanceof Number) {
            final var index = ((Number) from).intValue();
            if (index >= 0 && index < all.length) {
                return all[index];
            }
        }
        return null;
    }
}
