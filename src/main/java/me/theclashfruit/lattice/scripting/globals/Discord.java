package me.theclashfruit.lattice.scripting.globals;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import static me.theclashfruit.lattice.LatticePlugin.LOGGER;

public class Discord extends TwoArgFunction {
    private static final String[] functions = {
            "set_activity"
    };

    @Override
    public LuaValue call(LuaValue value, LuaValue env) {
        Globals globals = env.checkglobals();

        LuaTable discord = new LuaTable();
        for (String function : functions) {
            discord.set(function, new DiscordFunction(function));
        }

        env.set("discord", discord);

        return discord;
    }

    static class DiscordFunction extends VarArgFunction {
        public DiscordFunction(String name) {
            this.name = name;
        }
    }

    public Varargs invoke(Varargs args) {
        LOGGER.atInfo().log("get called %s", name);

        return NONE;
    }
}
