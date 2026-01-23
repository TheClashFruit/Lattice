package me.theclashfruit.lattice.util;

import org.luaj.vm2.LuaValue;

public interface Handler {
    void run(LuaValue fn, Object arg1);
}
