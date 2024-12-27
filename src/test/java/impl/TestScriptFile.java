package impl;

import me.fengming.openjs.script.file.ScriptFile;

import java.nio.file.Path;

/**
 * @author ZZZank
 */
public class TestScriptFile extends ScriptFile {
    private final Path relative;

    public TestScriptFile(Path path, Path root) {
        super(path);
        this.relative = root.relativize(path);
    }

    @Override
    public String toString() {
        return relative.toString();
    }
}
