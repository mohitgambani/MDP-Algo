package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import algorithm.SimulatedSensor;

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
					receiveFromClient();
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
			clientSocket.close();
			
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

	public static String receiveFromClient() {
		String content = "";
		try {
			content = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(content);
		return content;
	}
	
	public static void startFastestRun(){
		sendToClient("STARTFAS");
	}
	
	public static void startExploration(){
		sendToClient("STARTEXP");
		String nextMove = "";
		do{
			String sensingResult = SimulatedSensor.getSensoryInfo();
			System.out.println("#" + sensingResult);
			sendToClient(sensingResult);
			String fromClient = receiveFromClient();
			nextMove = fromClient.substring(1, fromClient.length());
//			System.out.println(nextMove);
		}while(!nextMove.equals("STOP"));
		
	}

}
