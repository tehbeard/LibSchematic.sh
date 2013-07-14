package com.tehbeard.forge.schematic.shell.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public abstract class PlayerCommand extends CommandBase {
    
    private final String command;
    private final String usage;
    private final int lvl;
    
    public PlayerCommand(){
        BCommand b = this.getClass().getAnnotation(BCommand.class);
        if(b==null){throw new IllegalStateException("No @BCommand found");}
        
        command = b.command();
        usage   = b.usage();
        lvl     = b.level().lvl;
    }

    @Override
    public String getCommandName() {
        return command;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return usage;
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return lvl;
    }

    @Override
    public abstract void processCommand(ICommandSender icommandsender, String[] astring);

}
