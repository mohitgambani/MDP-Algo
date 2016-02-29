package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import algorithm.RobotManager;

public class NetworkIOManager {
	private static final String HOST = "192.168.5.21";
	private static final int PORT = 3000;
	private static Socket socket;

	public static void openConnection() {
		try {
			socket = new Socket(HOST, PORT);
			sendMessage("Hello");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println("Connnected to " + HOST);
	}

	public static boolean closeConnection() {
		try {
			socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static void sendMessage(String message) {
		Thread thread = new Thread() {
			public void run() {
				try {
					PrintWriter out = new PrintWriter(socket.getOutputStream());
					out.print(message);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	public static void continuouslyReading() {
		Thread thread = new Thread() {
			public void run() {
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String content = in.readLine();
					while (content != "$") {
						if(content.matches("([0-9]+,){5}")){
							System.out.println("robot readings matched");
						}else if(content.matches("([0-9]){5}")){
							RobotManager.initialiseRobot(content);
						}else if(content.matches("STARTEXP")){
							RobotManager.startExploration();
						}else if(content.matches("STARTFAS")){
							RobotManager.startFastestRun();
						}
						System.out.println(content);
						content = in.readLine();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

}
