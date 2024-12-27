package me.fengming.openjs.script;

import me.fengming.openjs.utils.OpenJSPaths;

import java.nio.file.Path;

public enum ScriptType {
    SERVER(OpenJSPaths.SERVER),
    CLIENT(OpenJSPaths.CLIENT),
    STARTUP(OpenJSPaths.STARTUP),
    CORE(OpenJSPaths.STARTUP);

    public final Path scriptPath;

    ScriptType(Path scriptPath) {
        this.scriptPath = scriptPath;
    }
}
