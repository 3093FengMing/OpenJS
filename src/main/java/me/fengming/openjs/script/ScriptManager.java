package me.fengming.openjs.script;

import me.fengming.openjs.OpenJS;
import me.fengming.openjs.script.file.ScriptFile;
import me.fengming.openjs.utils.Utils;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ScriptManager {
    private final ScriptType type;
    private OpenJSContextFactory factory;

    private final List<ScriptFile> scriptFiles = new ArrayList<>();

    public ScriptManager(ScriptType type) {
        this.type = type;
    }

    public void addAllScripts(Path path) {
        try {
            Utils.checkPath(path);
            Files.walk(path, 10, FileVisitOption.FOLLOW_LINKS)
                    .filter(Files::isRegularFile)
                    .forEach(this::addScriptFile);
        } catch (IOException e) {
            OpenJS.LOGGER.error(e.getMessage());
        }
    }

    public void addScriptFile(Path path) {
        addScriptFile(new ScriptFile(path));
    }

    public void addScriptFile(ScriptFile file) {
        scriptFiles.add(file);
    }

    public void load() {
        factory = new OpenJSContextFactory(this);
        OpenJSContext context = (OpenJSContext) factory.enterContext();

        context.load();

        if (scriptFiles.isEmpty()) {
            addAllScripts(this.type.scriptPath);
        }
        for (ScriptFile file : scriptFiles) {
            file.load(context);
        }
    }
}
