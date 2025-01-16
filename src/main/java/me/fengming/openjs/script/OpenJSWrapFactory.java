package me.fengming.openjs.script;

import me.fengming.openjs.wrapper.TypeWrappers;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

/**
 * @author FengMing
 */
public class OpenJSWrapFactory extends WrapFactory {
    @Override
    public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class<?> staticType) {
        var wrapped = TypeWrappers.hasWrapper(javaObject.getClass())
                ? TypeWrappers.wrap(javaObject.getClass(), javaObject, cx)
                : javaObject;
        return super.wrapAsJavaObject(cx, scope, wrapped, staticType);
    }
}
