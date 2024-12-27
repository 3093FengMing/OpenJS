package me.fengming.openjs.script.file;

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

    public List<ScriptFile> collect() throws IOException {
        var unordered = collectUnordered();
        var sortables = new SortableScripts(unordered, this.root)
            .fromPriority()
            .fromPropertyAfter()
            .sortables;
        try {
            return TopoSort.sort(sortables).stream().map(SortableScript::unwrap).toList();
        } catch (TopoNotSolved e) {
            //TODO: warn users
        } catch (TopoPreconditionFailed e) {
            //TODO: warn users
        }
        return TopoSort
            .sort(new SortableScripts(unordered, this.root).fromPriority().sortables)
            .stream()
            .map(SortableScript::unwrap)
            .toList();
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
