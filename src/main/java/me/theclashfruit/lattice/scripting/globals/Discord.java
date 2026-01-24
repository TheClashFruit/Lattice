package me.theclashfruit.lattice.scripting.globals;

import me.theclashfruit.lattice.util.TwoArgFunctionWithEvents;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
        this.addEvent("message_received", (fn, args) -> {
            var event = (MessageReceivedEvent) args[0];
            var msg = event.getMessage();

            fn.call(LuaValue.valueOf(msg.getContentRaw()));
        });
        this.addEvent("slash_command_interaction", (fn, args) -> {
            var event = (SlashCommandInteractionEvent) args[0];
            var name = event.getName();

            fn.call(LuaValue.valueOf(name));
        });
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
