package me.theclashfruit.lattice.util;

import me.theclashfruit.lattice.util.script.JavaScriptConsole;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class ScriptingUtil {
    public ScriptingUtil(Path root) {
        try(Context cx = Context.enter()) {
            cx.setLanguageVersion(Context.VERSION_ES6);

            Scriptable scope = cx.initStandardObjects();

            JavaScriptConsole jsConsole = new JavaScriptConsole();
            scope.put("console", scope, jsConsole);

            File path = root.resolve("scripts").toFile();
            if (!path.exists())
                path.mkdirs();

            File[] files = path.listFiles((dir, name) -> name.toLowerCase().endsWith(".js"));

            assert files != null;
            Arrays.stream(files).forEach(file -> {
                try(FileReader reader = new FileReader(file)) {
                    cx.evaluateReader(scope, reader, file.getName(), 1, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
