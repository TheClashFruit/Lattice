package me.theclashfruit.lattice.util;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link TwoArgFunction} with event functionalities.
 */
public class TwoArgFunctionWithEvents extends TwoArgFunction {
    private static final Map<String, List<LuaValue>> events = new HashMap<>();
    private static final Map<String, Handler> handlers = new HashMap<>();

    protected String packageName;

    /**
     * A function to add an event that can be handled.
     *
     * @param name The events name, ex. `player_join`.
     * @param handler A lambda function to convert stuff to {@link LuaValue}.
     */
    protected void addEvent(String name, Handler handler) {
        events.put(name, new ArrayList<>());
        handlers.put(name, handler);
    }

    public static void callEvents(String eventName, Object... args) {
        if (!events.containsKey(eventName))
            return;

        List<LuaValue> functions = events.get(eventName);
        Handler handler = handlers.get(eventName);

        for (LuaValue function : functions) {
            handler.run(function, args);
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

        env.set(packageName, eventTable);
        env.get("package").get("loaded").set(packageName, eventTable);

        return eventTable;
    }
}
