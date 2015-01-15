package com.tehbeard.forge.schematic.shell.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

/**
 * Class Name - PlayerCommand
 * Package - com.tehbeard.forge.schematic.shell.commands
 * Desc of Class - This class sorts out the name and usage for
 *                 each of our commands (or rather, it reads it
 *                 from the annotations provided)
 * Author(s) - J. Holt (@James)
 */
public abstract class PlayerCommand extends CommandBase {

    /**
     * These are the three key parts to the commands, the
     * command word itself, how it is used, and the level
     * required to use the command
     */
    private final String command;
    private final String usage;
    private final int lvl;

    /**
     * We're going up the chain here, so this class grabs info
     * from the child class that is super-ing us
     */
    public PlayerCommand(){
        BCommand b = this.getClass().getAnnotation(BCommand.class);
        if(b==null){throw new IllegalStateException("No @BCommand found");}
        
        command = b.command();
        usage   = b.usage();
        lvl     = b.level().lvl;
    }

    /**
     * @return The name of the command
     */
    @Override
    public String getCommandName() {
        return command;
    }

    /**
     * @param iCommandSender Who wants to know?
     * @return How this command is used
     */
    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return usage;
    }

    /**
     * @return The permission level required to use this command
     */
    @Override
    public int getRequiredPermissionLevel() {
        return lvl;
    }

    /**
     * This method is ready to be filled with details on how to process
     * the command and what it actually does when it's activated
     *
     * @param iCommandSender The sender of the command
     * @param aString Any additional arguments that were sent
     */
    @Override
    public abstract void processCommand(ICommandSender iCommandSender, String[] aString);

}
