package impl;

import me.fengming.openjs.script.file.ScriptFile;
import me.fengming.openjs.script.file.ScriptFileCollector;
import me.fengming.openjs.script.file.SortableScript;
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
    public static final List<? extends ScriptFile> FILES = collect();

    public static List<? extends ScriptFile> collect() {
        LOG.info(TestPaths.ROOT.toAbsolutePath());
        try {
            return new ScriptFileCollector(TestPaths.PROP).collectUnordered();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fileToStr(ScriptFile file, Path root) {
        return root.relativize(file.path).toString();
    }

    public static String fileToStr(SortableScript file, Path root) {
        return fileToStr(file.file, root);
    }
}
