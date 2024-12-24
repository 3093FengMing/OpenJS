package me.fengming.openjs.script;

import me.fengming.openjs.utils.Cast;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZZZank
 */
public final class ScriptProperties {
    /**
     * TODO: make {@link ScriptProperty#ordinal} an {@code int}, then use Int2ObjectMap here
     */
    private final Map<Integer, Object> internal = new HashMap<>();

    public <T> void put(ScriptProperty<T> property, T value) {
        if (value != null) {
            internal.put(property.ordinal, value);
        }
    }

    public <T> T get(ScriptProperty<T> property) {
        return Cast.to(internal.get(property.ordinal));
    }

    public <T> T getOrDefault(ScriptProperty<T> property) {
        var got = get(property);
        return got == null ? property.defaultValue : got;
    }

    public Map<Integer, Object> getInternal() {
        return Collections.unmodifiableMap(internal);
    }
}
