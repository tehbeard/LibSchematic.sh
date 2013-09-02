package com.tehbeard.pluginChannel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PluginChannelManager {


	/**
	 * Holds list of partially processed messages
	 * subchannel -> message part id -> Message body
	 */
	private Map<String,Map<Integer,Message>> messages = new HashMap<String, Map<Integer,Message>>();

	/**
	 * Sub channel listeners
	 */
	private Map<String,MessageReader> listeners = new HashMap<String, MessageReader>();


	
	private boolean strictMode;
	
	private final Logger logger;
	
	public PluginChannelManager(Logger logger){
	    this(true,logger);
	}
    
    public PluginChannelManager(boolean strictMode,Logger logger){
        this.strictMode = strictMode;
        this.logger = logger;
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
		    logger.fine("Processing message");
			//Get the message
			MessagePart part = new MessagePart(data);

			//Create subchannel box if needed
			if(!messages.containsKey(part.getSubchannel())){
			    if(strictMode && !listeners.containsKey(part.getSubchannel())){
			        return;
			    }
				messages.put(part.getSubchannel(),new HashMap<Integer, Message>());
				logger.fine("Creating new subchannel [" + part.getSubchannel() + "]");
			}

			//Create new message or add to existing message
			if(!messages.get(part.getSubchannel()).containsKey(part.getMsgId())){
				messages.get(part.getSubchannel()).put(part.getMsgId(), new Message(part));
				logger.fine("New message, creating container for msg " + part.getMsgId());
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
			    
				messages.get(part.getSubchannel()).remove(part.getMsgId());
				MessageReader reader = listeners.get(message.getSubchannel());
				if(reader!=null){
				    
					reader.onMessage(channel, player,message);
					
				}
				else
				{
				    
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
