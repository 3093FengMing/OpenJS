package me.fengming.openjs.script.file;

import me.fengming.openjs.Config;
import me.fengming.openjs.OpenJS;
import me.fengming.openjs.script.ScriptProperties;
import me.fengming.openjs.script.ScriptProperty;
import me.fengming.openjs.utils.Cast;
import me.fengming.openjs.utils.Utils;
import me.fengming.openjs.utils.topo.TopoNotSolved;
import me.fengming.openjs.utils.topo.TopoPreconditionFailed;
import me.fengming.openjs.utils.topo.TopoSort;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author ZZZank
 */
public class ScriptFileCollector {
    final Path root;

    public ScriptFileCollector(Path root) {
        this.root = root;
    }

    public List<ScriptFile> collectUnordered() throws IOException {
        Utils.checkPath(root);
        return Files.walk(root, 10, FileVisitOption.FOLLOW_LINKS)
            .filter(Files::isRegularFile)
            .filter(p -> p.getFileName().toString().endsWith(".js"))
            .map(this::ofScriptFile)
            .filter(Objects::nonNull)
            .filter(ScriptFile::shouldEnable)
            .toList();
    }

    public List<ScriptFile> collectSorted() throws IOException {
        var unordered = collectUnordered();
        try {
            return sortUnsafe(unordered);
        } catch (TopoNotSolved e) {
            OpenJS.LOGGER.error("OpenJS is unable to solve the script dependency relations provided by user (via 'after' property), falling back to priority-only mode");
            OpenJS.LOGGER.error(e.getMessage());
            //TODO: warn players in-game
        } catch (TopoPreconditionFailed e) {
            OpenJS.LOGGER.error("user declared invalid 'after' property, falling back to priority-only mode");
            OpenJS.LOGGER.error(e.getMessage());
            //TODO: warn players in-game
        }
        return sortFallback(unordered);
    }

    public List<ScriptFile> sortUnsafe(List<ScriptFile> unordered) {
        List<SortableScript> sortables;
        if (Config.strongPriority) {
            sortables = new SortableScripts(unordered, this.root)
                .fromPriority() // strong priority dependencies
                .fromPropertyAfter()
                .sortables;
        } else {
            var ordered = new ArrayList<>(unordered);
            ordered.sort(Comparator.comparingInt(ScriptFile::getPriority));
            // weak priority dependencies that depends on our TopoSort's stable nature to make then in priorities when
            // possible, and break priorities when necessary
            sortables = new SortableScripts(ordered, this.root)
                .fromPropertyAfter()
                .sortables;
        }
        return TopoSort.sort(sortables).stream().map(SortableScript::unwrap).toList();
    }

    public List<ScriptFile> sortFallback(List<ScriptFile> unordered) {
        // Java sort is stable
        var sorted = new ArrayList<>(unordered);
        sorted.sort(Comparator.comparingInt(ScriptFile::getPriority));
        return sorted;
    }

    @Nullable
    private ScriptFile ofScriptFile(Path path) {
        var file = new ScriptFile(path);
        try (var reader = Files.newBufferedReader(path)) {
            fillProperties(file.getProperties(), reader);
        } catch (IOException e) {
            OpenJS.LOGGER.error("error when reading script property", e);
            return null;
        }
        return file;
    }

    public static void fillProperties(ScriptProperties properties, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            if (!line.startsWith("//")) {
                break;
            }
            line = line.substring("//".length()).trim();
            var parts = line.split(":", 2);
            if (parts.length < 2) {
                continue;
            }
            var prop = ScriptProperty.get(parts[0].trim());
            if (prop.isPresent()) {
                var value = prop.get().read(parts[1].trim());
                properties.put(Cast.to(prop.get()), value);
            }
        }
    }
}
