package me.fengming.openjs.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

public class OpenJSContextFactory extends ContextFactory {

    public final ScriptType type;

    public OpenJSContextFactory(ScriptType type) {
        this.type = type;
    }

    @Override
    protected boolean hasFeature(Context cx, int featureIndex) {
        return switch (featureIndex) {
            case Context.FEATURE_ENABLE_JAVA_MAP_ACCESS,
                 Context.FEATURE_INTEGER_WITHOUT_DECIMAL_PLACE -> true;
            default -> super.hasFeature(cx, featureIndex);
        };
    }

    @Override
    protected OpenJSContext makeContext() {
        OpenJSContext context = new OpenJSContext(this);
        context.init();
        return context;
    }
}
