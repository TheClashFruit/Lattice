package me.theclashfruit.lattice.scripting.globals;

import me.theclashfruit.lattice.util.TwoArgFunctionWithEvents;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import static me.theclashfruit.lattice.LatticePlugin.LOGGER;

public class Discord extends TwoArgFunctionWithEvents {
    private static final String[] functions = {
            "set_activity"
    };

    public Discord() {
        this.packageName = "discord";

        this.addEvent("ready", (fn, args) -> fn.call());
    }

    @Override
    public LuaValue call(LuaValue value, LuaValue env) {
        LuaTable discord = super.call(value, env).checktable();

        for (String function : functions) {
            discord.set(function, new DiscordFunction(function));
        }

        return discord;
    }

    static class DiscordFunction extends VarArgFunction {
        public DiscordFunction(String name) {
            this.name = name;
        }
    }

    @Override
    public Varargs invoke(Varargs args) {
        LOGGER.atInfo().log("get called %s", name);

        return NONE;
    }
}
