package com.tehbeard.forge.schematic.shell.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.tehbeard.forge.schematic.Blueprint;
import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicFactory;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.product.PasteToWorld;
import com.tehbeard.forge.schematic.shell.LibSchematicShell;
import com.tehbeard.forge.schematic.shell.commands.BCommand.PermLevel;
import com.tehbeard.forge.schematic.worker.VectorOffsetWorker;
import com.tehbeard.forge.schematic.worker.WEOffsetWorker;


import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;



@BCommand(command="loadsch",level=PermLevel.none,usage="/loadsch filename")
public class LoadCommand extends PlayerCommand {

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        
        EntityPlayerMP player = getCommandSenderAsPlayer(icommandsender);
        
        try {
            SchematicFile file = new SchematicFile(new File("./sch/" + astring[0] + ".schematic"));
            
            PasteToWorld paste = new PasteToWorld(player.worldObj);
            paste.setWorldVec(new SchVector((int)Math.floor(player.posX),(int) Math.floor(player.posY),(int) Math.floor(player.posZ)));
            
            new SchematicFactory().loadWorkers(new WEOffsetWorker()).loadSchematic(file).produce(paste);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
