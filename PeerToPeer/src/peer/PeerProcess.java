package peer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import peer.message.BitField;
import peer.message.Piece;
import thread.IncomingPeerListener;
import thread.OutgoingConnectionRequest;
import fileManager.CommonConfigFileReader;
import fileManager.Logger;
import fileManager.MyFileReader;
import fileManager.PeerInfoFileReader;

public class PeerProcess {
	
	private int numberOfPreferredNeighbors;
    private int unchokingInterval;
    private int optimisticUnchokingInterval;
    public static String fileName;
    private long fileSize;
    private long pieceSize;
    private int totalPieces;
    private int peer_ID;
    private int port;
    private boolean hasCompleteFile;
    public static HashMap<Integer, Piece> map;
    public static ArrayList<Peer> peers = new ArrayList<Peer>();
    public static ArrayList<Integer> allPeerID;
    public static LinkedList<MessageList> messageList = new LinkedList<MessageList>(); 
    public static ArrayList<HasCompleteFile> hasDownloadedCompleteFile = new ArrayList<HasCompleteFile>();
    
	public static void main(String[] args) {
		
    	PeerProcess self = new PeerProcess();
    	
    	//read common config file
		CommonConfigFileReader common = new CommonConfigFileReader(); 
		common.readFile();
		
		self.numberOfPreferredNeighbors = common.getNumberOfPreferredNeighbors();
		self.unchokingInterval = common.getUnchokingInterval();
		self.optimisticUnchokingInterval = common.getOptimisticUnchokingInterval();
		fileName = common.getFileName();
		self.fileSize = common.getFileSize();
		self.pieceSize = common.getPieceSize();
		
		self.totalPieces = (int) Math.ceil((double)self.fileSize/self.pieceSize);
		
		//read peer info file
		PeerInfoFileReader peerInfo = new PeerInfoFileReader(Integer.parseInt(args[0]));
		System.out.println("My peer id = " + args[0]);
		System.out.println();
		peerInfo.readFile();
		allPeerID = peerInfo.getAllPeerID();
		
		self.peer_ID = peerInfo.getPeer_ID();
		self.port = peerInfo.getPort();
		self.hasCompleteFile = peerInfo.isHasCompleteFile();
		
//		System.out.println(self.totalPieces);
//		System.out.println(self.hostName);
//		System.out.println(self.hasCompleteFile);
		
		BitField.setBitfield(self.hasCompleteFile,self.totalPieces);
		
		Logger.startLogger(self.peer_ID);
		
		if(self.hasCompleteFile == false) {
			
			map = new HashMap<Integer, Piece>();
			
			IncomingPeerListener peerListener = new IncomingPeerListener(self.port, self.peer_ID, self.totalPieces, self.hasCompleteFile, self.fileSize, self.pieceSize);
			peerListener.start();
			
			OutgoingConnectionRequest connect = new OutgoingConnectionRequest(self.peer_ID, self.totalPieces, self.hasCompleteFile, self.fileSize, self.pieceSize);
			connect.start();
		}
	
		else if(self.hasCompleteFile == true) {
			MyFileReader reader = new MyFileReader(self.peer_ID, self.pieceSize, fileName);
			map = reader.readFile();
			
			IncomingPeerListener peerListener = new IncomingPeerListener(self.port, self.peer_ID, self.totalPieces, self.hasCompleteFile, self.fileSize, self.pieceSize);
			peerListener.start();
//			Set<Integer> keys = self.map.keySet();
//			Iterator<Integer> it = keys.iterator();
//			
//			while(it.hasNext()) {
//				Integer i = it.next();
//				System.out.println(i + " " + self.map.get(i).getMessage());
//			}
		}
		
		
	}
}
