package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

import algorithm.RobotManager;
import io.FileIOManager;
import io.TCPClientManager;
import io.TCPServerManager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainWindow extends JFrame implements WindowListener{

	private JPanel contentPane;
	private JLabel robot_position;
	private JLabel movePerSecondLabel;
	private JTextField movePerSecondText;
	private JTextField percentageExplored;
	private JTextField timeLimitMin;
	private JTextField timeLimitSec;
	private JButton generateMap;
	private JButton loadMap;
	private JButton resetMap;
	private JButton startExp;
	private JButton startFastRun;
	private JLabel timerDisplay;
	private JLabel mapExplored;
	private JTextArea freeOutput;
	private JScrollPane scrollPane;
	
	private JRadioButton simulationRadio;
	private JRadioButton realRadio;
	
	private JLabel connectionStatus;
	

	public MainWindow() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(990, 600);
		setLocationRelativeTo(null);
		setTitle("Robot Simulation - CE/CZ3004 Multidisciplinary Design Project");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		JPanel controlPanel = new JPanel(new GridLayout(2, 1, 0, 0));
		controlPanel.setMinimumSize(new Dimension(250, 800));
		
		JPanel statusPanel = new JPanel(new GridLayout(1, 4));
		
		JPanel mapPanel = new JPanel(new GridLayout(MapManager.MAP_HEIGHT, MapManager.MAP_HEIGHT));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapPanel, controlPanel);
		
		JPanel topRightPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		topRightPanel.setBorder(new EmptyBorder(0, 10, 5, 10));
		JPanel bottomRightPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		bottomRightPanel.setBorder(new EmptyBorder(5, 10, 0, 10));
		
		robot_position = new JLabel("Robot Position: unknown", JLabel.CENTER);
		statusPanel.add(robot_position);
		
		timerDisplay = new JLabel("0 min 0 s 0 ms", JLabel.CENTER);
		statusPanel.add(timerDisplay);

		mapExplored = new JLabel("Map Explored: 0.00%", JLabel.CENTER);
		statusPanel.add(mapExplored);
		
		connectionStatus = new JLabel("Not Connnected", JLabel.CENTER);
		statusPanel.add(connectionStatus);
		
		JPanel movePerSecondPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		movePerSecondLabel = new JLabel("Move(s)/Second:");
		movePerSecondText = new JTextField(RobotManager.getMovePerSecond() + "");
		movePerSecondText.getDocument().addDocumentListener(new MovePerSecondDocumentListener());
		movePerSecondText.addFocusListener(new myFocusListener());
		movePerSecondPanel.add(movePerSecondLabel);
		movePerSecondPanel.add(movePerSecondText);
		topRightPanel.add(movePerSecondPanel);
		
		topRightPanel.add(new JLabel("Automatic Termination After:"));
		JPanel mapExplored = new JPanel(new GridLayout(1, 2, 5, 5));
		percentageExplored = new JTextField("100");
		percentageExplored.addFocusListener(new myFocusListener());
		percentageExplored.getDocument().addDocumentListener(new PercentageExploredDocumentListener());
		mapExplored.add(percentageExplored);
		mapExplored.add(new JLabel("% Map Explored", JLabel.CENTER));
		topRightPanel.add(mapExplored);
		
		JPanel timeLimitPanel = new JPanel(new GridLayout(1, 4, 5, 5));
		timeLimitMin = new JTextField("6");
		timeLimitMin.getDocument().addDocumentListener(new TimeLimitMinDocumentListener());
		timeLimitMin.addFocusListener(new myFocusListener());
		timeLimitSec = new JTextField("0");
		timeLimitSec.getDocument().addDocumentListener(new TimeLimitSecDocumentListener());
		timeLimitSec.addFocusListener(new myFocusListener());
		timeLimitPanel.add(timeLimitMin);
		timeLimitPanel.add(new JLabel("Min", JLabel.CENTER));
		timeLimitPanel.add(timeLimitSec);
		timeLimitPanel.add(new JLabel("Sec", JLabel.CENTER));
		topRightPanel.add(timeLimitPanel);
		

		generateMap = new JButton("Generate Random Map");
		generateMap.addActionListener(new GenerateMapListener());
		topRightPanel.add(generateMap);
		
		loadMap = new JButton("Load Map");
		loadMap.addActionListener(new LoadMapActionListener());
		topRightPanel.add(loadMap);

		resetMap = new JButton("Reset Map");
		resetMap.addActionListener(new ResetMapListener());
		topRightPanel.add(resetMap);

		startExp = new JButton("Start Exploration");
		startExp.addActionListener(new StartExpListener());
		topRightPanel.add(startExp);

		startFastRun = new JButton("Start Fastest Run");
		startFastRun.addActionListener(new StartFastRunListener());
		topRightPanel.add(startFastRun);
		
		simulationRadio = new JRadioButton("Simulation");
		simulationRadio.addActionListener(new SimulationRadioListener());
		realRadio = new JRadioButton("Real Run");
		realRadio.addActionListener(new RealRadioListener());
		simulationRadio.setSelected(true);
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(simulationRadio);
		radioGroup.add(realRadio);
		JPanel radioButtons = new JPanel(new GridLayout(1, 2));
		radioButtons.add(simulationRadio);
		radioButtons.add(realRadio);
		topRightPanel.add(radioButtons);
		

		freeOutput = new JTextArea();
//		freeOutput.setEditable(false);
		scrollPane = new JScrollPane(freeOutput);
		DefaultCaret caret = (DefaultCaret) freeOutput.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		bottomRightPanel.add(scrollPane);

		contentPane.add(splitPane, BorderLayout.CENTER);
		contentPane.add(statusPanel, BorderLayout.PAGE_END);
		splitPane.setResizeWeight(1);
		
		controlPanel.add(topRightPanel);
		controlPanel.add(bottomRightPanel);
		
//		JPanel bottomPanel = new JPanel(new GridLayout(15, 20));

		int num;
		for (num = 0; num < MapManager.MAP_WIDTH * MapManager.MAP_HEIGHT; ++num) {
			MapComponent mapComponent = new MapComponent(num);
			mapPanel.add(mapComponent);
			MapManager.arena.add(mapComponent);
//			MapComponent robotMapComponent = new MapComponent(-(num + 1));
//			bottomPanel.add(robotMapComponent);
//			MapManager.robotMap.add(robotMapComponent);
		}

//		subSplitPane.setTopComponent(topPanel);
//		subSplitPane.setBottomComponent(bottomPanel);
		
	}
	
	private class SimulationRadioListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Thread thread = new Thread(){
				@Override
				public void run(){
					MapManager.AllowSetObstacle();
					MapManager.resetMap();
//					TCPServerManager.resetConnection();
					TCPClientManager.closeConnection();
//					TCPClientManager.openConnection("127.0.0.1", TCPServerManager.PORT);
//					TCPClientManager.continuouslyReading();
				}
			};
			thread.start();
		}	
	}
	
	private class RealRadioListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Thread thread = new Thread(){
				@Override
				public void run(){
					MapManager.blockSetObstacle();
					MapManager.resetMap();
//					TCPServerManager.resetConnection();
//					TCPServerManager.closeConnection();
//					TCPClientManager.closeConnection();
					TCPClientManager.openConnection();
					TCPClientManager.continuouslyReading();
				}
			};
			thread.start();
		}	
	}

	private class ResetMapListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			MapManager.resetMap();
		}

	}

	private class StartExpListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
//			MapManager.startExploration();
			Thread thread = new Thread(){
				@Override
				public void run(){
//					TCPServerManager.startExploration();
					RobotManager.startExploration();
				}
			};
			thread.start();
		}

	}
	
	private class StartFastRunListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
//			MapManager.startFastestRun();
			Thread thread = new Thread(){
				@Override
				public void run(){
//					TCPServerManager.startFastestRun();
					RobotManager.startFastestRun();
				}
			};
			thread.start();
		}

	}

	private class GenerateMapListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			MapManager.generateMap();
		}

	}
	
	private class MovePerSecondDocumentListener implements DocumentListener{

		@Override
		public void insertUpdate(DocumentEvent e) {
			detectPositiveInteger();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			detectPositiveInteger();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			detectPositiveInteger();
		}
		public void detectPositiveInteger(){
			
			Runnable detectPostiveInteger = new Runnable() {
		        @Override
		        public void run() {
		        	int value = 0;
					try{
						value = Integer.parseInt(movePerSecondText.getText());
					}catch(NumberFormatException exception){
						movePerSecondText.setText("");
					}finally {
						if(value <= 0){
							movePerSecondText.setText("");
						}else{
							RobotManager.setMovePerSecond(value);
						}
					}
		        }
		    };
		    SwingUtilities.invokeLater(detectPostiveInteger);
		}
	}
	private class PercentageExploredDocumentListener extends MovePerSecondDocumentListener{
		@Override
		public void detectPositiveInteger(){
			Runnable detectPostiveInteger = new Runnable() {
		        @Override
		        public void run() {
		        	double value = -1.0;
					try{
						value = Double.parseDouble(percentageExplored.getText());
					}catch(NumberFormatException exception){
						percentageExplored.setText("");
					}finally {
						if(value < 0.0 || value > 100.0){
							percentageExplored.setText("");
						}else{
							RobotManager.setPercentageLimit(value);
						}
					}
		        }
		    };
		    SwingUtilities.invokeLater(detectPostiveInteger);
		}
	}
	private class TimeLimitMinDocumentListener extends MovePerSecondDocumentListener{
		@Override
		public void detectPositiveInteger(){
			Runnable detectPostiveInteger = new Runnable() {
		        @Override
		        public void run() {
		        	int value = -1;
					try{
						value = Integer.parseInt(timeLimitMin.getText());
					}catch(NumberFormatException exception){
						timeLimitMin.setText("");
					}finally {
						if(value < 0 || value >= 60){
							timeLimitMin.setText("");
						}
					}
		        }
		    };
		    SwingUtilities.invokeLater(detectPostiveInteger);
		}
	}
	
	private class TimeLimitSecDocumentListener extends MovePerSecondDocumentListener{
		@Override
		public void detectPositiveInteger(){
			Runnable detectPostiveInteger = new Runnable() {
		        @Override
		        public void run() {
		        	int value = -1;
					try{
						value = Integer.parseInt(timeLimitSec.getText());
					}catch(NumberFormatException exception){
						timeLimitSec.setText("");
					}finally {
						if(value < 0 || value >= 60){
							timeLimitSec.setText("");
						}
					}
		        }
		    };
		    SwingUtilities.invokeLater(detectPostiveInteger);
		}
	}
	
	private class LoadMapActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			int returnVal = fileChooser.showOpenDialog(MainWindow.this);
			
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
	            FileIOManager.setReadFilePath(file.getPath());
	            MapManager.readMap();
	        }
			
		}
		
	}
	
	private class myFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent e) {
			if (e.getSource() instanceof JTextField) {
				JTextField textField = (JTextField) e.getSource();
				textField.selectAll();
			}
		}

		@Override
		public void focusLost(FocusEvent e) {}
		
	}

	public void setTimerDisplay(String display) {
		timerDisplay.setText(display);
	}

	public void setRobotPosition(String position) {
		robot_position.setText("Robot Position: " + position);
	}

	public void setMapExplored(String explored) {
		mapExplored.setText(explored);
	}

	public void setFreeOutput(String output) {
		freeOutput.append(output);
	}
	public void clearFreeOutput(){
		freeOutput.setText("");
	}
	public long getTimeLimit(){
		return (Integer.parseInt(timeLimitMin.getText()) * 60 + Integer.parseInt(timeLimitSec.getText())) * 1000;
	}
	public void setConnectionStatus(String status){
		connectionStatus.setText(status);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		TCPClientManager.closeConnection();
		TCPServerManager.closeConnection();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}