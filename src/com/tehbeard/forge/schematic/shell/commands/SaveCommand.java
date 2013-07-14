package com.tehbeard.forge.schematic.shell.commands;

import com.tehbeard.forge.schematic.shell.commands.BCommand.PermLevel;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;


@BCommand(command="savesch",level=PermLevel.none,usage="/savesch filename")
public class SaveCommand extends PlayerCommand {

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        
        EntityPlayerMP player = getCommandSenderAsPlayer(icommandsender);
        
    }

}
