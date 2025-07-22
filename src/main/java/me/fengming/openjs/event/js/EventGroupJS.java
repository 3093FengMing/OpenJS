package me.fengming.openjs.event.js;

import me.fengming.openjs.utils.eventbus.EventBus;
import org.mozilla.javascript.ScriptableObject;

import java.util.Objects;

/**
 * @author ZZZank
 */
public class EventGroupJS extends ScriptableObject {

    public <E> EventBusJS<E> addBus(String name, EventBus<E> bus) {
        var busJS = new EventBusJS<>(bus);
        this.put(Objects.requireNonNull(name), this, busJS);
        return busJS;
    }

    @Override
    public String getClassName() {
        return "EventGroupJS";
    }
}
