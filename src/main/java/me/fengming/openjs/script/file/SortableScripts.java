package me.fengming.openjs.script.file;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import me.fengming.openjs.OpenJS;
import me.fengming.openjs.script.ScriptProperty;
import me.fengming.openjs.utils.Cast;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZZZank
 */
public class SortableScripts {
    public final List<SortableScript> sortables;

    public SortableScripts(List<ScriptFile> collectedUnordered) {
        this.sortables = collectedUnordered.stream().map(SortableScript::new).toList();
    }

    private static Multimap<String, SortableScript> collectAfterReferences(List<SortableScript> sortables) {
        var afterReferences = HashMultimap.<String, SortableScript>create();
        for (var sortable : sortables) {
            for (var ref : collectAfterReference(sortable.file)) {
                afterReferences.put(ref, sortable);
            }
        }
        return afterReferences;
    }

    private static Collection<String> collectAfterReference(ScriptFile file) {
        var path = file.path;
        var parts = Arrays.asList(path.toString().split(path.getFileSystem().getSeparator()));
        var size = parts.size();
        if (size == 0) {
            return Collections.emptyList();
        } else if (size == 1) {
            // path: ab.js -> add ab
            // no wildcard
            ensureDotJSFile(parts);
            return parts;
        }

        var ref = new ArrayList<String>();

        // add wildcard reference
        for (int i = 2; i < size; i++) {
            // for path: ab/c/d.js, size = 3
            // i = 0 is empty list, so skip
            // i = 1 is 'ab', which will be '*' if transformed to wildcard reference, so skip
            // i = 2: ab/c -> add ab/*
            // i = 3: ab/c/d.js -> add ab/c/*
            var sub = parts.subList(0, i);
            var last = sub.get(i - 1);
            sub.set(i - 1, "*");
            ref.add(String.join("/", sub));
            sub.set(i - 1, last);
        }

        // add exact reference
        // for path: ab/c/d.js, add ab/c/d
        ensureDotJSFile(parts);
        ref.add(String.join("/", parts));

        return ref;
    }

    private static void ensureDotJSFile(List<@NotNull String> parts) {
        var size = parts.size();
        var last = parts.get(size - 1);
        if (!last.endsWith(".js")) {
            throw new IllegalArgumentException("script file is not referring to a .js file");
        }
        parts.set(size - 1, last.substring(0, last.length() - ".js".length()));
    }

    /**
     * fill dependencies based on {@link ScriptProperty#AFTER}
     * @return this
     */
    public SortableScripts fromPropertyAfter() {
        for (var sortable : sortables) {
            sortable.file.getProperties()
                .getOrDefault(ScriptProperty.AFTER)
                .stream()
                .map(this::dependenciesFromAfter)
                .forEach(sortable.dependencies::addAll);
        }
        return this;
    }

    private Collection<SortableScript> dependenciesFromAfter(String after) {
        /*TODO
        aaa/bbb -> depends on aaa/bbb.js
        aaa/bbb.js -> depends on aaa/bbb.js.js, I dont know why would users name their files as such, but anyway
        aaa/* -> depends on all files in aaa/
        aaa/someInvalidFile -> ignore and warn about it
         */
        var afterReferences = collectAfterReferences(this.sortables);
        validateParts(after.split("/"));
        var references = afterReferences.get(after);
        if (references.isEmpty()) {
            OpenJS.LOGGER.warn("'after' property '{}' does not refers to any actual script file", after);
        }
        return references;
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

    /**
     * fill dependencies based on {@link ScriptFile#getPriority()}
     * @return this
     */
    public SortableScripts fromPriority() {
        var prioritized = sortables
            .stream()
            .collect(Collectors.groupingBy(SortableScript::getPriority))
            .entrySet()
            .stream()
            .sorted(Cast.to(Map.Entry.comparingByKey().reversed())) //higher priority comes first
            .map(Map.Entry::getValue)
            .toList(); // each element contains scripts with same priority, higher priority -> smaller index
        var depends = new ArrayList<SortableScript>();
        for (var scripts : prioritized) {
            for (var script : scripts) {
                script.dependencies.addAll(depends);
            }
            depends.addAll(scripts); //depended on by later scripts
        }
        return this;
    }
}
