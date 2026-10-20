package com.stevenlagoy.presidency.core;

import org.junit.jupiter.api.Test;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaJTest {

    @Test
    public void testLuaJCallable() {
        Globals globals = JsePlatform.standardGlobals();
        LuaValue chunk = globals.load("print('Hello from LuaJ')");
        chunk.call();
    }
}
