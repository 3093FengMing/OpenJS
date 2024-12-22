package me.fengming.openjs.script;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        return (T) internal.get(property.ordinal);
    }

    public <T> T getOrDefault(ScriptProperty<T> property) {
        var got = get(property);
        return got == null ? property.defaultValue : got;
    }

    public Map<Integer, Object> getInternal() {
        return Collections.unmodifiableMap(internal);
    }

    public void initFromLines(List<String> lines) {
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            if (!line.startsWith("//")) {
                break;
            }
            line = line.substring("//".length()).trim();
            var parts = line.split(":", 2);
            if (parts.length < 2) {
                continue;
            }
            var prop = ScriptProperty.get(parts[0].trim());
            if (prop.isPresent()) {
                var got = prop.get().read(parts[1].trim());
                this.put((ScriptProperty<Object>) prop.get(), got);
            }
        }
    }
}
