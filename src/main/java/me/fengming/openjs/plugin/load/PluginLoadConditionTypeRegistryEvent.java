package me.fengming.openjs.plugin.load;

import me.fengming.openjs.plugin.load.condition.PluginLoadConditionType;
import me.fengming.openjs.registry.api.SimpleRegistry;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * @author ZZZank
 */
@Mod.EventBusSubscriber
public class PluginLoadConditionTypeRegistryEvent extends Event {
    public final SimpleRegistry<PluginLoadConditionType> registry;

    public PluginLoadConditionTypeRegistryEvent(SimpleRegistry<PluginLoadConditionType> registry) {
        this.registry = Objects.requireNonNull(registry);
    }

    public PluginLoadConditionType register(String id, PluginLoadConditionType type) {
        return registry.register(id, type);
    }

    @SubscribeEvent
    public static void registerDefaultType(PluginLoadConditionTypeRegistryEvent event) {
        event.register("and", PluginLoadConditionType.AND);
        event.register("or", PluginLoadConditionType.OR);
        event.register("not", PluginLoadConditionType.NOT);
        event.register("constant", PluginLoadConditionType.CONSTANT);
        event.register("mod", PluginLoadConditionType.MOD);
        event.register("side", PluginLoadConditionType.SIDE);
    }
}
