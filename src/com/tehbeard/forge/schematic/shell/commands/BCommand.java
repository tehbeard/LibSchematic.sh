package com.tehbeard.forge.schematic.shell.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BCommand {
String command();
String usage();
PermLevel level();
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
