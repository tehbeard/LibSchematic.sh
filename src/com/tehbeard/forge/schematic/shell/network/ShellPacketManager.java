package com.tehbeard.forge.schematic.shell.network;

import java.io.DataInputStream;
import java.util.logging.Logger;

import com.tehbeard.forge.schematic.SchVector;
import com.tehbeard.forge.schematic.shell.LibSchematicShell;
import com.tehbeard.forge.schematic.shell.Reference;
import com.tehbeard.pluginChannel.Message;
import com.tehbeard.pluginChannel.MessageReader;
import com.tehbeard.pluginChannel.PluginChannelManager;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ShellPacketManager extends PluginChannelManager implements IPacketHandler {

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
                    if(currentItem.itemID != LibSchematicShell.setSquareItem.itemID){return;}

                    if(min){
                        LibSchematicShell.setSquareItem.setPos1(currentItem, vector);
                    }
                    else
                    {
                        LibSchematicShell.setSquareItem.setPos2(currentItem, vector);
                    }


                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPacketData(INetworkManager manager,
            Packet250CustomPayload packet, Player player) {
        onPluginMessageReceived(packet.channel, player, packet.data);

    }

}
