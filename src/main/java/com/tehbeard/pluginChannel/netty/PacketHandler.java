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

import com.tehbeard.forge.schematic.shell.Reference;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * Class Name - PacketHandler
 * Package - com.tehbeard.pluginChannel
 * Desc of Class - ...
 * Author(s) - M. D. Ball
 * Last Mod: 10/01/2015
 */
public class PacketHandler {

    public static SimpleNetworkWrapper network;

    public static void init() {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID.toUpperCase());
        registerMessage(SchemPacket.class, SchemPacket.SchemMessage.class);
    }

    private static int nxtPacketId = 0;

    private static void registerMessage(Class packet, Class message) {
        network.registerMessage(packet, message, nxtPacketId, Side.CLIENT);
        network.registerMessage(packet, message, nxtPacketId, Side.SERVER);
        ++nxtPacketId;
    }
}
