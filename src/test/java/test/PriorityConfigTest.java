package test;

import impl.Main;
import impl.SafeClosable;
import me.fengming.openjs.Config;
import me.fengming.openjs.script.file.ScriptFileCollector;
import me.fengming.openjs.utils.topo.TopoNotSolved;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

/**
 * @author ZZZank
 */
public class PriorityConfigTest {

    public static final Path PATH = Main.ROOT.resolve("priority_config");

    private static SafeClosable withPriorityCfg(boolean value) {
        var old = Config.strongPriority;
        Config.strongPriority = value;
        return () -> Config.strongPriority = old;
    }

    @Test
    void strongPriority() {
        try (var ignored = withPriorityCfg(true)) {
            var collector = new ScriptFileCollector(PATH);
            Assertions.assertThrows(
                TopoNotSolved.class,
                () -> {
                    var unordered = collector.collectUnordered();
                    collector.sortUnsafe(unordered);
                }
            );
            Assertions.assertDoesNotThrow(
                collector::collectSorted
            );
        }
    }

    @Test
    void weakPriority() {
        try (var ignored = withPriorityCfg(false)) {
            Assertions.assertDoesNotThrow(
                () -> {
                    var collector = new ScriptFileCollector(PATH);
                    var unordered = collector.collectUnordered();
                    collector.sortUnsafe(unordered);
                }
            );
        }
    }
}
