package me.fengming.openjs.binding.base;

import me.fengming.openjs.registry.api.TypedRegistry;
import me.fengming.openjs.script.ScriptType;

/**
 * @author FengMing
 */
public class BindingRegistry extends TypedRegistry<Binding> {
    public BindingRegistry(ScriptType type) {
        super("binding", type);
    }

    public void register(String key, Object value) {
        register(Binding.create(key, value));
    }

    public void register(Binding binding) {
        super.register(binding.name(), binding);
    }
}
