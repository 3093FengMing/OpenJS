package me.fengming.openjs.script;

import me.fengming.openjs.wrapper.type.TypeWrappers;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

/**
 * @author FengMing
 */
public class OpenJSWrapFactory extends WrapFactory {
    @Override
    public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class<?> staticType) {
        var wrapper = TypeWrappers.get(javaObject.getClass());
        var wrapped = wrapper == null ? javaObject : wrapper.wrap(javaObject, javaObject.getClass());
        return super.wrapAsJavaObject(cx, scope, wrapped, staticType);
    }
}
