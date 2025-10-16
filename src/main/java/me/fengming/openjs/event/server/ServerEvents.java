package me.fengming.openjs.event.server;

import me.fengming.openjs.event.js.EventBusForgeBridge;
import me.fengming.openjs.event.js.EventBusJS;
import me.fengming.openjs.event.js.EventGroupJS;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.*;

/**
 * @author ZZZank
 */
public interface ServerEvents {
    EventGroupJS GROUP = new EventGroupJS("ServerEvents");

    EventBusJS<ServerAboutToStartEvent, Void> ABOUT_TO_START =
        GROUP.addBus("aboutToStart", ServerAboutToStartEvent.class);

    EventBusJS<ServerStartingEvent, Void> STARTING =
        GROUP.addBus("starting", ServerStartingEvent.class);

    EventBusJS<ServerStartedEvent, Void> STARTED =
        GROUP.addBus("started", ServerStartedEvent.class);

    EventBusJS<ServerStoppingEvent, Void> STOPPING =
        GROUP.addBus("stopping", ServerStoppingEvent.class);

    EventBusJS<ServerStoppedEvent, Void> STOPPED =
        GROUP.addBus("stopped", ServerStoppedEvent.class);

    EventBusJS<ServerChatEvent, Void> CHAT =
        GROUP.addBus("chat", ServerChatEvent.class);

    EventBusJS<TickEvent.ServerTickEvent, Void> TICK =
        GROUP.addBus("tick", TickEvent.ServerTickEvent.class);

    EventBusForgeBridge FORGE_BRIDGE = EventBusForgeBridge.create(MinecraftForge.EVENT_BUS)
        .bind(ABOUT_TO_START)
        .bind(STARTING)
        .bind(STARTED)
        .bind(STOPPING)
        .bind(STOPPED)
        .bind(CHAT)
        .bind(TICK);
}
