package me.fengming.openjs.script;

import me.fengming.openjs.Config;
import me.fengming.openjs.registry.OpenJSRegistries;
import org.mozilla.javascript.*;

/**
 * @author FengMing
 */
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

    protected void addBinding(String key, Binding binding) {
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
            ScriptableObject.putProperty(topScope, key, new NativeJavaClass(topScope, cv));
        } else {
            ScriptableObject.putProperty(topScope, key, javaToJS(v, topScope));
        }
    }
}
