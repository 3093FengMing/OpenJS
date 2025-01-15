package me.fengming.openjs.registry.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author FengMing
 */
public class SimpleRegistry<T extends IRegistration> {
    protected HashMap<String, T> map = new HashMap<>();
    protected String id;

    public SimpleRegistry(String id) {
        this.id = id;
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
}
