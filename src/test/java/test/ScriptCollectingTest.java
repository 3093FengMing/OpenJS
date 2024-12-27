package test;

import impl.Main;
import impl.TestPaths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ZZZank
 */
public class ScriptCollectingTest {

    @Test
    void print() {
        Assertions.assertFalse(Main.FILES.isEmpty());
        Main.LOG.info(Main.FILES.size());
        Main.LOG.info(
            Main.FILES.stream()
            .map(f -> f.path)
            .map(TestPaths.ROOT::relativize)
            .toList()
        );
    }
}
