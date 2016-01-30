package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

public class MainWindow extends JFrame {

	private JPanel contentPane;

	// private static JButton button = new JButton("JButton5");
	// private DrawCanvas drawCanvas;
	// ArrayList<MapComponent> humanMapComponents;
	// ArrayList<MapComponent> robotMapComponents;
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
		setSize(600, 800);
		setLocationRelativeTo(null);
		setTitle("Robot Simulation");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(0.5);
		contentPane.add(splitPane);

		// humanMapComponents = new ArrayList<MapComponent>();
		// robotMapComponents = new ArrayList<MapComponent>();

		// splitPane.setLeftComponent(button);

		// JSplitPane subSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		// subSplitPane.setLayout(new GridLayout(15, 20, 0, 0));
		JPanel topPanel = new JPanel(new GridLayout(15, 20));
		JPanel bottomPanel = new JPanel(new GridLayout(15, 20));

		int num;
		for (num = 0; num < MapManager.MAP_WIDTH * MapManager.MAP_HEIGHT; ++num) {
			MapComponent humanMapComponent = new MapComponent(num);
			topPanel.add(humanMapComponent);
			MapManager.humanMapComponents.add(humanMapComponent);
			MapComponent robotMapComponent = new MapComponent(num);
			bottomPanel.add(robotMapComponent);
			MapManager.robotMapComponents.add(robotMapComponent);
		}

		splitPane.setTopComponent(topPanel);
		splitPane.setBottomComponent(bottomPanel);
		
		MapManager.drawStartZone();
		MapManager.drawGoalZone();
		// topPanel.add(new JButton("JButton3"));

		// subSplitPane.setTopComponent(new JButton("JButton3"));
		// subSplitPane.setTopComponent(topPanel);
		// subSplitPane.setBottomComponent(bottomPanel);
		// subSplitPane.setResizeWeight(0.5);

		// splitPane.setRightComponent(subSplitPane);

		// drawCanvas = new DrawCanvas();
		// splitPane.setRightComponent(drawCanvas);

	}

	// public void setFill(JButton button){
	// button.setBackground(Color.BLACK);
	// }
	//
	// public static JButton getButton(){
	// return button;
	// }

	// private class DrawCanvas extends JPanel {
	//
	// @Override
	// public void paintComponent(Graphics g) {
	// super.paintComponent(g); // paint parent's background
	// setBackground(Color.BLACK); // set background color for this JPanel
	//
	// // Your custom painting codes. For example,
	// // Drawing primitive shapes
	// g.setColor(Color.YELLOW); // set the drawing color
	// g.drawLine(30, 40, 100, 200);
	// g.drawOval(150, 180, 10, 10);
	// g.drawRect(200, 210, 20, 30);
	// g.setColor(Color.RED); // change the drawing color
	// g.fillOval(300, 310, 30, 50);
	// g.fillRect(400, 350, 60, 50);
	// // Printing texts
	// g.setColor(Color.WHITE);
	// g.setFont(new Font("Monospaced", Font.PLAIN, 12));
	// g.drawString("Testing custom drawing ...", 10, 20);
	// }
	// }

}
