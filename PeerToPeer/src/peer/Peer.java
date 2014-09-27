package peer;

import java.net.Socket;


public class Peer {
	
	private int myPeer_ID;
	private int peer_ID;
	private Socket socket;
	private byte[] bitfield;
	private boolean interested;
	
	public int getPeer_ID() {
		return peer_ID;
	}
	
	public void setPeer_ID(int peer_ID) {
		this.peer_ID = peer_ID;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public byte[] getBitfield() {
		return bitfield;
	}

	public void setBitfield(byte[] bitfield) {
		this.bitfield = bitfield;
	}

	public boolean isInterested() {
		return interested;
	}

	public void setInterested(boolean interested) {
		this.interested = interested;
	}

	public int getMyPeer_ID() {
		return myPeer_ID;
	}

	public void setMyPeer_ID(int myPeer_ID) {
		this.myPeer_ID = myPeer_ID;
	}
	
}
