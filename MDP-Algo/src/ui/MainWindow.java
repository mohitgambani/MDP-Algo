package ui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

import algorithm.RobotManager;
import io.FileIOManager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainWindow extends JFrame {

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
	

	public MainWindow() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 800);
		setLocationRelativeTo(null);
		setTitle("Robot Simulation - CE/CZ3004 Multidisciplinary Design Project");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 0));
		rightPanel.setMinimumSize(new Dimension(250, 800));

		JSplitPane subSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		subSplitPane.setResizeWeight(0.5);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, rightPanel);

		JPanel rightUpPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		rightUpPanel.setBorder(new EmptyBorder(0, 10, 5, 10));
		JPanel rightDownPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		rightDownPanel.setBorder(new EmptyBorder(5, 10, 0, 10));
		
		robot_position = new JLabel("Robot Position: unknown", JLabel.CENTER);
		rightUpPanel.add(robot_position);
		
		timerDisplay = new JLabel("0 min 0 s 0 ms", JLabel.CENTER);
		rightUpPanel.add(timerDisplay);

		mapExplored = new JLabel("Map Explored: 0.00%", JLabel.CENTER);
		rightUpPanel.add(mapExplored);
		
		JPanel movePerSecondPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		movePerSecondLabel = new JLabel("Move(s)/Second:");
		movePerSecondText = new JTextField(RobotManager.getMovePerSecond() + "");
		movePerSecondText.getDocument().addDocumentListener(new MovePerSecondDocumentListener());
		movePerSecondText.addFocusListener(new myFocusListener());
		movePerSecondPanel.add(movePerSecondLabel);
		movePerSecondPanel.add(movePerSecondText);
		rightUpPanel.add(movePerSecondPanel);
		
		rightUpPanel.add(new JLabel("Automatic Termination After:"));
		JPanel mapExplored = new JPanel(new GridLayout(1, 2, 5, 5));
		percentageExplored = new JTextField("100");
		percentageExplored.addFocusListener(new myFocusListener());
		percentageExplored.getDocument().addDocumentListener(new PercentageExploredDocumentListener());
		mapExplored.add(percentageExplored);
		mapExplored.add(new JLabel("% Map Explored", JLabel.CENTER));
		rightUpPanel.add(mapExplored);
		
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
		rightUpPanel.add(timeLimitPanel);
		

		generateMap = new JButton("Generate Random Map");
		generateMap.addActionListener(new GenerateMapListener());
		rightUpPanel.add(generateMap);
		
		loadMap = new JButton("Load Map");
		loadMap.addActionListener(new LoadMapActionListener());
		rightUpPanel.add(loadMap);

		resetMap = new JButton("Reset Map");
		resetMap.addActionListener(new ResetMapListener());
		rightUpPanel.add(resetMap);

		startExp = new JButton("Start Exploration");
		startExp.addActionListener(new StartExpListener());
		rightUpPanel.add(startExp);

		startFastRun = new JButton("Start Fastest Run");
		startFastRun.addActionListener(new StartFastRunListener());
		rightUpPanel.add(startFastRun);

		freeOutput = new JTextArea();
		freeOutput.setEditable(false);
		scrollPane = new JScrollPane(freeOutput);
		DefaultCaret caret = (DefaultCaret) freeOutput.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		rightDownPanel.add(scrollPane);

		contentPane.add(splitPane);
		splitPane.setResizeWeight(1);
		
		rightPanel.add(rightUpPanel);
		rightPanel.add(rightDownPanel);

		JPanel topPanel = new JPanel(new GridLayout(15, 20));
		JPanel bottomPanel = new JPanel(new GridLayout(15, 20));

		int num;
		for (num = 0; num < MapManager.MAP_WIDTH * MapManager.MAP_HEIGHT; ++num) {
			MapComponent humanMapComponent = new MapComponent(num);
			topPanel.add(humanMapComponent);
			MapManager.humanMap.add(humanMapComponent);
			MapComponent robotMapComponent = new MapComponent(-(num + 1));
			bottomPanel.add(robotMapComponent);
			MapManager.robotMap.add(robotMapComponent);
		}

		subSplitPane.setTopComponent(topPanel);
		subSplitPane.setBottomComponent(bottomPanel);
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
			MapManager.startExploration();
		}

	}
	
	private class StartFastRunListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			MapManager.startFastestRun();
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
}