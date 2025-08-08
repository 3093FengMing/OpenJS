package me.fengming.openjs;

import me.fengming.openjs.binding.PackMode;
import me.fengming.openjs.script.ScriptProperty;

/**
 * @author FengMing
 */
public class Config {
    /**
     * Run script in Interpreter Mode or Compiler Mode
     */
    public static boolean interpretMode = true;
    /**
     * strong priority requirement
     * <p>
     * if {@code true}, {@link ScriptProperty#AFTER} must not conflict with {@link ScriptProperty#PRIORITY},
     * or topo sort will fail
     * <p>
     * if {@code false}, {@link ScriptProperty#AFTER} can overwrite dependency relations, aka TopoSort can
     * ignore {@link ScriptProperty#PRIORITY} if necessary
     */
    public static boolean strongPriority = false;
    public static String packMode = PackMode.NONE;
}
