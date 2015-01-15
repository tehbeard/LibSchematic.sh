package com.tehbeard.pluginChannel.netty;
/**
 * Copyright 2015 M. D. Ball (m.d.ball2@ncl.ac.uk)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
            SchematicDataRegistry.logger().info(String.format(
                    "We've got a message! It has a length of %d, and came from the %s, sent by %s",
                    message.len, ctx.side, ctx.getServerHandler().playerEntity.getDisplayName()
            ));
            if (ctx.side.isClient()) {
                //The client needs to pull its finger out
                if (message.len != 0) return null;

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
            SchematicDataRegistry.logger().info(String.format(
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
