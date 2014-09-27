package thread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import peer.MessageList;
import peer.PeerProcess;

public class MessageSender extends Thread {

	@Override
	public void run() {
		
		while(true) {
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.err.println(e);
			}
			
//			Iterator<MessageList> itr = PeerProcess.messageList.iterator();
//			while(itr.hasNext()) {
//				byte[] temp = (byte[]) itr.next().getMessage(); 
//				int type = temp[4];
//				if(type == 7){
//					
//				}
//				else {
//				
//					for (int i = 0; i < temp.length; i++) {
//						System.out.print(temp[i]);
//					}
//					System.out.println();
//				}
//				
//			}
			
			if(!PeerProcess.messageList.isEmpty()) {
				
				synchronized (PeerProcess.messageList) {
					MessageList m = PeerProcess.messageList.poll();
					Socket socket = m.getSocket();
					byte[] message = m.getMessage();
					sendMessage(socket, message);
					
				}
			}
		}
	}
	
	
	public void sendMessage(Socket socket, byte[] message) {
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			synchronized (socket) {
				out.writeObject(message);
			}
		} catch (IOException e) {
			System.err.println(e);
		} 

	}
	
}
