package me.fengming.openjs.script.file;

import me.fengming.openjs.script.ScriptProperty;
import me.fengming.openjs.utils.Cast;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZZZank
 */
public class SortableScripts {
    private final List<SortableScript> sortables;
    private final Path base;
    private boolean inited;

    public SortableScripts(List<ScriptFile> files, Path base) {
        sortables = files.stream().map(SortableScript::new).toList();
        this.base = base;
    }

    public List<SortableScript> collect() {
        if (!inited) {
            inited = true;
            fromPriority();
            fromPropertyAfter(base);
        }
        return sortables;
    }

    /**
     * @see ScriptProperty#AFTER
     */
    private void fromPropertyAfter(Path base) {
        for (var sortable : sortables) {
            var afters = sortable.file.getProperties().getOrDefault(ScriptProperty.AFTER);
            for (var after : afters) {
                sortable.dependencies.addAll(dependenciesFromAfter(after));
            }
        }
    }

    private Collection<SortableScript> dependenciesFromAfter(String after) {
        /*
        aaa/bbb -> depends on aaa/bbb.js
        aaa/* -> depends on all files in aaa/
        aaa/someInvalidFile -> ignore and warn about it
         */
        return Collections.emptyList();
    }

    private void fromPriority() {
        var prioritized = sortables
            .stream()
            .collect(Collectors.groupingBy(SortableScript::getPriority))
            .entrySet()
            .stream()
            .sorted(Cast.to(Map.Entry.comparingByKey().reversed())) //higher priority comes first
            .map(Map.Entry::getValue)
            .toList(); // each element contains scripts with same priority, higher priority -> smaller index
        for (int i = 0, prioritizedSize = prioritized.size(); i < prioritizedSize; i++) {
            var scripts = prioritized.get(i);
            var depends = prioritized.subList(0, i)
                .stream()
                .flatMap(Collection::stream)
                .toList();
            for (var script : scripts) {
                script.dependencies.addAll(depends);
            }
        }
    }
}
