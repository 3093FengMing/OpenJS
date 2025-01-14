package me.fengming.openjs.registry;

import me.fengming.openjs.script.ScriptType;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public class TypedRegistries<T extends IRegistration, R extends SimpleRegistry<T>> {

    private final Map<ScriptType, R> registries;

    public TypedRegistries(Function<ScriptType, R> generator, Collection<ScriptType> types) {
        if (types.isEmpty()) {
            this.registries = Collections.emptyMap();
            return;
        }
        this.registries = new EnumMap<>(ScriptType.class);
        for (var type : types) {
            registries.put(type, generator.apply(type));
        }
    }

    public TypedRegistries(Function<ScriptType, R> generator, ScriptType... types) {
        this(generator, Arrays.asList(types));
    }

    public Optional<R> get(ScriptType type) {
        return Optional.ofNullable(registries.get(type));
    }

    public Map<ScriptType, R> getRegistries() {
        return Collections.unmodifiableMap(registries);
    }

    public Set<ScriptType> getValidTypes() {
        return registries.keySet();
    }

    public void forEach(Consumer<R> action) {
        for (R registry : registries.values()) {
            action.accept(registry);
        }
    }

    public void apply(BiConsumer<String, T> action) {
        for (var registry : registries.values()) {
            registry.apply(action);
        }
    }
}
