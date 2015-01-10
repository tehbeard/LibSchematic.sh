package com.tehbeard.forge.schematic.shell.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.tehbeard.forge.schematic.Blueprint;
import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.SchematicFile;
import com.tehbeard.forge.schematic.extensions.id.IdTranslateExtension;
import com.tehbeard.forge.schematic.extensions.WorldEditVectorExtension;
import com.tehbeard.forge.schematic.shell.LibSchematicShell;
import com.tehbeard.forge.schematic.shell.commands.BCommand.PermLevel;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

@BCommand(command="savesch",level=PermLevel.none,usage="/savesch filename")
public class SaveCommand extends PlayerCommand {

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        EntityPlayerMP player = getCommandSenderAsPlayer(icommandsender);
        ItemStack currentItem = player.inventory.getCurrentItem();
        if(currentItem == null){return;}
        if(currentItem.isItemEqual(new ItemStack(LibSchematicShell.setSquareItem))){
            SchVector p1 = LibSchematicShell.setSquareItem.getPos1(currentItem);
            SchVector p2 = LibSchematicShell.setSquareItem.getPos2(currentItem);

            System.out.println("NBT tag says: " + p1 + " :: " + p2);

            Blueprint blueprint = new Blueprint(player.worldObj, p1, p2);
            
             

            ArgumentPack arguments = new ArgumentPack(new String[]{"notile","noentity","rel"}, null, astring);
            
            blueprint.setIgnoreTileEntityData(arguments.getFlag("notile"));
            blueprint.setIgnoreEntityData(arguments.getFlag("noentity"));
            
            SchematicFile schematic = blueprint.createSchematicFile();
                    
            //Save Relative offset
            if(arguments.getFlag("rel")){
                WorldEditVectorExtension vectors = new WorldEditVectorExtension();
                SchVector playerPos = new SchVector((int)Math.floor(player.posX),(int) Math.floor(player.posY),(int) Math.floor(player.posZ));
                SchVector minPos = SchVector.min(p1, p2);
                vectors.setOffset(minPos.sub(playerPos));
                schematic.addExtension(vectors);
            }

            //FMLCommonHandler.instance().getMinecraftServerInstance().getFile(par1Str)
            try {
                new File("./sch/").mkdir();
                File f = new File("./sch/" + arguments.get(0) + ".schematic");
                
                
                IdTranslateExtension translate = new IdTranslateExtension();
                translate.redoCache();
                schematic.addExtension(translate);
                schematic.saveSchematic(new FileOutputStream(f));



                player.addChatMessage(new ChatComponentText(
                        "Saved to " + arguments.get(0) + ".schematic"
                ));
                return;
            } catch (FileNotFoundException e) {
                player.addChatMessage(new ChatComponentText(
                        "Could not save to that file"
                ));
                e.printStackTrace();
            } catch (IOException e) {
                player.addChatMessage(new ChatComponentText(
                        "An error occured writing to that file"
                ));
                e.printStackTrace();
            }
        }

        player.addChatMessage(new ChatComponentText(
                "No SetSquare in hand!"
        ));
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
