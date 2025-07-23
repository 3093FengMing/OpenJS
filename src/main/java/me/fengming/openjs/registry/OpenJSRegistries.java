package me.fengming.openjs.registry;

import me.fengming.openjs.binding.base.BindingRegistry;
import me.fengming.openjs.registry.api.TypedRegistries;
import me.fengming.openjs.script.ScriptType;

/**
 * @author FengMing
 */
public class OpenJSRegistries {
    public static final TypedRegistries<BindingRegistry> BINDINGS = new TypedRegistries<>(
        BindingRegistry::new,
        ScriptType.CLIENT, ScriptType.SERVER, ScriptType.STARTUP
    );
    public static final TypedRegistries<EventGroupRegistry> EVENT_GROUPS = new TypedRegistries<>(
        EventGroupRegistry::new,
        ScriptType.values()
    );
}
