package me.fengming.openjs.binding;

import me.fengming.openjs.script.ScriptType;

import java.util.function.Consumer;

/**
 * @author ZZZank
 */
record CustomBinding(
    String name,
    Object value,
    Consumer<ScriptType> onLoad,
    Consumer<ScriptType> onUnload
) implements Binding {
    @Override
    public String name() {
        return name;
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public void onLoad(ScriptType scriptType) {
        if (onLoad != null) {
            onLoad.accept(scriptType);
        }
    }

    @Override
    public void onUnload(ScriptType scriptType) {
        if (onUnload != null) {
            onUnload.accept(scriptType);
        }
    }
}
