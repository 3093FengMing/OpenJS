package me.fengming.openjs.utils.eventbus.impl;

import me.fengming.openjs.utils.eventbus.EventBus;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author ZZZank
 */
public class EventBusImpl<E> extends EventBusBase<E, Consumer<E>> implements EventBus<E> {

    public EventBusImpl(Class<E> eventType) {
        super(eventType);
    }

    @Override
    public final boolean post(E event) {
        getBuilt(EventBusImpl::compile).accept(event);
        return false;
    }

    private static <E> Consumer<E> compile(Stream<Consumer<E>> consumerStream) {
        var arr = (Consumer<E>[]) consumerStream.toArray(Consumer[]::new);
        switch (arr.length) {
            case 0:
                return (ignored) -> {};
            case 1:
                return arr[0];
            case 2:
                return arr[0].andThen(arr[1]);
            case 3:
                var c1 = arr[0];
                var c2 = arr[1];
                var c3 = arr[2];
                return event -> {
                    c1.accept(event);
                    c2.accept(event);
                    c3.accept(event);
                };
            default:
                return event -> {
                    for (var consumer : arr) {
                        consumer.accept(event);
                    }
                };
        }
    }
}
