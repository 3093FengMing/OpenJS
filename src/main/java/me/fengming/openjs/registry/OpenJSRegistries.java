package me.fengming.openjs.registry;

import me.fengming.openjs.script.Binding;
import me.fengming.openjs.script.ScriptType;

/**
 * @author FengMing
 */
public class OpenJSRegistries {
    public static final TypedRegistries<Binding, BindingRegistry> BINDINGS = new TypedRegistries<>(
        BindingRegistry::new,
        ScriptType.CLIENT, ScriptType.SERVER, ScriptType.STARTUP
    );
    public static final EventGroupRegistry EVENT_GROUPS = new EventGroupRegistry();
}
