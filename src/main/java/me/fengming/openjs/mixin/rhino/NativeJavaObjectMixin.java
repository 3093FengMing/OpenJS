package me.fengming.openjs.mixin.rhino;

import me.fengming.openjs.wrapper.type.TypeWrappers;
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
        }
    }

    @Inject(method = "coerceTypeImpl", at = @At("HEAD"), cancellable = true)
    private static void injectHeadCoerceTypeImpl(Class<?> type, Object value, CallbackInfoReturnable<Object> cir) {
        final var wrapper = TypeWrappers.getValidated(type, value, type);
        if (wrapper != null) {
            cir.setReturnValue(wrapper.wrap(value, type));
        }
    }
}
