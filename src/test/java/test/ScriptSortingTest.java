package test;

import impl.Main;
import impl.TestPaths;
import me.fengming.openjs.script.file.SortableScripts;
import me.fengming.openjs.utils.topo.TopoSort;
import org.junit.jupiter.api.Test;

/**
 * @author ZZZank
 */
public class ScriptSortingTest {

    @Test
    void readDependencies() {
        var sortables = new SortableScripts(Main.FILES, TestPaths.PROP)
            .fromPriority()
            .fromPropertyAfter()
            .sortables;
        Main.LOG.info(sortables);
    }

    @Test
    void sort() {
        var sortables = new SortableScripts(Main.FILES, TestPaths.PROP)
            .fromPriority()
            .fromPropertyAfter()
            .sortables;
        Main.LOG.info(TopoSort.sort(sortables));
    }
}
