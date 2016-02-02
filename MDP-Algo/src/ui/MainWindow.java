package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class MainWindow extends JFrame {


	private JPanel contentPane;
	private JLabel robot_position;
	private JButton generateMap;
	private JButton resetMap;
	private JButton startExp;
	private JButton startFastRun;
	private JLabel timerDisplay;
	private JLabel mapExplored;
	private JTextArea freeOutput;

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


		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setMinimumSize(new Dimension(200, 800));

		JSplitPane subSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		subSplitPane.setResizeWeight(0.5);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, rightPanel);

		robot_position = new JLabel("Robot Position: unknown");
		GridBagConstraints gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.weightx = 1.0;
		gridBagConstraint.weighty = 0.1;
		gridBagConstraint.gridx = 0;
		gridBagConstraint.gridy = 0;
		rightPanel.add(robot_position, gridBagConstraint);

		generateMap = new JButton("Generate Random Map");
		generateMap.addActionListener(new GenerateMapListener());
		gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.gridx = 0;
		gridBagConstraint.gridy = 1;
		gridBagConstraint.weightx = 1.0;
		gridBagConstraint.weighty = 0.1;
		rightPanel.add(generateMap, gridBagConstraint);

		resetMap = new JButton("Reset Map");
		resetMap.addActionListener(new ResetMapListener());
		gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.gridx = 0;
		gridBagConstraint.gridy = 2;
		gridBagConstraint.weightx = 1.0;
		gridBagConstraint.weighty = 0.1;
		rightPanel.add(resetMap, gridBagConstraint);

		startExp = new JButton("Start Exploration");
		startExp.addActionListener(new StartExpListener());
		gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.gridx = 0;
		gridBagConstraint.gridy = 3;
		gridBagConstraint.weightx = 1.0;
		gridBagConstraint.weighty = 0.1;
		rightPanel.add(startExp, gridBagConstraint);

		startFastRun = new JButton("Start Fastest Run");
		gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.gridx = 0;
		gridBagConstraint.gridy = 4;
		gridBagConstraint.weightx = 1.0;
		gridBagConstraint.weighty = 0.1;
		rightPanel.add(startFastRun, gridBagConstraint);

		timerDisplay = new JLabel("0s0ms");
		gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.gridx = 0;
		gridBagConstraint.gridy = 5;
		gridBagConstraint.weightx = 1.0;
		gridBagConstraint.weighty = 0.1;
		rightPanel.add(timerDisplay, gridBagConstraint);

		mapExplored = new JLabel("Map Explored: 0%");
		gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.gridx = 0;
		gridBagConstraint.gridy = 6;
		gridBagConstraint.weightx = 1.0;
		gridBagConstraint.weighty = 0.1;
		rightPanel.add(mapExplored, gridBagConstraint);

		freeOutput = new JTextArea("Free Output Area");
		freeOutput.setMinimumSize(new Dimension(200, 400));
		gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.gridx = 0;
		gridBagConstraint.gridy = 7;
		gridBagConstraint.weightx = 1.0;
		gridBagConstraint.weighty = 1.0;
		rightPanel.add(freeOutput, gridBagConstraint);

		contentPane.add(splitPane);
		splitPane.setResizeWeight(1.0);

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
	
	private class GenerateMapListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			MapManager.generateMap();
		}
		
	}
	
	public void setTimerDisplay(String display){
		timerDisplay.setText(display);
	}

	public void setRobotPosition(String position) {
		robot_position.setText("Robot Position: " + position);
	}
	
	public void setMapExplored(String explored){
		mapExplored.setText(explored);
	}
}
