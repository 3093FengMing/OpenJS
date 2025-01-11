package me.fengming.openjs.plugin;

import me.fengming.openjs.OpenJS;
import net.minecraftforge.forgespi.language.IModFileInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FengMing
 */
public class OpenJSPlugins {
    public static final List<IOpenJSPlugin> plugins = new ArrayList<>();

    public static void load(IModFileInfo info) {
        Path path = info.getFile().findResource("openjs.plugins");
        if (Files.exists(path)) {
            try {
                for (String l : Files.readAllLines(path)) {
                    IOpenJSPlugin plugin = Class.forName(l).asSubclass(IOpenJSPlugin.class).getDeclaredConstructor().newInstance();

                    plugins.add(plugin);
                }
            } catch (Exception e) {
                OpenJS.LOGGER.error(e.getMessage());
            }
        }
    }
}
