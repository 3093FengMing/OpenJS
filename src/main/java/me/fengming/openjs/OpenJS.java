package me.fengming.openjs;

import com.mojang.logging.LogUtils;
import me.fengming.openjs.plugin.IOpenJSPlugin;
import me.fengming.openjs.plugin.OpenJSPlugins;
import me.fengming.openjs.script.ScriptManager;
import me.fengming.openjs.script.ScriptType;
import me.fengming.openjs.utils.OpenJSPaths;
import net.minecraftforge.fml.ModList;
import org.slf4j.Logger;

public class OpenJS {
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * game dir is null when in testing, so disable this for now to prevent NPE during class init
     * @see OpenJSPaths#GAMEDIR
     */
//    public static final ScriptManager STARTUP_SCRIPT = new ScriptManager(ScriptType.STARTUP);

    public static void init() {
        // Load plugin
        ModList.get().getModFiles().forEach(OpenJSPlugins::load);
        OpenJSPlugins.postAction(IOpenJSPlugin::load);

        OpenJSPaths.check();

//        STARTUP_SCRIPT.load();
    }
}
