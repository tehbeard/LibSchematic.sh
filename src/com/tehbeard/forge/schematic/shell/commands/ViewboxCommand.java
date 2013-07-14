package com.tehbeard.forge.schematic.shell.commands;

import com.tehbeard.forge.schematic.shell.LibSchematicShell;
import com.tehbeard.forge.schematic.shell.commands.BCommand.PermLevel;

import net.minecraft.command.ICommandSender;


@BCommand(command="viewbox",level=PermLevel.none,usage="/viewbox")
public class ViewboxCommand extends PlayerCommand {

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        LibSchematicShell.render.active = !LibSchematicShell.render.active;
        
        getCommandSenderAsPlayer(icommandsender).addChatMessage("Viewbox is now " + (LibSchematicShell.render.active ? "active" : "inactive"));
        
    }

}
