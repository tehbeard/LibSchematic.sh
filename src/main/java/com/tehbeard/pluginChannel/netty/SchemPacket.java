package com.tehbeard.pluginChannel.netty;

import com.tehbeard.forge.schematic.SchematicDataRegistry;
import com.tehbeard.forge.schematic.shell.network.ShellPacketManager;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * Class Name - SchemPacket
 * Package - com.tehbeard.pluginChannel.netty
 * Desc of Class - ...
 * Author(s) - M. D. Ball
 * Last Mod: 10/01/2015
 */
public class SchemPacket implements IMessageHandler<SchemPacket.SchemMessage, IMessage> {

    @Override
    public IMessage onMessage(SchemMessage message, MessageContext ctx) {
        try {
            SchematicDataRegistry.logger().debug(String.format(
                    "We've got a message! It has a length of %d, and came from the %s, sent by %s",
                    message.len, ctx.side, ctx.getServerHandler().playerEntity.getDisplayName()
            ));
            if (ctx.side.isClient()) {
                //The client needs to pull its finger out
                if (message.len != 0) return null;
                //If we get this, we need to do some rendering
            } else {
                //The server needs to do its stuffs
                ShellPacketManager spm = new ShellPacketManager();
                spm.onPluginMessageReceived(
                        ctx.getServerHandler().netManager.channel().toString(),
                        ctx.getServerHandler().playerEntity, message.tag
                );
            }
            return null;
        } catch (Exception e) {
            SchematicDataRegistry.logger().error("Apparently, the input stream wants to be alone for a little while");
            return null;
        }
    }

    public static class SchemMessage implements IMessage {

        private byte[] tag;
        private int len;

        public SchemMessage() {}

        public SchemMessage(byte[] tagData) {
            tag = tagData;
            len = tagData.length;
            SchematicDataRegistry.logger().debug(String.format(
                    "New packet of length: %d", len
            ));
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            len = buf.readInt();
            if (tag==null)
                tag = new byte[len];
            buf.readBytes(tag,0,len);
            SchematicDataRegistry.logger().info(Arrays.toString(tag));
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(len);
            buf.writeBytes(tag);
        }
    }
}
