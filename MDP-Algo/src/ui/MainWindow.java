package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import java.awt.Canvas;
import java.awt.Color;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	
	private DrawCanvas drawCanvas;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane);
		
		
		
		drawCanvas = new DrawCanvas();
		splitPane.setRightComponent(drawCanvas);
		
	}
	
	private class DrawCanvas extends JPanel {
		
	      @Override
	      public void paintComponent(Graphics g) {
	         super.paintComponent(g);     // paint parent's background
	         setBackground(Color.BLACK);  // set background color for this JPanel
	 
	         // Your custom painting codes. For example,
	         // Drawing primitive shapes
	         g.setColor(Color.YELLOW);    // set the drawing color
	         g.drawLine(30, 40, 100, 200);
	         g.drawOval(150, 180, 10, 10);
	         g.drawRect(200, 210, 20, 30);
	         g.setColor(Color.RED);       // change the drawing color
	         g.fillOval(300, 310, 30, 50);
	         g.fillRect(400, 350, 60, 50);
	         // Printing texts
	         g.setColor(Color.WHITE);
	         g.setFont(new Font("Monospaced", Font.PLAIN, 12));
	         g.drawString("Testing custom drawing ...", 10, 20);
	      }
	   }

}
