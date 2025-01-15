package me.fengming.openjs.script;

import me.fengming.openjs.registry.api.IRegistration;

// Is it necessary for it to exist?
public record Binding(Object value) implements IRegistration {
}
