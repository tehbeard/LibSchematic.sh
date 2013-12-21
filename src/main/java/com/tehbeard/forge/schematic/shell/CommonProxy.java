package com.tehbeard.forge.schematic.shell;

import com.tehbeard.forge.schematic.shell.commands.LoadCommand;
import com.tehbeard.forge.schematic.shell.commands.SaveCommand;

import net.minecraft.command.CommandHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonProxy {
    
    public void registerItems(){
        LanguageRegistry.addName(LibSchematicShell.setSquareItem, "SetSquare");
        LanguageRegistry.instance().addStringLocalization("itemGroup.tabLibSchematic", "LibSchematic.sh");
        GameRegistry.registerItem(LibSchematicShell.setSquareItem, "SetSquare");
        
        LibSchematicShell.setSquareItem.setCreativeTab(LibSchematicShell.tabLibSch);
        
    }
    
    public void registerGUI(){
        
    }
    
    public void registerCommands(CommandHandler ch){
        ch.registerCommand(new SaveCommand());
        ch.registerCommand(new LoadCommand());
    }

}
