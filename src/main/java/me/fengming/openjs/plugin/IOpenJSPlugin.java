package me.fengming.openjs.plugin;

import me.fengming.openjs.registry.BindingRegistry;
import me.fengming.openjs.registry.EventGroupRegistry;

public interface IOpenJSPlugin {

    void load();

    void registerBinding(BindingRegistry registry);

    void registerEvent(EventGroupRegistry registry);
}
