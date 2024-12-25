package me.fengming.openjs.utils.topo;

import java.util.*;

/**
 * trimmed version of <a href="https://github.com/ZZZank/TopoSort">TopoSort(GitHub)</a>
 * @author ZZZank
 */
public final class TopoSort {

    private static <T extends TopoSortable<T>> void indexSortableDependencies(
        Map<T, Integer> indexes,
        Map<Integer, Set<Integer>> requiredBy,
        Map<Integer, Set<Integer>> requires
    ) throws TopoPreconditionFailed {
        for (var e : indexes.entrySet()) {
            var sortable = e.getKey();
            var index = e.getValue();
            var dependencies = sortable.getDependencies();

            var dependencyIndexes = new TreeSet<Integer>();
            for (var dependency : dependencies) {
                var depIndex = indexes.get(dependency);
                dependencyIndexes.add(depIndex);
                if (depIndex == null) {
                    throw new TopoPreconditionFailed(
                        "%s (dependency of %s) not in input",
                        dependency,
                        sortable
                    );
                } else if (depIndex.equals(index)) {
                    throw new TopoPreconditionFailed("%s claimed itself as its dependency", sortable);
                }
                requiredBy.computeIfAbsent(depIndex, (k) -> new TreeSet<>()).add(index);
            }

            requires.put(index, dependencyIndexes);
        }
    }

    private static <T extends TopoSortable<T>> HashMap<T, Integer> indexSortables(List<T> input)
        throws TopoPreconditionFailed {
        var toIndexes = new HashMap<T, Integer>();
        for (int i = 0, size = input.size(); i < size; i++) {
            var sortable = input.get(i);
            var old = toIndexes.put(sortable, i);
            if (old != null) {
                throw new TopoPreconditionFailed("values in index %s and %s are same values", i, old);
            }
        }
        return toIndexes;
    }

    public static <T extends TopoSortable<T>> List<T> sort(List<T> input)
        throws TopoNotSolved, TopoPreconditionFailed {
        //construct object->index map, sorting will only use index for better generalization
        var indexes = indexSortables(input);

        //indexing dependencies
        var requiredBy = new TreeMap<Integer, Set<Integer>>();
        var requires = new TreeMap<Integer, Set<Integer>>();
        indexSortableDependencies(indexes, requiredBy, requires);

        var avaliables = new ArrayList<Integer>();
        for (var e : requires.entrySet()) {
            var dependencies = e.getValue();
            var index = e.getKey();
            if (dependencies.isEmpty()) {
                avaliables.add(index);
            }
        }

        //sort
        var sorted = new ArrayList<T>();
        while (!avaliables.isEmpty()) {
            var newlyFree = new ArrayList<Integer>();

            for (var free : avaliables) {
                sorted.add(input.get(free));
                var dependents = requiredBy.getOrDefault(free, Collections.emptySet());
                for (var dependent : dependents) {
                    var require = requires.get(dependent);
                    require.remove(free);
                    if (require.isEmpty()) {
                        newlyFree.add(dependent);
                    }
                }
            }

            avaliables = newlyFree;
        }
        validateResult(requires, input);
        return sorted;
    }

    private static <T extends TopoSortable<T>> void validateResult(
        Map<Integer, Set<Integer>> requires,
        List<T> input
    ) throws TopoNotSolved {
        for (var require : requires.values()) {
            if (!require.isEmpty()) {
                var unsolved = requires.entrySet()
                    .stream()
                    .filter(e -> !e.getValue().isEmpty())
                    .toList();
                throw new TopoNotSolved(unsolved, input);
            }
        }
    }
}
