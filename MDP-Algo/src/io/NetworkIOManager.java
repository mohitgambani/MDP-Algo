package io;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkIOManager {
	private static final String HOST = "192.168.5.21";
	private static final int PORT = 3000;
	private static Socket socket;
	
	public static boolean openConnection(){
		PrintWriter out;
		try{
			socket = new Socket(HOST, PORT);
			out = new PrintWriter(socket.getOutputStream());
			out.println("Hello");
			out.flush();
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
