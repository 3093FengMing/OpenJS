package test;

import impl.Main;
import me.fengming.openjs.script.file.ScriptFile;
import me.fengming.openjs.script.file.SortableScripts;
import me.fengming.openjs.utils.topo.TopoSort;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

/**
 * @author ZZZank
 */
public class ScriptSortingTest {

    public static final Path PATH = Main.ROOT.resolve("sort");
    public static final List<? extends ScriptFile> FILES = Main.collect(PATH);

    @Test
    void readDependencies() {
        var sortables = new SortableScripts(FILES, PATH)
            .fromPriority()
            .fromPropertyAfter()
            .sortables;
        Main.LOG.info(sortables);
    }

    @Test
    void sort() {
        var sortables = new SortableScripts(FILES, PATH)
            .fromPriority()
            .fromPropertyAfter()
            .sortables;
        Main.LOG.info(TopoSort.sort(sortables));
    }
}
