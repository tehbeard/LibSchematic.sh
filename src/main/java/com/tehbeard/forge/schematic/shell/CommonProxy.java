package com.tehbeard.forge.schematic.shell;

import com.tehbeard.forge.schematic.shell.commands.LoadCommand;
import com.tehbeard.forge.schematic.shell.commands.SaveCommand;

import net.minecraft.command.CommandHandler;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Class Name - CommonProxy
 * Package - com.tehbeard.forge.schematic.shell
 * Desc of Class - This is the proxy where all the registration is going
 *                 to happen, this includes items and commands
 * Author(s) - J. Holt (@James), M. D. Ball (@M4Numbers)
 */
public class CommonProxy {

    /**
     * Register our item
     */
    public void registerItems(){

        GameRegistry.registerItem(LibSchematicShell.setSquareItem, "SetSquare");

    }

    /**
     * Register our GUI stuff (Might be done in the ClientProxy)
     */
    public void registerGUI(){
        
    }

    /**
     * Register the commands we have on offer
     *
     * @param ch The handler to which we're going to hand our commands
     */
    public void registerCommands(CommandHandler ch){
        ch.registerCommand(new SaveCommand());
        ch.registerCommand(new LoadCommand());
    }

}
