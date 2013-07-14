package com.tehbeard.pluginChannel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PluginChannelManager {


	/**
	 * Holds list of partially processed messages
	 */
	private Map<String,Map<Integer,Message>> messages = new HashMap<String, Map<Integer,Message>>();

	/**
	 * Holds oneshot reply listeners
	 */
	private Map<Integer,MessageReader> onReply = new HashMap<Integer, MessageReader>();

	
	/**
	 * Sub channel listeners
	 */
	private Map<String,MessageReader> listeners = new HashMap<String, MessageReader>();



    //private Logger logger;
    
    public PluginChannelManager(Logger logger){
        //this.logger = logger;
    }

	/**
	 * Add a subchannel listener.
	 * Sub channel listeners respond to all messages on a sub channel
	 * Sub channels 
	 * @param subchannel
	 * @param reader
	 */
	public void addSubchannelListener(String subchannel,MessageReader reader){
		listeners.put(subchannel, reader);
	}


	public void onPluginMessageReceived(String channel, Object player, byte[] data) {
		try {
		    System.out.println("Processing message");
			//Get the message
			MessagePart part = new MessagePart(data);

			//Create subchannel box if needed
			if(!messages.containsKey(part.getSubchannel())){
				messages.put(part.getSubchannel(),new HashMap<Integer, Message>());
				System.out.println("Creating new sub channel box");
			}

			//Create new message or add to existing message
			if(!messages.get(part.getSubchannel()).containsKey(part.getMsgId())){
				messages.get(part.getSubchannel()).put(part.getMsgId(), new Message(part));
				System.out.println("Creating new  message box");
			}
			else
			{
				messages.get(part.getSubchannel()).get(part.getMsgId()).add(part);
				System.out.println("Adding to exisiting message");
			}


			//check if message is done
			Message message = messages.get(part.getSubchannel()).get(part.getMsgId());

			//process message
			if(message.isDone()){
			    System.out.println("Message complete, processing");
				messages.get(part.getSubchannel()).remove(part.getMsgId());
				MessageReader reader = listeners.get(message.getSubchannel());
				if(reader!=null){
				    System.out.println("Passing to handler");
					reader.onMessage(channel, player,message);
					
				}
				else
				{
				    System.out.println("No handler found!");
					//logger.info("Message for subchannel "  + message.getSubchannel() + " dropped due to lack of listener.");
				}

			}
			else
			{
			    System.out.println("Message incomplete");
			}

		} catch (IOException e) {
		    //logger.severe("Error occured unpacking message");
			e.printStackTrace();
		}

	}

}
