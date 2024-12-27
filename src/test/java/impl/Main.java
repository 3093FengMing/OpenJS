package impl;

import me.fengming.openjs.script.file.ScriptFile;
import me.fengming.openjs.script.file.ScriptFileCollector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author ZZZank
 */
public class Main {

    public static final Logger LOG = LogManager.getLogger("openjs_test");
    public static final Path ROOT = Path.of("src/test/resources");

    public static List<? extends ScriptFile> collect(Path path) {
        try {
            return new ScriptFileCollector(path).collectUnordered();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<? extends ScriptFile> collect(String relativePath) {
        return collect(ROOT.resolve(relativePath));
    }
}
