package com.tehbeard.forge.schematic.shell;

import com.tehbeard.forge.schematic.shell.render.AABBRender;

import net.minecraftforge.common.MinecraftForge;

/**
 * Class Name - ClientProxy
 * Package - com.tehbeard.forge.schematic.shell
 * Desc of Class - This is the extension on the proxy, just for the client
 *                 since only the client needs to render things
 * Author(s) - J. Holt (@James)
 */
public class ClientProxy extends CommonProxy {

    /**
     * Our rendering environment
     */
    private AABBRender render = new AABBRender();

    /**
     * Register our rendering engine with the big boys
     */
    @Override
    public void registerGUI() {
        MinecraftForge.EVENT_BUS.register(render);
    }

}
