package me.fengming.openjs.utils.topo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ZZZank
 */
public class TopoNotSolved extends RuntimeException {

    public final List<Map.Entry<Integer, Set<Integer>>> unsolved;
    private final List<? extends TopoSortable<?>> sortables;

    public TopoNotSolved(
        List<Map.Entry<Integer, Set<Integer>>> unsolved,
        List<? extends TopoSortable<?>> sortables
    ) {
        this.unsolved = unsolved;
        this.sortables = sortables;
    }

    public TopoSortable<?> getFromIndex(int index) {
        return sortables.get(index);
    }

    public String getFullMessage() {
        var lines = new ArrayList<String>(unsolved.size() + 1);
        lines.add(getMessage());
        for (var entry : unsolved) {
            var sortable = getFromIndex(entry.getKey());
            var dependencies = entry.getValue()
                .stream()
                .map(this::getFromIndex)
                .toList();
            lines.add(sortable + " requires: " + dependencies);
        }
        return String.join("\n", lines);
    }

    @Override
    public String getMessage() {
        var sortableInString = unsolved.stream()
            .map(Map.Entry::getKey)
            .map(sortables::get)
            .map(Object::toString)
            .collect(Collectors.joining(", "));
        return String.format("there are %s unsolved sortables: %s", unsolved.size(), sortableInString);
    }
}
