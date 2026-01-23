package me.theclashfruit.lattice.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaHandler {
    private static Globals globals;

    public LuaHandler() {
        globals = JsePlatform.standardGlobals();
    }
}
