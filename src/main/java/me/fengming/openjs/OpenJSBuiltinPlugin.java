package me.fengming.openjs;

import me.fengming.openjs.binding.PackMode;
import me.fengming.openjs.plugin.IOpenJSPlugin;
import me.fengming.openjs.registry.BindingRegistry;
import me.fengming.openjs.registry.EventGroupRegistry;
import me.fengming.openjs.registry.OpenJSRegistries;
import me.fengming.openjs.wrapper.EventGroupWrapper;

/**
 * @author FengMing
 */
public class OpenJSBuiltinPlugin implements IOpenJSPlugin {
    @Override
    public void load() {
        //registerBinding();
    }

    @Override
    public void registerBinding(BindingRegistry registry) {
        // placeholder for testing
        registry.register("System", System.class);
        registry.register("PackMode", PackMode.class);

        // event
        OpenJSRegistries.EVENT_GROUPS.apply((name, group) -> registry.register(name, new EventGroupWrapper(group)));
    }

    @Override
    public void registerEvent(EventGroupRegistry registry) {
        // registry.register(TestEvents.GROUP);
    }

//    public interface TestEvents {
//        EventGroup GROUP = EventGroup.create("TestEvents");
//        EventHandler<RegistryEvent> REGISTRY = GROUP.add("test1", ScriptType.SERVER, RegistryEvent.class);
//    }

}
