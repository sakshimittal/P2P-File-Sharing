package peer;

public class Handshake {
	
	public byte[] handshake = new byte[32];
	private String header = "CNT5106C2013SPRING";
	private String zero_bits = "0000000000";
	private int peer_ID;
	
	public Handshake(int peer_ID) {
		this.peer_ID = peer_ID;
		String array = header + zero_bits + Integer.toString(this.peer_ID);
		handshake = array.getBytes();
	}
	
}
