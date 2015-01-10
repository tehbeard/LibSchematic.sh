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

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import io.netty.buffer.ByteBuf;

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
        if (ctx.side.isClient()) {
            //Do stuff when receiving a message...
        }
        return null;
    }

    public static class SchemMessage implements IMessage {

        public byte[] tag;

        public SchemMessage() {}

        public SchemMessage(byte[] tagData) {
            tag = tagData;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            buf.readBytes(tag);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBytes(tag);
        }
    }
}
