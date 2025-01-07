package test;

import impl.Main;
import impl.SafeClosable;
import me.fengming.openjs.Config;
import me.fengming.openjs.script.file.ScriptFileCollector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author ZZZank
 */
public class PackModeTest {
    public static final Path PATH = Main.ROOT.resolve("pack_mode");

    private static SafeClosable withPackMode(String mode) {
        var old = Config.packMode;
        Config.packMode = mode;
        return () -> Config.packMode = old;
    }

    @Test
    void easy() {
        try (var ignored = withPackMode("easy")) {
            var collector = new ScriptFileCollector(PATH);
            var collected = collector.collectUnordered();
            // 5 = 3(easy) + 2(nope)
            Assertions.assertEquals(5, collected.size());
            for (var file : collected) {
                // priority is set manually
                Assertions.assertTrue(file.getPriority() <= 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void hard() {
        try (var ignored = withPackMode("hard")) {
            var collector = new ScriptFileCollector(PATH);
            var collected = collector.collectUnordered();
            // 4 = 2(hard) + 2(nope)
            Assertions.assertEquals(4, collected.size());
            for (var file : collected) {
                // priority is set manually
                Assertions.assertTrue(file.getPriority() >= 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void none() {
        try {
            var collector = new ScriptFileCollector(PATH);
            var collected = collector.collectUnordered();
            // 2 = 2(nope)
            Assertions.assertEquals(2, collected.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
