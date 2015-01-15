package com.tehbeard.forge.schematic.shell.commands;

import java.io.File;
import java.io.IOException;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.factory.SchematicFactory;
import com.tehbeard.forge.schematic.factory.output.PasteToWorld;
import com.tehbeard.forge.schematic.factory.worker.blocks.IdTranslateWorker;
import com.tehbeard.forge.schematic.factory.worker.worldedit.WEOffsetWorker;
import com.tehbeard.forge.schematic.shell.commands.BCommand.PermLevel;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

@BCommand(command="loadsch",level=PermLevel.none,usage="/loadsch filename")
public class LoadCommand extends PlayerCommand {

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        
        EntityPlayerMP player = getCommandSenderAsPlayer(icommandsender);
        
        try {
            SchematicDataRegistry.logger().info("Starting a new load schem...");

            SchematicFile file = new SchematicFile(new File("./sch/" + astring[0] + ".schematic"));
            PasteToWorld paste = new PasteToWorld(player.worldObj);

            SchematicDataRegistry.logger().info("Classes loaded");

            paste.setWorldVec(new SchVector((int)Math.floor(player.posX),(int) Math.floor(player.posY),(int) Math.floor(player.posZ)));
            SchematicFactory factory = new SchematicFactory().loadWorkers(new WEOffsetWorker(),new IdTranslateWorker());

            SchematicDataRegistry.logger().info("Vector and factory created");

            ArgumentPack arguments = new ArgumentPack(new String[]{}, new String[]{"rotate"}, astring);
            
            if(arguments.getOption("rotate")!=null){
                paste.setRotations(Integer.parseInt(arguments.getOption("rotate")));
            }

            SchematicDataRegistry.logger().info("Rotation inspection completed");
            
            factory.loadSchematic(file).produce(paste);

            SchematicDataRegistry.logger().info("Schematic pasted");

        } catch (IOException e) {
            player.addChatMessage(new ChatComponentText("Could not read file"));
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
