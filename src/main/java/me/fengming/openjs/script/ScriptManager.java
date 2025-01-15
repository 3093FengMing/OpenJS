package me.fengming.openjs.script;

import me.fengming.openjs.OpenJS;
import me.fengming.openjs.script.file.ScriptFile;
import me.fengming.openjs.script.file.ScriptFileCollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FengMing
 */
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
        factory = new OpenJSContextFactory(type);
        // 1. enterContext() will attach context to current thread, and will do nothing when there's already one attached
        // 2. Rhino internals heavily rely on 'context on current thread'
        // 3. there's no guarantee that server/client/startup/core will use different thread
        // so, ScriptType specific things should NOT be in context, which, unfortunately, affects bindings
        OpenJSContext context = (OpenJSContext) factory.enterContext();

        // doc of enterContext(): Get a context associated with the current thread, creating one if need be
        // context might not be newly created, how to solve this
        context.load();

        try {
            this.scriptFiles.addAll(new ScriptFileCollector(this.type.scriptPath).collectSorted());
        } catch (IOException e) {
            type.logger.error(e.getMessage());
        }

        for (var file : scriptFiles) {
            file.load(context);
        }
    }
}
