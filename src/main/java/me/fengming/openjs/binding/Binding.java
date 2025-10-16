package me.fengming.openjs.binding;

import me.fengming.openjs.registry.api.IRegistration;
import me.fengming.openjs.script.ScriptType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author ZZZank
 */
public interface Binding extends IRegistration {
    static Binding create(@NotNull String name, Object value) {
        return new SimpleBinding(Objects.requireNonNull(name), value);
    }

    static Binding create(@NotNull String name, Object value, @Nullable Consumer<ScriptType> onLoad, @Nullable Consumer<ScriptType> onUnload) {
        return new CustomBinding(Objects.requireNonNull(name), value, onLoad, onUnload);
    }

    String name();

    Object value();

    default void onLoad(ScriptType scriptType) {
    }

    default void onUnload(ScriptType scriptType) {
    }
}
