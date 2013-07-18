package com.tehbeard.forge.schematic.shell;

import com.tehbeard.forge.schematic.shell.render.AABBRender;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    private AABBRender render = new AABBRender();
    
    @Override
    public void registerGUI() {
        MinecraftForge.EVENT_BUS.register(render);
    }

}
