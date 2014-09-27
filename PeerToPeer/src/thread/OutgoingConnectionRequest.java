package thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;

import peer.Handshake;
import peer.HasCompleteFile;
import peer.Peer;
import peer.PeerProcess;
import peer.message.BitField;
import fileManager.Logger;
import fileManager.PeerInfoFileReader;


public class OutgoingConnectionRequest extends Thread {
	
	private String hostName;
	private int port;
	private int myPeer_ID;
	private ArrayList<String[]> connectTo = new ArrayList<String[]>();
	private int totalPieces;
	private boolean haveAllPieces;
	private long fileSize; 
	private long pieceSize;
	
	public OutgoingConnectionRequest(int peer_ID, int totalPieces, boolean haveAllPieces, long fileSize, long pieceSize) {
		myPeer_ID = peer_ID;
		this.totalPieces = totalPieces;
		this.haveAllPieces = haveAllPieces;
		this.fileSize = fileSize;
		this.pieceSize = pieceSize;
	}
	
	@Override
	public void run() {
		
		PeerInfoFileReader peerInfo = new PeerInfoFileReader(myPeer_ID);
		connectTo = peerInfo.getPeerInfo();
		
		ListIterator<String[]> it = connectTo.listIterator();
		
		while(it.hasNext()) {
			String[] value = it.next();
			hostName = value[1];
			port = Integer.parseInt(value[2]);
			
			try {
				Socket socket = new Socket(hostName, port);
				
				//Handshake
				Handshake send = new Handshake(myPeer_ID);
				sendHandShake(socket, send.handshake);
				
				byte[] received = receiveHandShake(socket);
				byte[] temp = new byte[28];
				for(int i = 0 ; i < 28 ; i++){
					temp[i] = received[i];
				}
				
				String header = new String(temp);
				
				int j = 0;
				byte[] ID_temp = new byte[4];
				for(int i = 28 ; i < 32 ; i++) {
					ID_temp[j] = received[i];
					j++;
				}
				
				String s = new String(ID_temp);
				int ID = Integer.parseInt(s);
				
				if(header.equals("CNT5106C2013SPRING0000000000")) {
					
					boolean flag = false;
					ListIterator<Integer> iter = PeerProcess.allPeerID.listIterator();
					
					while(iter.hasNext()) {
						
						int num = iter.next().intValue(); 
						if(num != myPeer_ID) {
							if(num == ID) {
								flag = true;
								break;
							}
						}
					}
					
					if(flag == true) {
						
						Peer p = new Peer();
						p.setMyPeer_ID(myPeer_ID);
						p.setSocket(socket);
						p.setPeer_ID(Integer.parseInt(value[0]));
						
						//receive bitfield
						byte[] field = receiveBitfield(socket);
						p.setBitfield(field);
						
						//send bitfield
						sendBitfield(socket);
						p.setInterested(false);
						
						synchronized (PeerProcess.peers) {
							PeerProcess.peers.add(p);
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								System.err.println(e);
							}
						}
						
						HasCompleteFile completeFile = new HasCompleteFile();
						completeFile.setSocket(socket);
						completeFile.setHasDownLoadedCompleteFile(false);
						
						PeerProcess.hasDownloadedCompleteFile.add(completeFile);
						
						System.out.println("Connection request sent to " + Integer.parseInt(value[0]));
						System.out.println();
						Logger.makeTCPConnection(Integer.parseInt(value[0]));
						
						MessageSender messageSender = new MessageSender();
						messageSender.start();

						PieceRequest pieceReq = new PieceRequest(Integer.parseInt(value[0]), totalPieces, haveAllPieces, fileSize, pieceSize);
						pieceReq.start();
						
						MessageReceiver messageReceiver = new MessageReceiver(socket, pieceSize);
						messageReceiver.start();
						
//						ListIterator<Peer> list = PeerProcess.peers.listIterator();
//						
//						while(list.hasNext()) {
//							Peer pt = (Peer)list.next();
//							System.out.println(pt.getPeer_ID() + " " + pt.getSocket());
//						}
					}
					else {
						System.out.println("Unexpected peer connection");
					}
				}
				
				
			}  
			catch (UnknownHostException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
			
		}
	}
	

	private void sendBitfield(Socket socket) {
		
		try {
		
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(BitField.bitfield);
		
		} catch (IOException e) {
			System.err.println(e);
		}
		
	}

	
	private byte[] receiveBitfield(Socket socket) {
		
		byte[] bitfield = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			bitfield = (byte[]) in.readObject();
		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		
		return bitfield;
	}


	private void sendHandShake(Socket socket, byte[] handshake) {
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(handshake);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	private byte[] receiveHandShake(Socket socket) {
		
		byte[] handshake = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			handshake = (byte[]) in.readObject();
		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		
		return handshake;
	}

}
