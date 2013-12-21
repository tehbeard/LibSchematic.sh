package com.tehbeard.pluginChannel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Represents a single 32kb transmission over a plugin message channel
 * @author James
 *
 */
public class MessagePart {
	
	

	private char protocolVersion;
	private int msgId;
	private int replyId;
	private char orderId;
	private char options;
	private String subchannel;
	private byte[] data;


	private char totalMsgs;
	
	
	
	public MessagePart(char protocolVersion, int msgId,int replyId,
			char orderId, char options, String subchannel, byte[] data,
			char totalMsgs) {
		super();
		this.protocolVersion = protocolVersion;
		this.msgId = msgId;
		this.replyId = replyId;
		this.orderId = orderId;
		this.options = options;
		this.subchannel = subchannel;
		this.data = data;
		this.totalMsgs = totalMsgs;
	}

	public MessagePart(byte[] rawData) throws IOException{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(rawData));
		protocolVersion = dis.readChar();
		options = dis.readChar();
		totalMsgs = dis.readChar();
		msgId = dis.readInt();
		orderId = dis.readChar();
		replyId = dis.readInt();
		
		StringBuilder b = new StringBuilder();
		for(int i=0;i<16;i++){
			
			char c = dis.readChar();
			if(c!=0){
				b.append(c);
			}
		}
		subchannel = b.toString();
		data = new byte[rawData.length-48];
		dis.readFully(data);
	}

	public char getProtocolVersion() {
		return protocolVersion;
	}



	public int getMsgId() {
		return msgId;
	}

	public int getOrderId() {
		return orderId;
	}

	public char getOptions() {
		return options;
	}
	
	public boolean getOption(char option){
		return (options ^ option) < options;
	}
	

	public String getSubchannel() {
		return subchannel;
	}

	public byte[] getData() {
		return data;
	}
	
	public char getTotalMsgs() {
		return totalMsgs;
	}	

	
	public void writeOut(DataOutputStream dos) throws IOException{
		dos.writeChar(protocolVersion);
		dos.writeChar(options);
		dos.writeChar(totalMsgs);
		dos.writeInt(msgId);
		dos.writeChar(orderId);
		dos.writeInt(replyId);
				
		StringBuilder b = new StringBuilder();
		b.append(subchannel);
		b.setLength(16);
		
		dos.writeChars(b.toString());
		
		dos.write(data);
		dos.flush();
	}

	public byte[] writeOutBytes() throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		writeOut(dos);
		byte[] r = bos.toByteArray();
		bos.close();
		dos.close();
		return r;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessagePart other = (MessagePart) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		if (msgId != other.msgId)
			return false;
		if (options != other.options)
			return false;
		if (orderId != other.orderId)
			return false;
		if (protocolVersion != other.protocolVersion)
			return false;
		if (subchannel == null) {
			if (other.subchannel != null)
				return false;
		} else if (!subchannel.equals(other.subchannel))
			return false;
		if (totalMsgs != other.totalMsgs)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MessagePart [protocolVersion=" + protocolVersion
				+ ", msgId=" + msgId
				+ ", orderId=" + orderId + ", options=" + options
				+ ", subchannel=" + subchannel + ", data="
				+ Arrays.toString(data) + ", totalMsgs=" + (int)totalMsgs + "]";
	}
	
	
	
	
	
}
