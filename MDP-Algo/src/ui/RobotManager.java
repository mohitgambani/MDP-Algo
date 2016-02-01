package ui;

import java.util.Hashtable;

import algorithm.Movable;
import algorithm.SimpleMove;

public class RobotManager {

	protected static final int ROBOT_WIDTH = 2;
	protected static final int ROBOT_HEIGHT = 2;

	protected static final int HEAD_UP = 0;
	protected static final int HEAD_DOWN = 1;
	protected static final int HEAD_LEFT = 2;
	protected static final int HEAD_RIGHT = 3;

	private static int robotUpLeft = 0;
	private static int robotOrientation = HEAD_UP;
	private static boolean isRobotSet = false;

	private static final int FRONT_SENSING_RANGE = 2;
	private static final int SIDE_SENSING_RANGE = 1;

	private static Movable moveStrategy = new SimpleMove();

	public static int getRobotUpLeft() {
		return robotUpLeft;
	}

	public static void setRobotUpLeft(int upLeft) {
		robotUpLeft = upLeft;
		isRobotSet = true;
		MainControl.mainWindow.setRobotPosition(MapManager.idToX(robotUpLeft) + "," + MapManager.idToY(robotUpLeft));
	}

	public static int getRobotOrientation() {
		return robotOrientation;
	}

	public static void setRobotOrientation(int orientation) {
		robotOrientation = orientation;
	}
	
	public static void startExploration(){
		if(isRobotSet){
			Thread thread = new Thread(){
				@Override
				public void run() {
					move();
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

		do {
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
			nextMove = moveStrategy.nextMove();
			if (nextMove == Movable.LEFT) {
				moveLeft();
			} else if (nextMove == Movable.RIGHT) {
				moveRight();
			} else if (nextMove == Movable.UP) {
				moveUp();
			} else if (nextMove == Movable.DOWN) {
				moveDown();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		} while (nextMove != Movable.STOP);
	}

	private static void moveLeft() {
		MapManager.moveLeft();
	}

	private static void moveRight() {
		MapManager.moveRight();
	}

	private static void moveUp() {
		MapManager.moveUp();
	}

	private static void moveDown() {
		MapManager.moveDown();
	}

	private static void senseUp(final int RANGE) {
		Hashtable<Integer, Integer> results = MapManager.robotSensing(MapManager.idToX(robotUpLeft),
				MapManager.idToY(robotUpLeft) - RANGE, ROBOT_WIDTH, RANGE);
		moveStrategy.getMapUpdate(results);
	}

	private static void senseDown(final int RANGE) {
		Hashtable<Integer, Integer> results = MapManager.robotSensing(MapManager.idToX(robotUpLeft),
				MapManager.idToY(robotUpLeft) + ROBOT_HEIGHT, ROBOT_WIDTH, RANGE);
		moveStrategy.getMapUpdate(results);
	}

	private static void senseLeft(final int RANGE) {
		Hashtable<Integer, Integer> results = MapManager.robotSensing(MapManager.idToX(robotUpLeft) - RANGE,
				MapManager.idToY(robotUpLeft), RANGE, ROBOT_HEIGHT);
		moveStrategy.getMapUpdate(results);
	}

	private static void senseRight(final int RANGE) {
		Hashtable<Integer, Integer> results = MapManager.robotSensing(MapManager.idToX(robotUpLeft) + ROBOT_WIDTH,
				MapManager.idToY(robotUpLeft), RANGE, ROBOT_HEIGHT);
		moveStrategy.getMapUpdate(results);
	}
}
