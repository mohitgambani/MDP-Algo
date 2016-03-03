package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import algorithm.RobotManager;
import ui.MainControl;

public class NetworkIOManager {
	private static final String HOST = "192.168.5.21";
//	private static final String HOST = "127.0.0.1";
	private static final int PORT = 3000;
	private static Socket socket;
	private static BufferedReader in;
	private static PrintWriter out;

	public static void openConnection() {
		try {
			socket = new Socket(HOST, PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			sendMessage("Hello");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		MainControl.mainWindow.setFreeOutput("Connnected to " + HOST + "\n");
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
				out.print(message);
				out.flush();
			}
		};
		thread.start();
	}

	public static void continuouslyReading() {
		String content = "";
		do {
			try {
				content = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(content);
			if (content.matches("^([0-9]+,){5}$")) {
				RobotManager.startExploration(content);
			} else if (content.matches("^([0-9]){5}$")) {
				RobotManager.initialiseRobot(content);
			} else if (content.matches("^STARTEXP$")) {
				Thread thread = new Thread() {
					@Override
					public void run() {
						RobotManager.initialiseRealExploration();
//						RobotManager.startExploration();
					}
				};
				thread.start();
			} else if (content.matches("^STARTFAS$")) {
				Thread thread = new Thread() {
					@Override
					public void run() {
						RobotManager.startFastestRun();
					}
				};
				thread.start();
			}
		} while (content != "$");
	}

}
