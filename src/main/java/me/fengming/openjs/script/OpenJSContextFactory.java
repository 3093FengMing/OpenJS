package me.fengming.openjs.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

public class OpenJSContextFactory extends ContextFactory {

    @Override
    protected boolean hasFeature(Context cx, int featureIndex) {
        return switch (featureIndex) {
            case Context.FEATURE_ENABLE_JAVA_MAP_ACCESS -> true;
            default -> super.hasFeature(cx, featureIndex);
        };
    }

    @Override
    protected Context makeContext() {
        OpenJSContext context = new OpenJSContext(this);
        context.init();
        return context;
    }
}
