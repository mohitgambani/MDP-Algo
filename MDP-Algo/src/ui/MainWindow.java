package ui;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;

import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
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
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(750, 800);
		setLocationRelativeTo(null);
		setTitle("Robot Simulation");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		// JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		// splitPane.setResizeWeight(0.5);
		// contentPane.add(splitPane);
		//
		// JPanel topPanel = new JPanel(new GridLayout(15, 20));
		// JPanel bottomPanel = new JPanel(new GridLayout(15, 20));
		//
		// int num;
		// for (num = 0; num < MapManager.MAP_WIDTH * MapManager.MAP_HEIGHT;
		// ++num) {
		// MapComponent humanMapComponent = new MapComponent(num);
		// topPanel.add(humanMapComponent);
		// MapManager.humanMapComponents.add(humanMapComponent);
		// MapComponent robotMapComponent = new MapComponent(-(num + 1));
		// bottomPanel.add(robotMapComponent);
		// MapManager.robotMapComponents.add(robotMapComponent);
		// }
		//
		// splitPane.setTopComponent(topPanel);
		// splitPane.setBottomComponent(bottomPanel);
		//
		// MapManager.drawStartZone();
		// MapManager.drawGoalZone();

		JPanel rightPanel = new JPanel(new GridLayout(10, 1));
		rightPanel.setMinimumSize(new Dimension(200, 800));
		
		JSplitPane subSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		subSplitPane.setResizeWeight(0.5);

		JButton jbutton = new JButton("JButton");
		rightPanel.add(jbutton);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, rightPanel);
		contentPane.add(splitPane);
		splitPane.setResizeWeight(1.0);

		JPanel topPanel = new JPanel(new GridLayout(15, 20));
		JPanel bottomPanel = new JPanel(new GridLayout(15, 20));

		int num;
		for (num = 0; num < MapManager.MAP_WIDTH * MapManager.MAP_HEIGHT; ++num) {
			MapComponent humanMapComponent = new MapComponent(num);
			topPanel.add(humanMapComponent);
			MapManager.humanMapComponents.add(humanMapComponent);
			MapComponent robotMapComponent = new MapComponent(-(num + 1));
			bottomPanel.add(robotMapComponent);
			MapManager.robotMapComponents.add(robotMapComponent);
		}

		subSplitPane.setTopComponent(topPanel);
		subSplitPane.setBottomComponent(bottomPanel);

		MapManager.drawStartZone();
		MapManager.drawGoalZone();

	}
}
