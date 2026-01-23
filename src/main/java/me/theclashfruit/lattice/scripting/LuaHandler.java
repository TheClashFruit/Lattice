package me.theclashfruit.lattice.scripting;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static me.theclashfruit.lattice.LatticePlugin.LOGGER;

public class LuaHandler {
    public LuaHandler() {

    }

    public void setup(@NotNull Path path) {
        File pathFile = path.toFile();

        if (!pathFile.exists())
            return;
        if (!pathFile.isDirectory())
            return;

        List<File> files = Arrays.stream(pathFile.listFiles())
                .filter(f -> f.isFile() && f.getName().endsWith(".lua"))
                .toList();

        if (files.isEmpty())
            return;
        for (File file : files)
            loadScript(file);
    }

    public void shutdown() {
        // TODO: Implement
    }

    private Globals getGlobals() {
        return JsePlatform.standardGlobals();
    }

    private void loadScript(File file) {
        try {
            Globals globals = getGlobals();
            LuaValue chunk = globals.load(Files.readString(file.toPath()));
            chunk.call();
        } catch (Exception e) {
            LOGGER.atSevere().withCause(e).log("Failed to load script `%s`.", file.getName());
        }
    }
}
