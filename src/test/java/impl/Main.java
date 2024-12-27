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
    public static final Path RES = Path.of("src/test/resources");
    public static final List<? extends ScriptFile> FILES = collect();

    public static List<? extends ScriptFile> collect() {
        var root = RES.resolve("script_prop");
        LOG.info(root.toAbsolutePath());
        LOG.info(RES.toAbsolutePath());
        try {
            return new ScriptFileCollector(root)
                .collectUnordered()
                .stream()
                .map((f) -> new TestScriptFile(f.path, root))
                .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
