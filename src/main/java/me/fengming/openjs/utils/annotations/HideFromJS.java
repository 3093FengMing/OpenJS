package me.fengming.openjs.utils.annotations;

import java.lang.annotation.*;

// TODO: Who knows, todo
/**
 * In theory, the IDE's Tab Auto-completion (or IntelliSense)
 * should not include things annotated by this annotation.
 * @author FengMing
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
public @interface HideFromJS {
}
