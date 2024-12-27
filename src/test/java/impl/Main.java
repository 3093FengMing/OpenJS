package impl;

import me.fengming.openjs.script.file.ScriptFile;
import me.fengming.openjs.script.file.ScriptFileCollector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * @author ZZZank
 */
public class Main {

    public static final Logger LOG = LogManager.getLogger("openjs_test");
    public static final List<? extends ScriptFile> FILES = collect();

    public static List<? extends ScriptFile> collect() {
        LOG.info(TestPaths.ROOT.toAbsolutePath());
        try {
            return new ScriptFileCollector(TestPaths.PROP).collectUnordered();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
