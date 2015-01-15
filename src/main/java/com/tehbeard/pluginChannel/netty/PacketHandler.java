package com.tehbeard.pluginChannel.netty;

import com.tehbeard.forge.schematic.shell.Reference;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * Class Name - PacketHandler
 * Package - com.tehbeard.pluginChannel.netty
 * Desc of Class - This class deals with registering the packets that
 *                 we're going to get at some point and pointing the
 *                 system towards the correct handler
 * Author(s) - M. D. Ball
 */
public class PacketHandler {

    /**
     * The network wrapper which we're sending all of our messages via
     */
    public static SimpleNetworkWrapper network;

    /**
     * Initialisation method for registering our messages and actually
     * just setting up our channel for messages to go through
     */
    public static void init() {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID.toUpperCase());
        registerMessage(SchemPacket.class, SchemPacket.SchemMessage.class);
    }

    /**
     * Packet ID business
     */
    private static int nxtPacketId = 0;

    /**
     * Register a new packet and message to go with one another through
     * both the client and the server channels
     *
     * @param packet This is the packet class
     * @param message This is the message class
     */
    private static void registerMessage(Class packet, Class message) {
        network.registerMessage(packet, message, nxtPacketId, Side.CLIENT);
        network.registerMessage(packet, message, nxtPacketId, Side.SERVER);
        ++nxtPacketId;
    }

}
