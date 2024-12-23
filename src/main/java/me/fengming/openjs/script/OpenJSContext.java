package me.fengming.openjs.script;

import me.fengming.openjs.Config;
import me.fengming.openjs.plugin.OpenJSRegistries;
import org.mozilla.javascript.*;

public class OpenJSContext extends Context {
    public Scriptable topScope;

    public OpenJSContext(ContextFactory contextFactory) {
        super(contextFactory);
    }

    public void init() {
        this.setOptimizationLevel(Config.optLevel);
    }

    public void load() {
        this.topScope = initSafeStandardObjects();
        OpenJSRegistries.BINDINGS.apply(this::addBinding);
    }

    protected void addBinding(Binding binding) {
        for (String alias : binding.alias()) {
            Object v = binding.value();
            /* defineClass
            if (v instanceof Scriptable sv) {
                try {
                    ScriptableObject.defineClass(topScope, sv.getClass());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            */
            if (v instanceof Class<?> cv) {
                ScriptableObject.putProperty(topScope, alias, new NativeJavaClass(topScope, cv));
            } else {
                ScriptableObject.putProperty(topScope, alias, javaToJS(v, topScope));
            }
        }
    }
}
