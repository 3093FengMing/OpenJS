package me.fengming.openjs;

import com.mojang.logging.LogUtils;
import me.fengming.openjs.plugin.IOpenJSPlugin;
import me.fengming.openjs.plugin.OpenJSPlugins;
import me.fengming.openjs.registry.OpenJSRegistries;
import me.fengming.openjs.script.ScriptManager;
import me.fengming.openjs.script.ScriptType;
import me.fengming.openjs.utils.OpenJSPaths;
import net.minecraftforge.fml.ModList;
import org.slf4j.Logger;

import java.util.function.Consumer;

/**
 * @author FengMing
 */
public class OpenJS {
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * {@link OpenJSPaths#GAMEDIR} is null when in testing, so initialization is deferred to prevent NPE during class init
     */
    private static ScriptManager STARTUP_SCRIPT;

    public static void init() {
        // Load plugin
        ModList.get().getModFiles().forEach(OpenJSPlugins::load);
        postAction(IOpenJSPlugin::load);
        postAction(p -> p.registerEvent(OpenJSRegistries.EVENT_GROUPS));
        postAction(p -> p.registerBinding(OpenJSRegistries.BINDINGS));

        OpenJSPaths.check();

        STARTUP_SCRIPT = new ScriptManager(ScriptType.STARTUP);
        STARTUP_SCRIPT.load();
    }

    public static ScriptManager getStartupScript() {
        return STARTUP_SCRIPT;
    }

    public static void postAction(Consumer<IOpenJSPlugin> consumer) {
        OpenJSPlugins.plugins.forEach(consumer);
    }
}
