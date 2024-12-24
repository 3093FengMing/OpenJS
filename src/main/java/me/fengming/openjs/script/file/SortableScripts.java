package me.fengming.openjs.script.file;

import me.fengming.openjs.OpenJS;
import me.fengming.openjs.script.ScriptProperty;
import me.fengming.openjs.utils.Cast;

import java.io.IOException;
import java.nio.file.Files;
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
    private boolean init;

    public SortableScripts(List<ScriptFile> files, Path base) {
        sortables = files.stream().map(SortableScript::new).toList();
        this.base = base;
    }

    public List<SortableScript> collect() {
        if (!init) {
            init = true;
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
                sortable.dependencies.addAll(dependenciesFromAfter(base, after));
            }
        }
    }

    private Collection<SortableScript> dependenciesFromAfter(Path base, String after) {
        /*TODO
        aaa/bbb -> depends on aaa/bbb.js
        aaa/* -> depends on all files in aaa/
        aaa/someInvalidFile -> ignore and warn about it
         */
        var parts = after.split("/");
        validateParts(parts);
        for (var part : parts) {
            if ("*".equals(part)) {
                try {
                    Files.walk(base); //TODO
                } catch (IOException e) {
                    return Collections.emptyList();
                }
            }
            base = base.resolve(part);
            if (!Files.exists(base)) {
                //error per script type
                OpenJS.LOGGER.error(
                    "'after' property '{}' cannot be resolved to any actual script file, ignoring",
                    after
                );
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    private void validateParts(String[] parts) {
        for (int i = 0; i < parts.length; i++) {
            var part = parts[i];
            if ("*".equals(part) && i != parts.length - 1) {
                throw new IllegalArgumentException(
                    "wildcard match '*' should only be the last part of a 'after' property");
            }
        }
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
