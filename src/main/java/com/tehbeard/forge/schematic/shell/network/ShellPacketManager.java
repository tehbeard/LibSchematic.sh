package com.tehbeard.forge.schematic.shell.network;

import java.io.DataInputStream;
import java.util.logging.Logger;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.shell.LibSchematicShell;
import com.tehbeard.forge.schematic.shell.Reference;
import com.tehbeard.pluginChannel.Message;
import com.tehbeard.pluginChannel.MessageReader;
import com.tehbeard.pluginChannel.PluginChannelManager;

import com.tehbeard.pluginChannel.netty.PacketHandler;
import com.tehbeard.pluginChannel.netty.SchemPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

/**
 * Class Name - ShellPacketManager
 * Package - com.tehbeard.forge.schematic.shell.network
 * Desc of Class - Manage what exactly happens when a packet is received
 *                 and pass it on accordingly
 * Author(s) - J. Holt (@James), M. D. Ball (@M4Numbers)
 */
public class ShellPacketManager extends PluginChannelManager {

    /**
     * Initialise the class whilst registering a few things so we can get
     * down to business when the time comes
     */
    public ShellPacketManager() {

        //Fetch our logger for debug/information purposes
        super(Logger.getLogger("minecraft"));

        //Now add in a listener so that when we get a message, it can be dealt
        // with by the appropriate sources
        addSubchannelListener(Reference.SUB_CHANNEL, new MessageReader() {

            @Override
            public void onMessage(String channel, Object player, Message message) {
                try{

                    //Strip our player from the object and set up some streams
                    EntityPlayerMP entityPlayer = (EntityPlayerMP)player;
                    DataInputStream stream = message.getDataStream();

                    //Is this the first or the second position?
                    boolean min = stream.readBoolean();

                    //Create a vector from the stream positions
                    SchVector vector = new SchVector(
                            stream.readInt(),
                            stream.readInt(),
                            stream.readInt()
                    );

                    //And let's still make sure that the player is holding a set
                    // square
                    ItemStack currentItem = entityPlayer.inventory.getCurrentItem();

                    //Because, if he isn't... we'll cry :(
                    if(currentItem == null){return;}
                    if(!currentItem.isItemEqual(new ItemStack(LibSchematicShell.setSquareItem))){return;}

                    //Now let's classify the point into its position
                    if(min){
                        LibSchematicShell.setSquareItem.setPos1(currentItem, vector);
                    }
                    else
                    {
                        LibSchematicShell.setSquareItem.setPos2(currentItem, vector);
                    }

                    //Now that's done, let's send a pointer back to the client telling them
                    // that they're free to go and render things
                    PacketHandler.network.sendTo(new SchemPacket.SchemMessage(new byte[0]), entityPlayer);

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

}