package test;

import impl.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ZZZank
 */
public class ScriptCollectingTest {

    @Test
    void print() {
        var files = Main.collect(Main.ROOT);
        Assertions.assertFalse(files.isEmpty());
        Main.LOG.info(files.size());
        Main.LOG.info(
            files.stream()
            .map(f -> f.path)
            .map(Main.ROOT::relativize)
            .toList()
        );
    }
}
