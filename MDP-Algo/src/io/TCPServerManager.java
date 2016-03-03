package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerManager {
	public static final int PORT = 8000;
	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static PrintWriter out;
	private static BufferedReader in;

	public static void listen() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(PORT);
					clientSocket = serverSocket.accept();
					out = new PrintWriter(clientSocket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					sendToClient("Hi");
					// String content;
					// while((content = in.readLine()) != null){
					// System.out.println(content);
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	public static void closeConnection() {
		try {
			serverSocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void resetConnection(){
		closeConnection();
		listen();
	}

	public static void sendToClient(String content) {
		out.println(content);
	}

	public static void receiveFromClient() {
		try {
			System.out.println(in.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void startFastestRun(){
		sendToClient("STARTFAS");
	}

}
