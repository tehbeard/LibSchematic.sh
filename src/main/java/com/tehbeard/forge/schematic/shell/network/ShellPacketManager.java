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


public class ShellPacketManager extends PluginChannelManager {

    public ShellPacketManager() {
        super(Logger.getLogger("minecraft"));

        addSubchannelListener(Reference.SUB_CHANNEL, new MessageReader() {

            @Override
            public void onMessage(String channel, Object player, Message message) {
                try{

                    EntityPlayerMP entityPlayer = (EntityPlayerMP)player;
                    DataInputStream stream = message.getDataStream();
                    boolean min = stream.readBoolean();

                    SchVector vector = new SchVector(
                            stream.readInt(),
                            stream.readInt(),
                            stream.readInt()
                            );
                    ItemStack currentItem = entityPlayer.inventory.getCurrentItem();
                    if(currentItem == null){return;}
                    if(!currentItem.isItemEqual(new ItemStack(LibSchematicShell.setSquareItem))){return;}

                    if(min){
                        LibSchematicShell.setSquareItem.setPos1(currentItem, vector);
                    }
                    else
                    {
                        LibSchematicShell.setSquareItem.setPos2(currentItem, vector);
                    }

                    PacketHandler.network.sendTo(new SchemPacket.SchemMessage(new byte[0]), entityPlayer);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}