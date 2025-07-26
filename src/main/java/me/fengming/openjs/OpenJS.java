package me.fengming.openjs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import me.fengming.openjs.plugin.IOpenJSPlugin;
import me.fengming.openjs.plugin.OpenJSPlugins;
import me.fengming.openjs.registry.OpenJSRegistries;
import me.fengming.openjs.script.ScriptManager;
import me.fengming.openjs.script.ScriptType;
import me.fengming.openjs.utils.OpenJSPaths;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.slf4j.Logger;

/**
 * @author FengMing
 */
public class OpenJS {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = new GsonBuilder().create();

    /**
     * {@link OpenJSPaths#GAMEDIR} is null when in testing, so initialization is deferred to prevent NPE during class init
     */
    private static ScriptManager STARTUP_SCRIPT;

    public static void init() {
        // Load plugin
        ModList.get().getModFiles().forEach(OpenJSPlugins::load);

        OpenJSPlugins.forEach(IOpenJSPlugin::load);
        OpenJSPlugins.forEach(p -> OpenJSRegistries.EVENT_GROUPS.forEach(p::registerEvent));
        OpenJSPlugins.forEach(p -> OpenJSRegistries.BINDINGS.forEach(p::registerBinding));

        OpenJSPaths.check();

        STARTUP_SCRIPT = new ScriptManager(ScriptType.STARTUP);
        STARTUP_SCRIPT.load();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(OpenJSMod.MODID, path);
    }

    public static ScriptManager getStartupScript() {
        return STARTUP_SCRIPT;
    }
}
