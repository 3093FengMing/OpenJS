package me.fengming.openjs.binding.base;

import me.fengming.openjs.registry.api.IRegistration;
import me.fengming.openjs.script.ScriptType;

import java.util.Objects;

/**
 * @author ZZZank
 */
public interface Binding extends IRegistration {
    static Binding create(String name, Object value) {
        return new SimpleBinding(Objects.requireNonNull(name), value);
    }

    String name();

    Object value();

    default void onLoad(ScriptType scriptType) {
    }

    default void onUnload(ScriptType scriptType) {
    }
}
