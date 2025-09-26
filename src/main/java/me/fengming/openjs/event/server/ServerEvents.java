package me.fengming.openjs.event.server;

import me.fengming.openjs.event.js.EventBusJS;
import me.fengming.openjs.event.js.EventGroupJS;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.*;

/**
 * @author ZZZank
 */
public interface ServerEvents {
    EventGroupJS GROUP = new EventGroupJS("ServerEvents");

    EventBusJS<ServerAboutToStartEvent, Void> ABOUT_TO_START =
        GROUP.createBus("aboutToStart", ServerAboutToStartEvent.class);

    EventBusJS<ServerStartingEvent, Void> STARTING =
        GROUP.createBus("starting", ServerStartingEvent.class);

    EventBusJS<ServerStartedEvent, Void> STARTED =
        GROUP.createBus("started", ServerStartedEvent.class);

    EventBusJS<ServerStoppingEvent, Void> STOPPING =
        GROUP.createBus("stopping", ServerStoppingEvent.class);

    EventBusJS<ServerStoppedEvent, Void> STOPPED =
        GROUP.createBus("stopped", ServerStoppedEvent.class);

    EventBusJS<ServerChatEvent, Void> CHAT =
        GROUP.createBus("chat", ServerChatEvent.class);

    EventBusJS<TickEvent.ServerTickEvent, Void> TICK =
        GROUP.createBus("tick", TickEvent.ServerTickEvent.class);
}
