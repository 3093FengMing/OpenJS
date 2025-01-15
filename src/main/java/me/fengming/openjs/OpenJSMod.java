package me.fengming.openjs;

import me.fengming.openjs.event.ForgeEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author FengMing
 */
@Mod(OpenJSMod.MODID)
public class OpenJSMod {
    public static final String MODID = "openjs";

    private static IEventBus MOD_BUS;

    public OpenJSMod(FMLJavaModLoadingContext context) {
        MOD_BUS = context.getModEventBus();

        MOD_BUS.addListener(this::commonSetup);

        MOD_BUS.addListener(EventPriority.LOWEST, ForgeEventHandler::handleRegister);

        OpenJS.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        OpenJS.LOGGER.info("OpenJS Common Setup");
    }

    /**
     * {@link IModBusEvent} -> mod event bus
     * <p>
     * regular {@link Event} -> {@link MinecraftForge#EVENT_BUS}
     */
    public static IEventBus selectBus(Class<? extends Event> eventType) {
        return IModBusEvent.class.isAssignableFrom(eventType)
            ? MOD_BUS
            : MinecraftForge.EVENT_BUS;
    }
}
