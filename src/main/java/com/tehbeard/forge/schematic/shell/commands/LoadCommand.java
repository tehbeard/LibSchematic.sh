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

/**
 * Class Name - LoadCommand
 * Package - com.tehbeard.forge.schematic.shell.commands
 * Desc of Class - This is the command class for loading in a schematic in
 *                 the 'sch/' folder inside .minecraft
 * Author(s) - J. Holt (@James)
 */
@BCommand(command="loadsch",level=PermLevel.none,usage="/loadsch filename")
public class LoadCommand extends PlayerCommand {

    /**
     * This is called whenever our command is. We must do what we can to
     * see that its wishes are carried out
     *
     * @param iCommandSender This is our interface to whoever sent the command
     * @param aString These are any additional arguments to the command
     */
    @Override
    public void processCommand(ICommandSender iCommandSender, String[] aString) {

        //Fetch the player from the interface
        EntityPlayerMP player = getCommandSenderAsPlayer(iCommandSender);
        
        try {

            //And start up the two parts of this thing: The piece we're plugging,
            // and the world that we're plugging it into
            SchematicFile file = new SchematicFile(new File("./sch/" + aString[0] + ".schematic"));
            PasteToWorld paste = new PasteToWorld(player.worldObj);

            //Now give ourselves the central point as to where the schematic is going
            // to be pasted into the world, then create the factory that's going to
            // do most of the heavy lifting
            paste.setWorldVec(new SchVector((int)Math.floor(player.posX),(int) Math.floor(player.posY),(int) Math.floor(player.posZ)));
            SchematicFactory factory = new SchematicFactory().loadWorkers(new WEOffsetWorker(),new IdTranslateWorker());

            //Let's check whether we've been told to rotate anything
            ArgumentPack arguments = new ArgumentPack(new String[]{}, new String[]{"rotate"}, aString);
            
            if(arguments.getOption("rotate")!=null){
                paste.setRotations(Integer.parseInt(arguments.getOption("rotate")));
            }

            //Now we've prepared properly, place the schematic into the world
            factory.loadSchematic(file).produce(paste);

        } catch (IOException e) {
            //Since we couldn't read the file... something is obviously wrong
            player.addChatMessage(new ChatComponentText("Could not read file"));
            e.printStackTrace();
        }

    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

}
