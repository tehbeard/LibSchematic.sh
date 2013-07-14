package com.tehbeard.pluginChannel;


public interface MessageReader {
	
	public void onMessage(String channel, Object player,Message message);

}
