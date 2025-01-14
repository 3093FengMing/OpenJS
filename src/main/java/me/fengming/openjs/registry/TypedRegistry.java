package me.fengming.openjs.registry;

import me.fengming.openjs.script.ScriptType;

/**
 * @author ZZZank
 */
public class TypedRegistry<T extends IRegistration> extends SimpleRegistry<T> {
    public final ScriptType type;

    public TypedRegistry(String id, ScriptType type) {
        super(id);
        this.type = type;
    }
}
