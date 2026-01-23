package me.theclashfruit.lattice.scripting.globals;

import com.hypixel.hytale.server.core.entity.entities.Player;
import me.theclashfruit.lattice.util.Handler;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Events extends TwoArgFunction {
    private static final Map<String, List<LuaValue>> events = new HashMap<>();
    private static final Map<String, Handler> handlers = new HashMap<>();

    public Events() {
        events.put("player_ready", new ArrayList<>());
        handlers.put("player_ready", (fn, arg1) -> {
            Player player = (Player) arg1;
            LuaTable params = new LuaTable();
            params.set("display_name", player.getDisplayName());
            fn.call(params);
        });
    }

    public static void callEvents(String eventName, Object arg1) {
        if (!events.containsKey(eventName))
            return;

        List<LuaValue> functions = events.get(eventName);
        Handler handler = handlers.get(eventName);

        for (LuaValue function : functions) {
            handler.run(function, arg1);
        }
    }

    @Override
    public LuaValue call(LuaValue value, LuaValue env) {
        Globals globals = env.checkglobals();

        LuaTable eventTable = new LuaTable();

        eventTable.set("on", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue name, LuaValue fn) {
                String n = name.checkjstring();

                if (!events.containsKey(n))
                    return NONE;

                events.get(n).add(fn);
                int len = events.get(n).size() - 1;

                return LuaValue.valueOf(len);
            }
        });

        env.set("events", eventTable);
        env.get("package").get("loaded").set("events", eventTable);

        return eventTable;
    }
}
