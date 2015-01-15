package com.tehbeard.pluginChannel;

/**
 * Class Name - MessageReader
 * Package - com.tehbeard.pluginChannel
 * Desc of Class - This is the interface which deals which any classes wishing
 *                 to call themselves readers of messages
 * Author(s) - J. Holt (@James), M. D. Ball (@M4Numbers)
 */
public interface MessageReader {

	/**
	 * Whenever a message is received, this is what will be carried out
	 *
	 * @param channel This is the channel the message was received upon
	 * @param player This is the player from whom the message came from
	 * @param message This is the message itself
	 */
	public void onMessage(String channel, Object player,Message message);

}
