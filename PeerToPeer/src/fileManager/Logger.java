package fileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.LinkedList;

public class Logger {

	private static int myPeer_ID;
	private static File file;
	private static BufferedWriter out;
	private static int numberOfPieces = 0;
	public static boolean fileFlag = false;
	public static boolean fileCompleteFlag = false;
	public static LinkedList<Integer> fileWriteOperation = new LinkedList<Integer>();
	
	public static void startLogger(int peer_ID) {
		
		myPeer_ID = peer_ID;
		String fileName = "log_peer_" + myPeer_ID + ".log";
		file = new File(fileName);
		
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			System.err.println(e);
		}
	}
	 
	public static void makeTCPConnection(int peer_ID) {
		
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeer_ID + " makes a connection to Peer " + peer_ID + ".";
			out.append(s);
			out.newLine();
			out.newLine();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void madeTCPConnection(int peer_ID) {
		
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeer_ID + " is connected from Peer " + peer_ID + ".";
			out.append(s);
			out.newLine();
			out.newLine();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void receiveHave(int peer_ID, int pieceIndex) {
		
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeer_ID + " received the 'have' message from Peer " + peer_ID + " for the piece " + pieceIndex + ".";
			out.append(s);
			out.newLine();
			out.newLine();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void receiveInterested(int peer_ID) {
		
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeer_ID + " received the 'interested' message from Peer " + peer_ID + ".";
			out.append(s);
			out.newLine();
			out.newLine();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void receiveNotInterested(int peer_ID) {
		
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeer_ID + " received the 'not interested' message from Peer " + peer_ID + ".";
			out.append(s);
			out.newLine();
			out.newLine();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void downloadPiece(int peer_ID, int pieceIndex) {
		
		numberOfPieces++;
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeer_ID + " has downloaded the piece " + pieceIndex +" from Peer " + peer_ID + ".";
			out.append(s);
			out.newLine();
			s = "Now  the number of pieces it has is " + numberOfPieces;
			out.append(s);
			out.newLine();
			out.newLine();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void downloadComplete() {
		
		if(fileFlag == true) {
			
			try {
				String date = new Date().toString();
				String s = date + " : Peer " + myPeer_ID + " has downloaded the complete file.";
				out.append(s);
				out.newLine();
				out.newLine();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
	
	public static void closeLogger() {
		try {
			out.close();
		} catch (IOException e) {
			System.err.println(e);
		} 
	}

}
