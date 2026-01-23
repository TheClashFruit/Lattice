package me.theclashfruit.lattice.scripting.globals;

import com.hypixel.hytale.server.core.entity.entities.Player;
import me.theclashfruit.lattice.util.TwoArgFunctionWithEvents;
import org.luaj.vm2.LuaTable;

public class Events extends TwoArgFunctionWithEvents {
    public Events() {
        this.packageName = "events";

        this.addEvent("player_ready", (fn, args) -> {
            Player player = (Player) args[0];
            LuaTable params = new LuaTable();
            params.set("display_name", player.getDisplayName());
            fn.call(params);
        });

        this.addEvent("shutdown", (fn, _) -> fn.call());
    }
}
