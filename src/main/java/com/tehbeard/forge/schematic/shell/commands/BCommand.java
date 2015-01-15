package com.tehbeard.forge.schematic.shell.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class Name - BCommand
 * Package - com.tehbeard.forge.schematic.shell.commands
 * Desc of Class - This annotation is here to quickly pick out the details
 *                 about commands, this includes their names, usages, and
 *                 the level required to operate them
 * Author(s) - J. Holt (@James)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BCommand {

    String command();
    String usage();
    PermLevel level();

    /**
     * These are all the possible operation levels for a command
     */
    public enum PermLevel{
        none(0),
        op(2),
        block(4);

        public final int lvl;

        PermLevel(int lvl){
            this.lvl = lvl;
        }

    }

}
