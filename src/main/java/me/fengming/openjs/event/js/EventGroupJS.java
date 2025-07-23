package me.fengming.openjs.event.js;

import me.fengming.openjs.utils.eventbus.CancellableEventBus;
import me.fengming.openjs.utils.eventbus.EventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchCancellableEventBus;
import me.fengming.openjs.utils.eventbus.dispatch.DispatchEventBus;
import org.mozilla.javascript.ScriptableObject;

import java.util.Objects;

/**
 * @author ZZZank
 */
public class EventGroupJS extends ScriptableObject {

    public <E> EventBusJS.Regular<E> addBus(String name, EventBus<E> bus) {
        var busJS = new EventBusJS.Regular<>(bus);
        this.put(Objects.requireNonNull(name), this, busJS);
        return busJS;
    }

    public <E> EventBusJS.Cancellable<E> addBus(String name, CancellableEventBus<E> bus) {
        var busJS = new EventBusJS.Cancellable<>(bus);
        this.put(Objects.requireNonNull(name), this, busJS);
        return busJS;
    }

    public <E, K> EventBusJS.Dispatch<E, K> addBus(String name, DispatchEventBus<E, K> bus) {
        var busJS = new EventBusJS.Dispatch<>(bus);
        this.put(Objects.requireNonNull(name), this, busJS);
        return busJS;
    }

    public <E, K> EventBusJS.DispatchCancellable<E, K> addBus(String name, DispatchCancellableEventBus<E, K> bus) {
        var busJS = new EventBusJS.DispatchCancellable<>(bus);
        this.put(Objects.requireNonNull(name), this, busJS);
        return busJS;
    }

    @Override
    public String getClassName() {
        return "EventGroupJS";
    }
}
