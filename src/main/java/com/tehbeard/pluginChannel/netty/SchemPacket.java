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
 * Desc of Class - This class holds both the message and its packet
 *                 wrapper that are going to be sent over the network
 *                 at some point in the future
 * Author(s) - M. D. Ball
 */
public class SchemPacket implements IMessageHandler<SchemPacket.SchemMessage, IMessage> {

    /**
     * Whenever we receive a message through this channel, we're prodded towards
     * this method, where we are going to solve things <.< >.>
     *
     * @param message This is the message that was sent through the network
     * @param ctx This is the context that came along with it
     * @return Null
     */
    @Override
    public IMessage onMessage(SchemMessage message, MessageContext ctx) {
        try {

            //Let's put out a bit of debug about the message
            SchematicDataRegistry.logger().debug(String.format(
                    "We've got a message! It has a length of %d, and came from the %s, sent by %s",
                    message.len, ctx.side, ctx.getServerHandler().playerEntity.getDisplayName()
            ));

            //Now let's see which side the message came from
            if (ctx.side.isClient()) {

                //The client needs to pull its finger out
                if (message.len != 0) return null;
                //Note: If we get this, we need to do some rendering

            } else {

                //The server needs to do its stuffs
                //These stuffs include delegating
                ShellPacketManager spm = new ShellPacketManager();

                //Now, because it's easier to do this and hijack a listener... we're
                // doing this
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

    /**
     * Now this, on the other hand, is the message that we're sending within the packet,
     * holding only an array of bytes to be re-assembled into a
     * {@link com.tehbeard.pluginChannel.Message}, along with its length
     */
    public static class SchemMessage implements IMessage {

        /**
         * The byte array and its length
         */
        private byte[] tag;
        private int len;

        /**
         * The blank constructor as required by Forge
         */
        public SchemMessage() {}

        /**
         * When we need to assemble a message, we need the data to put into
         * the message
         *
         * @param tagData This is the byte array of data that is to be put into
         *                the message, originating from a Message
         */
        public SchemMessage(byte[] tagData) {
            tag = tagData;
            len = tagData.length;
            SchematicDataRegistry.logger().debug(String.format(
                    "New packet of length: %d", len
            ));
        }

        /**
         * Okay, whenever we receive a packet, we need to unwind it
         *
         * @param buf This is the byte buffer we're reading from
         */
        @Override
        public void fromBytes(ByteBuf buf) {
            len = buf.readInt();

            if (tag==null)
                //Since the byte array here is going to be null at first,
                // we need to initialise it to the length designated above
                tag = new byte[len];

            //Then we read exactly the number of bytes from the buffer
            buf.readBytes(tag,0,len);

            SchematicDataRegistry.logger().debug(Arrays.toString(tag));
        }

        /**
         * On the other hand, sometimes we need to send packets and write
         * the details to a byte buffer
         *
         * @param buf The byte buffer we're writing to
         */
        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(len);
            buf.writeBytes(tag);
        }
    }
}
