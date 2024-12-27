package me.fengming.openjs.script.file;

import me.fengming.openjs.utils.topo.TopoSortable;

import java.nio.file.Path;
import java.util.*;

/**
 * @author ZZZank
 */
public class SortableScript implements TopoSortable<SortableScript> {

    public final Path relative;
    public final ScriptFile file;
    public final Set<SortableScript> dependencies = new HashSet<>();

    public SortableScript(Path root, ScriptFile file) {
        this.relative = root.relativize(file.path);
        this.file = file;
    }

    public ScriptFile unwrap() {
        return file;
    }

    public Integer getPriority() {
        return file.getPriority();
    }

    @Override
    public Collection<SortableScript> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SortableScript that && Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(file);
    }

    public String pathString() {
        return this.relative.toString();
    }

    @Override
    public String toString() {
        return "SortableScript{"
            + pathString()
            + ": " + dependencies.stream().map(SortableScript::pathString).toList()
            + '}';
    }
}
