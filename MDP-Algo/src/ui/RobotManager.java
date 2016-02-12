package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.Timer;

import algorithm.Movable;
import algorithm.SimpleMove;

public class RobotManager {

	private static final int ROBOT_WIDTH = 2;
	private static final int ROBOT_HEIGHT = 2;

	protected static final int HEAD_UP = 0;
	protected static final int HEAD_DOWN = 1;
	protected static final int HEAD_LEFT = 2;
	protected static final int HEAD_RIGHT = 3;

	private static int robotPosition = 0; //Up left corner of the robot
	private static int robotOrientation = HEAD_UP;
	private static boolean isRobotSet = false;

	private static final int FRONT_SENSING_RANGE = 1;
	private static final int SIDE_SENSING_RANGE = 1;
	
	private static final int START_X = 0;
	private static final int START_Y = 0;
	private static final int END_X = START_X + MapManager.MAP_WIDTH - 1;
	private static final int END_Y = START_Y + MapManager.MAP_HEIGHT - 1;
	
	private static Timer timer;
	private static final int TIMER_DELAY = 1;
	private static long startTime;
	private static long timeElapsed;

	private static Movable moveStrategy = new SimpleMove();
	
	public static int getRobotPosition() {
		return robotPosition;
	}

	public static void setRobotPosition(int position) {
		robotPosition = position;
		isRobotSet = true;
		MainControl.mainWindow.setRobotPosition(MapManager.idToX(robotPosition) + "," + MapManager.idToY(robotPosition));
	}

	public static int getRobotOrientation() {
		return robotOrientation;
	}

	public static void setRobotOrientation(int orientation) {
		robotOrientation = orientation;
	}
	
	public static void startExploration(){
		if(isRobotSet){
			startTime = System.currentTimeMillis();
			timer = new Timer(TIMER_DELAY, new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					timeElapsed = System.currentTimeMillis() - startTime;
					MainControl.mainWindow.setTimerDisplay(timeElapsed / 1000 + "s" + timeElapsed % 1000 + "ms");
				}
			});
			Thread thread = new Thread(){
				@Override
				public void run() {
					timer.start();
					move();
					timer.stop();
				}
			};
			thread.start();
		}
	}
	
	public static void setRobot(boolean setRobot){
		isRobotSet = setRobot;
		if(!setRobot){
			MainControl.mainWindow.setRobotPosition("unknown");
		}
	}

	private static void move() {
		int nextMove;

		
			Thread thread = new Thread() {
				@Override
				public void run() {
					switch (robotOrientation) {
					case HEAD_UP:
						senseUp(FRONT_SENSING_RANGE);
						senseLeft(SIDE_SENSING_RANGE);
						senseRight(SIDE_SENSING_RANGE);
						break;
					case HEAD_DOWN:
						senseDown(FRONT_SENSING_RANGE);
						senseLeft(SIDE_SENSING_RANGE);
						senseRight(SIDE_SENSING_RANGE);
						break;
					case HEAD_LEFT:
						senseLeft(FRONT_SENSING_RANGE);
						senseUp(SIDE_SENSING_RANGE);
						senseDown(SIDE_SENSING_RANGE);
						break;
					case HEAD_RIGHT:
						senseRight(FRONT_SENSING_RANGE);
						senseUp(SIDE_SENSING_RANGE);
						senseDown(SIDE_SENSING_RANGE);
						break;
					}
				}
			};
			thread.start();			
			
			int robotPos = robotPosition;
			int robotPosX = MapManager.idToX(robotPos);
			int robotPosY = MapManager.idToY(robotPos);
			
			if(robotPosX > START_X){
				if( MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX - 1, robotPosY)) == false ||
						MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX - 1, robotPosY + 1)) == false ||
						((robotPosX > START_X + 1) && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX - 2, robotPosY)) == false ||
						MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX - 2, robotPosY + 1)) == false)) ||
						((robotPosY < END_Y) && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX - 1, robotPosY + 2)) == false))){
					if(moveLeft()){
						pause();
						
						move();
						
						if(MapManager.exploredSpaces.size() == MapManager.MAP_HEIGHT * MapManager.MAP_WIDTH)
							return;
						
						moveRight();
						
						pause();
					}
				}
			}
			if(robotPosX < END_X - 1){
				if( MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 2, robotPosY)) == false ||
						MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 2, robotPosY + 1)) == false ||
						((robotPosX < END_X - 2) && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 3, robotPosY)) == false ||
						MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 3, robotPosY + 1)) == false)) || 
						((robotPosY < END_Y) && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 2, robotPosY + 2)) == false))){
					if(moveRight()){
						pause();
						
						move();
						
						if(MapManager.exploredSpaces.size() == MapManager.MAP_HEIGHT * MapManager.MAP_WIDTH)
							return;
						
						moveLeft();
						
						pause();
					}
				}
			}
			if(robotPosY > START_Y){
				if( MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX, robotPosY - 1)) == false ||
						MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 1 , robotPosY - 1)) == false ||
						((robotPosY > START_Y + 1) && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX, robotPosY - 2)) == false ||
						MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 1 , robotPosY - 2)) == false)) || 
						((robotPosX > START_X) && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX - 1, robotPosY - 1)) == false)) ||
						((robotPosX < END_X) && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 2, robotPosY - 1)) == false))){
					if(moveUp()){
						pause();
						
						move();
						
						if(MapManager.exploredSpaces.size() == MapManager.MAP_HEIGHT * MapManager.MAP_WIDTH)
							return;
						
						moveDown();
						
						pause();
					}
				}
			}
			if(robotPosY < END_Y - 1){
				if( MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX, robotPosY + 2)) == false ||
						MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 1 , robotPosY + 2)) == false ||
						((robotPosY < END_Y - 2) && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX, robotPosY + 3)) == false ||
						MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 1 , robotPosY + 3)) == false))){
					if(moveDown()){
						
						
						pause();
						
						move();
						
						if(MapManager.exploredSpaces.size() == MapManager.MAP_HEIGHT * MapManager.MAP_WIDTH)
							return;
						
						moveUp();
						
						pause();
					}
				}
			}
			
						
		
	}

	private static boolean moveLeft() {
		return MapManager.moveLeft();
	}

	private static boolean moveRight() {
		return MapManager.moveRight();
	}

	private static boolean moveUp() {
		return MapManager.moveUp();
	}

	private static boolean moveDown() {
		return MapManager.moveDown();
	}

	private static void senseUp(final int RANGE) {
		Hashtable<Integer, Integer> results = MapManager.robotSensing(MapManager.idToX(robotPosition),
				MapManager.idToY(robotPosition) - RANGE, ROBOT_WIDTH, RANGE);
		moveStrategy.getMapUpdate(results);
	}

	private static void senseDown(final int RANGE) {
		Hashtable<Integer, Integer> results = MapManager.robotSensing(MapManager.idToX(robotPosition),
				MapManager.idToY(robotPosition) + ROBOT_HEIGHT, ROBOT_WIDTH, RANGE);
		moveStrategy.getMapUpdate(results);
	}

	private static void senseLeft(final int RANGE) {
		Hashtable<Integer, Integer> results = MapManager.robotSensing(MapManager.idToX(robotPosition) - RANGE,
				MapManager.idToY(robotPosition), RANGE, ROBOT_HEIGHT);
		moveStrategy.getMapUpdate(results);
	}

	private static void senseRight(final int RANGE) {
		Hashtable<Integer, Integer> results = MapManager.robotSensing(MapManager.idToX(robotPosition) + ROBOT_WIDTH,
				MapManager.idToY(robotPosition), RANGE, ROBOT_HEIGHT);
		moveStrategy.getMapUpdate(results);
	}
	
	public static int getRobotWidth(){
		return ROBOT_WIDTH;
	}
	
	public static int getRobotHeight(){
		return ROBOT_HEIGHT;
	}
	
	protected static void getExplorationPercentage(double percentage){
		MainControl.mainWindow.setMapExplored(String.format("Map Explored: %.2f%%", percentage * 100.0));
	}
	
	private static void pause(){
		try {
			Thread.sleep(300);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}
