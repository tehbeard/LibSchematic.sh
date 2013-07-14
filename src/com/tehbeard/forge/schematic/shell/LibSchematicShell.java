package com.tehbeard.forge.schematic.shell;


import com.tehbeard.forge.schematic.shell.commands.LoadCommand;
import com.tehbeard.forge.schematic.shell.commands.SaveCommand;
import com.tehbeard.forge.schematic.shell.commands.ViewboxCommand;
import com.tehbeard.forge.schematic.shell.render.AABBRender;


import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "tehbeard.schematic.sh",name="LibSchematic.sh",version="1.00",useMetadata=true)
public class LibSchematicShell {
    
    public static final SetSquareItem setSquareItem = new SetSquareItem(30000);
    
    public static final AABBRender render = new AABBRender();
    @EventHandler
    public void init(FMLInitializationEvent event){
        LanguageRegistry.addName(setSquareItem, "SetSquare");
        GameRegistry.registerItem(setSquareItem, "SetSquare");
        
        MinecraftForge.EVENT_BUS.register(render);

    }
    
    @EventHandler
    public void preServerStart(FMLServerAboutToStartEvent event){
        CommandHandler ch = ((CommandHandler)event.getServer().getCommandManager());
        ch.registerCommand(new ViewboxCommand());
        ch.registerCommand(new SaveCommand());
        ch.registerCommand(new LoadCommand());
    }

}
