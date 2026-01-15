package me.theclashfruit.lattice.util.script;

import java.util.Arrays;

import static me.theclashfruit.lattice.LatticePlugin.LOGGER;

public class JavaScriptConsole {
    public void debug(String... args) {
        LOGGER.atFine().log(joinMessage(args));
    }

    public void info(String... args) {
        LOGGER.atInfo().log(joinMessage(args));
    }

    public void log(String... args) {
        this.info(args.clone());
    }

    public void trace(String... args) {
        LOGGER.atSevere().log(joinMessage(args));
    }

    public void error(String... args) {
        LOGGER.atSevere().log(joinMessage(args));
    }

    public void warn(String... args) {
        LOGGER.atWarning().log(joinMessage(args));
    }

    private String joinMessage(String... args) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(args).forEach(a -> builder.append(a).append(" "));
        return builder.toString();
    }
}
