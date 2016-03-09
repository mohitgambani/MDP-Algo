package ui;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import io.TCPClientManager;
import io.TCPServerManager;

public class MainControl {
	
	public static MainWindow mainWindow;
	
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				mainWindow = new MainWindow();
				mainWindow.setVisible(true);
				MapManager.drawStartZone();
				MapManager.drawGoalZone();
				
//				TCPServerManager.listen();
//				
//				Thread thread = new Thread(){
//					@Override
//					public void run(){
//						TCPClientManager.openConnection("127.0.0.1", TCPServerManager.PORT);
//						TCPClientManager.continuouslyReading();
//					}
//				};
//				thread.start();
			}
		});
	}
}
