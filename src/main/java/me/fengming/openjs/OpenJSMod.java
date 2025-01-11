package me.fengming.openjs;

import me.fengming.openjs.event.ForgeEventHandler;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author FengMing
 */
@Mod(OpenJSMod.MODID)
public class OpenJSMod {
    public static final String MODID = "openjs";

    public OpenJSMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);


        modEventBus.addListener(EventPriority.LOWEST, ForgeEventHandler::handleRegister);

        OpenJS.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        OpenJS.LOGGER.info("OpenJS Common Setup");
    }
}
