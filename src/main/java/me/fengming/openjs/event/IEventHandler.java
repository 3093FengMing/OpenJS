package me.fengming.openjs.event;

/**
 * @author FengMing
 */
@FunctionalInterface
public interface IEventHandler {
    Object onEvent(OpenJSEvent event);
}

