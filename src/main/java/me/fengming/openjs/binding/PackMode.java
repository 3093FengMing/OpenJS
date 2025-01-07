package me.fengming.openjs.binding;

import me.fengming.openjs.Config;

import java.util.Objects;

/**
 * @author ZZZank
 */
public class PackMode {
    public static final String NONE = "*";

    public static boolean is(String mode) {
        return Config.packMode.equals(mode);
    }

    public static boolean isNot(String mode) {
        return !is(mode);
    }

    public static String get() {
        return Config.packMode;
    }

    public static void set(String mode) {
        //TODO: reload script on set
        Config.packMode = Objects.requireNonNull(mode);
    }

    public static void reset() {
        set(NONE);
    }
}
