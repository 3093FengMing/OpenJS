package me.fengming.openjs.mixin.rhino;

import me.fengming.openjs.wrapper.TypeWrappers;
import org.mozilla.javascript.NativeJavaObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author FengMing
 */
@Mixin(value = NativeJavaObject.class, remap = false)
public class NativeJavaObjectMixin {

    @Inject(method = "getConversionWeight", at = @At("HEAD"), cancellable = true)
    private static void injectHeadGetConversionWeight(Object fromObj, Class<?> to, CallbackInfoReturnable<Integer> cir) {
        if (TypeWrappers.hasWrapper(to)) {
            cir.setReturnValue(1);
            cir.cancel();
        }
    }

    @Inject(method = "coerceTypeImpl", at = @At("HEAD"), cancellable = true)
    private static void injectHeadCoerceTypeImpl(Class<?> type, Object value, CallbackInfoReturnable<Object> cir) {
        if (TypeWrappers.hasWrapper(type)) {
            Object wrapped = TypeWrappers.wrap(type, value);
            cir.setReturnValue(wrapped);
            cir.cancel();
        }
    }
}
