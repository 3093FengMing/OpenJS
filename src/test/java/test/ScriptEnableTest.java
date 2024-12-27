package test;

import impl.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ZZZank
 */
public class ScriptEnableTest {

    @Test
    void test() {
        var files = Main.collect("enable");
        Assertions.assertTrue(files.isEmpty());
    }
}
