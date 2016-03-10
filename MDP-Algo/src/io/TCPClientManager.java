package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import algorithm.RobotManager;
import ui.MainControl;

public class TCPClientManager {
	private static final String HOST = "192.168.5.21";
//	private static final String HOST = "127.0.0.1";
	private static final int PORT = 3000;

	private static Socket clientSocket;
	private static BufferedReader in;
	private static PrintWriter out;

	public static void openConnection(String host, int port) {
		try {
			clientSocket = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream());
			MainControl.mainWindow.setConnectionStatus("Connnected to " + host + ":" + port);
			sendMessage("Hello");
		} catch (IOException ex) {
			MainControl.mainWindow.setConnectionStatus("Not Connnected");
			ex.printStackTrace();
		}
	}

	public static void openConnection() {
		openConnection(HOST, PORT);
	}

	public static void closeConnection() {
		try {
			clientSocket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			MainControl.mainWindow.setConnectionStatus("Not Connnected");
		}
	}

	public static void sendMessage(String message) {
		Thread thread = new Thread() {
			public void run() {
				if(out != null){
					out.println(message);
					out.flush();
					System.out.println("Sent:" + message);
				}
			}
		};
		thread.start();
	}

	public static void continuouslyReading() {
		String content = "";
		while (content != null && !clientSocket.isClosed()) {
			
			if (content.matches("^([0-9]+,){5}$")) {
				RobotManager.startExploration(content);
			} else if (content.matches("^([0-9]){5}$")) {
				RobotManager.initialiseRobot(content);
			} else if (content.matches("^STARTEXP$")) {
				if(clientSocket.getPort() == PORT){
					RobotManager.setIsDelay(false);
				}else{
					RobotManager.setIsDelay(true);
				}
				RobotManager.initialiseRealExploration();
			} else if (content.matches("^STARTFAS$")) {
				Thread thread = new Thread() {
					@Override
					public void run() {
//						RobotManager.startFastestRun();
						RobotManager.initialiseFastestRun();
						RobotManager.startRealFastestRun();
					}
				};
				thread.start();
			} else if(content.matches("DONE")){
				Thread thread = new Thread() {
					@Override
					public void run() {
//						RobotManager.startFastestRun();
						RobotManager.startRealFastestRun();
					}
				};
				thread.start();
			}
			try {
				content = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			System.out.println(content);
			MainControl.mainWindow.setFreeOutput("-Received from Server: " + content + "\n");
		}
	}
}
