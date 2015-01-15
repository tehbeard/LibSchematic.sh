package com.tehbeard.pluginChannel.netty;

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
