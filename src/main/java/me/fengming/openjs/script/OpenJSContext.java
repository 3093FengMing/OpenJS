package me.fengming.openjs.script;

import me.fengming.openjs.Config;
import me.fengming.openjs.binding.base.Binding;
import me.fengming.openjs.registry.OpenJSRegistries;
import org.mozilla.javascript.*;

/**
 * @author FengMing
 */
public class OpenJSContext extends Context {
    public final ScriptType type;
    public Scriptable topScope;

    public OpenJSContext(OpenJSContextFactory factory) {
        super(factory);
        type = factory.type;
    }

    public void init() {
        this.setLanguageVersion(Context.VERSION_ES6);
        this.setOptimizationLevel(Config.optLevel);
    }

    public void load() {
        this.topScope = initStandardObjects();
        OpenJSRegistries.BINDINGS.get(type).ifPresent(b -> b.apply(this::addBinding));
        setWrapFactory(new OpenJSWrapFactory());
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
        binding.onLoad(this.type);
    }
}
