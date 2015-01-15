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

/**
 * Class Name - SaveCommand
 * Package - com.tehbeard.forge.schematic.shell.commands
 * Desc of Class - This is the command class for saving a schematic
 *                 into the 'sch/' folder in .minecraft
 * Author(s) - J. Holt (@James), M. D. Ball (@M4Numbers)
 */
@BCommand(command="savesch",level=PermLevel.none,usage="/savesch filename")
public class SaveCommand extends PlayerCommand {

    /**
     * Process the save command, saving the selected area into a
     * schematic file along with a few details to keep it company
     *
     * @param iCommandSender This is the information about the command sender
     * @param aString These are any additional arguments provided
     */
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] aString) {

        //Let's strip the player and their current item
        EntityPlayerMP player = getCommandSenderAsPlayer(iCommandSender);
        ItemStack currentItem = player.inventory.getCurrentItem();

        //Are they holding nothing?
        if(currentItem == null){return;}

        //Since they're not, let's ask if they're holding a set square
        // as that set square holds the information about the schematic
        if(currentItem.isItemEqual(new ItemStack(LibSchematicShell.setSquareItem))){

            //Let's get the positions stored in the set square
            SchVector p1 = LibSchematicShell.setSquareItem.getPos1(currentItem);
            SchVector p2 = LibSchematicShell.setSquareItem.getPos2(currentItem);

            //Show the nice people
            System.out.println("NBT tag says: " + p1 + " :: " + p2);

            //And boot a blueprint from the data
            Blueprint blueprint = new Blueprint(player.worldObj, p1, p2);

            //Now let's go over the additional arguments/flags that could be here
            ArgumentPack arguments = new ArgumentPack(new String[]{"notile","noentity","rel"}, null, aString);
            
            blueprint.setIgnoreTileEntityData(arguments.getFlag("notile"));
            blueprint.setIgnoreEntityData(arguments.getFlag("noentity"));

            //Now we've done for now, let's move the blueprint into a schematic
            SchematicFile schematic = blueprint.createSchematicFile();

            //The one flag left is if we have a relative offset to check on
            if(arguments.getFlag("rel")){

                //Since we do, let's sort that out
                WorldEditVectorExtension vectors = new WorldEditVectorExtension();
                SchVector playerPos = new SchVector((int)Math.floor(player.posX),(int) Math.floor(player.posY),(int) Math.floor(player.posZ));
                SchVector minPos = SchVector.min(p1, p2);
                vectors.setOffset(minPos.sub(playerPos));

                //And add it on as an extension at the end
                schematic.addExtension(vectors);

            }

            try {

                //Okay, now let's check to see if we have to create the directory or not
                new File("./sch/").mkdir();

                //Now let's make the file with a provided argument
                File f = new File("./sch/" + arguments.get(0) + ".schematic");
                
                //Now we need to store our current IDs into the translator so that the
                // eventual user can cross-reference them against their own blocks
                IdTranslateExtension translate = new IdTranslateExtension();
                translate.redoCache();
                schematic.addExtension(translate);

                //Once everything here is done, let's finally save the file
                schematic.saveSchematic(new FileOutputStream(f));

                //And tell the player
                player.addChatMessage(new ChatComponentText(
                        "Saved to " + arguments.get(0) + ".schematic"
                ));

                return;

            } catch (FileNotFoundException e) {

                //... If this goes off, I don't know what to say
                player.addChatMessage(new ChatComponentText(
                        "Could not save to that file"
                ));
                e.printStackTrace();

            } catch (IOException e) {

                //This is for Read-Only files that can't be deleted/overwritten/something
                player.addChatMessage(new ChatComponentText(
                        "An error occured writing to that file"
                ));
                e.printStackTrace();

            }

        }

        //Otherwise, we don't have a set square and, therefore, have no
        // information linked to it to act on
        player.addChatMessage(new ChatComponentText(
                "No SetSquare in hand!"
        ));

    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
