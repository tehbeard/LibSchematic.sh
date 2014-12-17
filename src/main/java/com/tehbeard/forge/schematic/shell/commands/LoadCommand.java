package com.tehbeard.forge.schematic.shell.commands;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.factory.SchematicFactory;
import com.tehbeard.forge.schematic.factory.output.PasteToWorld;
import com.tehbeard.forge.schematic.factory.worker.blocks.IdTranslateWorker;
import com.tehbeard.forge.schematic.factory.worker.worldedit.WEOffsetWorker;
import com.tehbeard.forge.schematic.shell.commands.BCommand.PermLevel;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import uk.co.cynicode.handlers.PluginInfoMessage;

@BCommand(command="loadsch",level=PermLevel.none,usage="/loadsch filename")
public class LoadCommand extends PlayerCommand {

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        
        EntityPlayerMP player = getCommandSenderAsPlayer(icommandsender);
        
        try {
            SchematicFile file = new SchematicFile(new File("./sch/" + astring[0] + ".schematic"));
            PasteToWorld paste = new PasteToWorld(player.worldObj);
            paste.setWorldVec(new SchVector((int)Math.floor(player.posX),(int) Math.floor(player.posY),(int) Math.floor(player.posZ)));
            SchematicFactory factory = new SchematicFactory().loadWorkers(new WEOffsetWorker(),new IdTranslateWorker());
            
            ArgumentPack arguments = new ArgumentPack(new String[]{}, new String[]{"rotate"}, astring);
            
            if(arguments.getOption("rotate")!=null){
                paste.setRotations(Integer.parseInt(arguments.getOption("rotate")));
            }
            
            
            factory.loadSchematic(file).produce(paste);
            
        } catch (IOException e) {
            player.addChatMessage("Could not read file");
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
