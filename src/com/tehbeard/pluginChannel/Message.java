package com.tehbeard.pluginChannel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


/**
 * Represents a Multi part message sent over a plugin channel
 * @author James
 *
 */
public class Message {
	
    //Protocol version, not used?
	public static final char PROTOCOL_VERSION = 1;

	//chunks of the message
	public MessagePart[] messageParts;

	//Have we got all the parts?
	private boolean isDone = false;

	//Raw data body
	private byte[] rawData;
	
	
	//options
	private char options;

	//Sub channel name
	private String subchannel = null;

	//Maximum size of a chunk of the data body
	private final static int MAX_CHUNK = 16384;
	
	private static int nextMsgId = 0;
	
	public Message(byte[] data,char options,String subchannel,int replyId) throws IOException{
		this.subchannel = subchannel;
		
		isDone = true;
		rawData = data;
		
		InputStream is = new ByteArrayInputStream(data);
		char totalMsgs = (char) Math.ceil(((double)data.length) / MAX_CHUNK);
		
		messageParts = new MessagePart[totalMsgs];
		
		int msgId = nextMsgId++;
		char orderId = 0;
		
		byte[] buffer = new byte[0];
		while(is.available() > 0){
			buffer = drainChunk(is);
			
			//generate messagePart
			MessagePart part = new MessagePart(
					PROTOCOL_VERSION, 
					msgId,
					replyId,
					orderId, 
					options, 
					subchannel, 
					buffer, 
					totalMsgs);
			System.out.println("" + (int)orderId + "/" + (int)totalMsgs);
			messageParts[orderId] = part;
			
			orderId++;
		}
		
		

	}

	public Message(MessagePart part) throws IOException{

		subchannel = part.getSubchannel();

		messageParts = new MessagePart[part.getTotalMsgs()];
		add(part);
	}

	/**
	 * Add a messagePart to this message, attempts to assemble message.
	 * @param part
	 * @throws IOException
	 */
	public void add(MessagePart part) throws IOException{
		messageParts[part.getOrderId()] = part;

		assemble();
	}

	/**
	 * Attempts to concaternate all MessageParts together
	 * @throws IOException
	 */
	private void assemble() throws IOException{
		//check we have all message parts
		for(MessagePart p : messageParts){
			if(p==null){return;}
		}

		//concaternate all the data
		ByteArrayOutputStream concatenatedRawData = new ByteArrayOutputStream();

		for(MessagePart p : messageParts){
			concatenatedRawData.write(p.getData());
		}

		//create raw access pipe
		InputStream is = new ByteArrayInputStream(concatenatedRawData.toByteArray());
		//if gzipped, wrap in GZIPInputStream
		
		options = messageParts[0].getOptions();
		isDone = true;
		rawData = drainStreamIntoByteArray(is);
	}

	private byte[] drainStreamIntoByteArray(InputStream is) throws IOException{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[MAX_CHUNK];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}
	
	private byte[] drainChunk(InputStream is) throws IOException{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[MAX_CHUNK];

		nRead = is.read(data, 0, data.length);
			buffer.write(data, 0, nRead);
		

		buffer.flush();

		return buffer.toByteArray();
	}

	public boolean isDone() {
		return isDone;
	}

	public byte[] getRawData() {
		return rawData;
	}

	public String getSubchannel() {
		return subchannel;
	}

	@Override
	public String toString() {
		return "Message [messageParts=" + Arrays.toString(messageParts)
				+ "\n, isDone="	+ isDone
				+ "\n, finalData=" + Arrays.toString(rawData)
				+ "\n, subchannel=" + subchannel + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (!Arrays.equals(rawData, other.rawData))
			return false;
	
		if (!Arrays.equals(messageParts, other.messageParts))
			return false;
		if (subchannel == null) {
			if (other.subchannel != null)
				return false;
		} else if (!subchannel.equals(other.subchannel))
			return false;
		return true;
	}
	
	
	public byte[][] getParts() throws IOException{
		byte[][] r = new byte[messageParts.length][];
		int i = 0;
		for(MessagePart part : messageParts){
			r[i++] = part.writeOutBytes();
		}
		
		return r;
	}

	public DataInputStream getDataStream(){
	    return new DataInputStream(new ByteArrayInputStream(getRawData()));
	}
	
	
	public char getOptions() {
        return options;
    }
    
    public boolean getOption(char option){
        return (options ^ option) < options;
    }
	
}
