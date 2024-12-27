package me.fengming.openjs.script;

import me.fengming.openjs.OpenJS;
import me.fengming.openjs.script.file.ScriptFile;
import me.fengming.openjs.script.file.ScriptFileCollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScriptManager {
    private final ScriptType type;
    private OpenJSContextFactory factory;

    private final List<ScriptFile> scriptFiles = new ArrayList<>();

    public ScriptManager(ScriptType type) {
        this.type = type;
    }

    public void addScriptFile(ScriptFile file) {
        scriptFiles.add(file);
    }

    public void load() {
        factory = new OpenJSContextFactory(this);
        OpenJSContext context = (OpenJSContext) factory.enterContext();

        context.load();

        try {
            this.scriptFiles.addAll(new ScriptFileCollector(this.type.scriptPath).collectSorted());
        } catch (IOException e) {
            OpenJS.LOGGER.error(e.getMessage());
        }

        for (ScriptFile file : scriptFiles) {
            file.load(context);
        }
    }
}
