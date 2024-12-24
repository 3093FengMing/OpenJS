package me.fengming.openjs.script.file;

import me.fengming.openjs.utils.topo.TopoSortable;

import java.util.*;

/**
 * @author ZZZank
 */
public class SortableScript implements TopoSortable<SortableScript> {

    public final ScriptFile file;
    public final Set<SortableScript> dependencies = new HashSet<>();

    public SortableScript(ScriptFile file) {
        this.file = file;
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
}
