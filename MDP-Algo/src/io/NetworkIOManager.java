package io;

import java.io.IOException;
import java.net.Socket;

public class NetworkIOManager {
	private static final String HOST = "localhost";
	private static final int PORT = 8000;
	private static Socket socket;
	
	public static boolean openConnection(){
		try{
			socket = new Socket(HOST, PORT);
		}catch(IOException ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean closeConnection(){
		try{
			socket.close();
		}catch(IOException ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
}
