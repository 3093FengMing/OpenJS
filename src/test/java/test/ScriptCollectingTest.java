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
        Assertions.assertFalse(Main.FILES.isEmpty());
        Main.LOG.info(Main.FILES.size());
        Main.LOG.info(Main.FILES);
    }
}
