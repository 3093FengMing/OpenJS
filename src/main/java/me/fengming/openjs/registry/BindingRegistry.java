package me.fengming.openjs.registry;

import me.fengming.openjs.script.Binding;
import me.fengming.openjs.script.ScriptType;

/**
 * @author FengMing
 */
public class BindingRegistry extends TypedRegistry<Binding> {
    public BindingRegistry(ScriptType type) {
        super("binding", type);
    }

    public void register(String key, Object value) {
        super.register(key, new Binding(value));
    }
}
