package me.fengming.openjs.script;

import me.fengming.openjs.OpenJSMod;
import me.fengming.openjs.utils.OpenJSPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public enum ScriptType {
    SERVER(OpenJSPaths.SERVER),
    CLIENT(OpenJSPaths.CLIENT),
    STARTUP(OpenJSPaths.STARTUP),
    CORE(OpenJSPaths.STARTUP);

    public final Path scriptPath;
    public final Logger logger = LogManager.getLogger("%s:%s".formatted(OpenJSMod.MODID, name()));

    ScriptType(Path scriptPath) {
        this.scriptPath = scriptPath;
    }
}
