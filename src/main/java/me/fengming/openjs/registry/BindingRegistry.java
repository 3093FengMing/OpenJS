package me.fengming.openjs.registry;

import me.fengming.openjs.script.Binding;

/**
 * @author FengMing
 */
public class BindingRegistry extends SimpleRegistry<Binding> {
    public BindingRegistry() {
        super("binding");
    }

    public void register(String key, Object value) {
        super.register(key, new Binding(value));
    }
}
