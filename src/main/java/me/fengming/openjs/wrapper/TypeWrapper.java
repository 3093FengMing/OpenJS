package me.fengming.openjs.wrapper;

import org.mozilla.javascript.Context;

/**
 * @author FengMing
 */
@FunctionalInterface
public interface TypeWrapper<T> {
    T wrap(Context context, Object from);
}
