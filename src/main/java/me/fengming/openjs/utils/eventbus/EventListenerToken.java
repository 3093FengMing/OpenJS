package me.fengming.openjs.utils.eventbus;

/**
 * @author ZZZank
 */
public interface EventListenerToken<E> {
    Class<E> eventType();

    byte priority();
}
