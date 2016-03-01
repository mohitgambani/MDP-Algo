package ui;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import io.NetworkIOManager;

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
				
				NetworkIOManager.openConnection();
				NetworkIOManager.continuouslyReading();
			}
		});
	}
}
