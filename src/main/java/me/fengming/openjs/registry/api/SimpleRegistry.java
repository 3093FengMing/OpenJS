package me.fengming.openjs.registry.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author FengMing
 */
public class SimpleRegistry<T extends IRegistration> {
    protected HashMap<String, T> map = new HashMap<>();
    protected String id;

    public SimpleRegistry(String id) {
        this.id = Objects.requireNonNull(id);
    }

    public String getRegistryId() {
        return id;
    }

    public T register(String key, T registration) {
        map.put(key, registration);
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

    public Optional<T> get(String name) {
        return Optional.of(map.get(name));
    }

    public Optional<String> searchKey(T value) {
        return map.entrySet()
            .stream()
            .filter(entry -> entry.getValue().equals(value))
            .findFirst()
            .map(Map.Entry::getKey);
    }

    public Codec<T> byNameCodec() {
        return Codec.STRING.flatXmap(
            (key) -> this.get(key)
                .map(DataResult::success)
                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + this.id + ": " + key)),
            (value) -> this.searchKey(value)
                .map(DataResult::success)
                .orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + this.id + ":" + value))
        );
    }
}
