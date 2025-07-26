package me.fengming.openjs.registry.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author FengMing
 */
public class SimpleRegistry<T extends IRegistration> {
    protected Map<String, T> map = new HashMap<>();
    protected Map<T, String> reversed = new HashMap<>();
    protected String id;

    public SimpleRegistry(String id) {
        this.id = Objects.requireNonNull(id);
    }

    public String getRegistryId() {
        return id;
    }

    public T register(String key, T registration) {
        Objects.requireNonNull(key, "null registry key is invalid");
        if (map.containsKey(key)) {
            throw new IllegalArgumentException(String.format("'%s' already registered", key));
        }
        map.put(key, registration);
        reversed.put(registration, key);
        return registration;
    }

    public Set<String> getKeys() {
        return map.keySet();
    }

    public Collection<T> getValues() {
        return map.values();
    }

    public void apply(BiConsumer<String, T> consumer) {
        map.forEach(consumer);
    }

    public Optional<T> getValue(String name) {
        return Optional.of(map.get(name));
    }

    public Optional<String> getKey(T value) {
        return Optional.of(reversed.get(value));
    }

    public Codec<T> byNameCodec() {
        return Codec.STRING.flatXmap(
            (key) -> this.getValue(key)
                .map(DataResult::success)
                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + this.id + ": " + key)),
            (value) -> this.getKey(value)
                .map(DataResult::success)
                .orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + this.id + ":" + value))
        );
    }
}
