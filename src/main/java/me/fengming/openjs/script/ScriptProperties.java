package me.fengming.openjs.script;

import me.fengming.openjs.utils.Cast;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author ZZZank
 */
public final class ScriptProperties {
    /**
     * TODO: make {@link ScriptProperty#ordinal} an {@code int}, then use Int2ObjectMap here
     */
    private final Object[] internal = new Object[ScriptProperty.getLastIndex()];

    public <T> void put(ScriptProperty<T> property, T value) {
        if (value == null) return;
        internal[property.ordinal] = value;
    }

    @NotNull
    public <T> Optional<T> get(ScriptProperty<T> property) {
        return Optional.ofNullable(Cast.to(internal[property.ordinal]));
    }

    @NotNull
    public <T> T getOrDefault(ScriptProperty<T> property) {
        T got = Cast.to(internal[property.ordinal]);
        return got == null ? property.defaultValue : got;
    }

    @NotNull
    public List<Object> getInternal() {
        return List.of(internal);
    }
}
