package me.fengming.openjs.wrapper;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * @author ZZZank
 */
public class BuiltinTypeWrappers {

    public static ResourceLocation resourceLocation(Object o) {
        if (o instanceof ResourceLocation rl) {
            return rl;
        } else if (o instanceof ResourceKey<?> key) {
            return key.location();
        }
        return ResourceLocation.tryParse(String.valueOf(o));
    }
}
