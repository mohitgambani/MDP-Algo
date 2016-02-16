package algorithm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.Timer;

import ui.MainControl;
import ui.MapManager;

public class RobotManager {

	public static final int MAP_WIDTH = 20;
	public static final int MAP_HEIGHT = 15;
	
	private static final int ROBOT_WIDTH = 2;
	private static final int ROBOT_HEIGHT = 2;
	
	private static final int FRONT_SENSING_RANGE = 1;
	private static final int SIDE_SENSING_RANGE = 1;

	public static enum ORIENTATION{
		NORTH, SOUTH, EAST, WEST
	}

	private static int positionX, positionY;
	private static Enum<ORIENTATION> orientation;
	private static Movable moveStrategy = null;

	private static Timer timer;
	private static final int TIMER_DELAY = 1;
	private static long startTime;
	private static long timeElapsed;

	public static void setRobot(int posX, int posY, Enum<ORIENTATION> ori) {
		int x, y;
		boolean failed = false;

		for (x = posX; x < posX + ROBOT_WIDTH && !failed; ++x) {
			for (y = posY; y < posY + ROBOT_HEIGHT && !failed; ++y) {
				if (isOutBoundary(x, y) || MapManager.isObstacle(x, y)) {
					failed = true;
				}
			}
		}
		if (!failed) {
			unsetRobot();
			positionX = posX;
			positionY = posY;
			orientation = ori;
			MainControl.mainWindow.setRobotPosition(posX + "," + posY);
		}
	}

	public static void unsetRobot() {
		MapManager.unSetRobot();
	}

	public static int getRobotPositionX() {
		return positionX;
	}

	public static int getRobotPositionY() {
		return positionY;
	}

	public static Enum<ORIENTATION> getRobotOrientation() {
		return orientation;
	}

	public static void setRobotOrientation(Enum<ORIENTATION> ori) {
		orientation = ori;
	}

	public static void startExploration() {
		startTime = System.currentTimeMillis();
		timer = new Timer(TIMER_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeElapsed = System.currentTimeMillis() - startTime;
				MainControl.mainWindow.setTimerDisplay(timeElapsed / 1000 + "s" + timeElapsed % 1000 + "ms");
			}
		});
		
		moveStrategy = new FloodFillMove();
		Thread thread = new Thread() {
			@Override
			public void run() {
				timer.start();
				Enum<Movable.MOVE> nextMove = Movable.MOVE.STOP;
				do {
					if (orientation == ORIENTATION.NORTH || orientation == ORIENTATION.SOUTH) {
						senseNorth();
						senseSouth();
					} else {
						senseWest();
						senseEast();
					}
					nextMove = moveStrategy.nextMove();
					if (nextMove == Movable.MOVE.EAST) {
						moveEast();
					} else if (nextMove == Movable.MOVE.WEST) {
						moveWest();
					} else if (nextMove == Movable.MOVE.NORTH) {
						moveNorth();
					} else if (nextMove == Movable.MOVE.SOUTH) {
						moveSouth();
					} else if( nextMove == Movable.MOVE.TURN_EAST){
						headEast();
					} else if( nextMove == Movable.MOVE.TURN_WEST){
						headWest();
					}else if(nextMove == Movable.MOVE.TURN_NORTH){
						headNorth();
					}else if(nextMove == Movable.MOVE.TURN_SOUTH){
						headSouth();
					}
					System.out.println(nextMove);
				} while (nextMove != Movable.MOVE.STOP);
				timer.stop();
			}
		};
		thread.start();
		
	}

	public static void headWest(){
		MapManager.headWest();
	}
	public static void headEast(){
		MapManager.headEast();
	}
	public static void headNorth(){
		MapManager.headNorth();
	}
	public static void headSouth(){
		MapManager.headSouth();
	}
	
	
	public static void moveWest() {
		unsetRobot();
		setRobot(positionX - 1, positionY, ORIENTATION.WEST);
		MapManager.headWest();
	}

	public static void moveEast() {
		unsetRobot();
		setRobot(positionX + 1, positionY, ORIENTATION.EAST);
		MapManager.headEast();
	}

	public static void moveNorth() {
		unsetRobot();
		setRobot(positionX, positionY - 1, ORIENTATION.NORTH);
		MapManager.headNorth();
	}

	public static void moveSouth() {
		unsetRobot();
		setRobot(positionX, positionY + 1, ORIENTATION.SOUTH);
		MapManager.headSouth();
	}

	public static void senseNorth() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange;
		boolean stop = false;

		if (orientation == ORIENTATION.NORTH || orientation == ORIENTATION.SOUTH) {
			sensingRange = FRONT_SENSING_RANGE;
		} else {
			sensingRange = SIDE_SENSING_RANGE;
		}
		for (x = positionX + ROBOT_WIDTH - 1; x >= positionX; --x) {
			for (y = positionY - 1; y > positionY - 1 - sensingRange && !stop; --y) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					MapManager.setRobotMapObstacle(x, y);
					stop = true;
				} else {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
					MapManager.setRobotMapOpenSpace(x, y);
					MapManager.setRobotMapExplored(x, y);
				}
			}
			stop = false;
		}
		Enumeration<Integer> keys = results.keys();
		while (keys.hasMoreElements()) {
			Integer key = keys.nextElement();
			moveStrategy.getMapUpdate(key, results.get(key));
		}
	}

	public static void senseSouth() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange;
		boolean stop = false;

		if (orientation == ORIENTATION.NORTH || orientation == ORIENTATION.SOUTH) {
			sensingRange = FRONT_SENSING_RANGE;
		} else {
			sensingRange = SIDE_SENSING_RANGE;
		}
		for (x = positionX + ROBOT_WIDTH - 1; x >= positionX; --x) {
			for (y = positionY + ROBOT_HEIGHT; y < positionY + ROBOT_HEIGHT + sensingRange
					&& !stop; ++y) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					MapManager.setRobotMapObstacle(x, y);
					stop = true;
				} else {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
					MapManager.setRobotMapOpenSpace(x, y);
					MapManager.setRobotMapExplored(x, y);
				}
			}
			stop = false;
		}
		Enumeration<Integer> keys = results.keys();
		while (keys.hasMoreElements()) {
			Integer key = keys.nextElement();
			moveStrategy.getMapUpdate(key, results.get(key));
		}
	}

	public static void senseWest() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange;
		boolean stop = false;

		if (orientation == ORIENTATION.NORTH || orientation == ORIENTATION.SOUTH) {
			sensingRange = FRONT_SENSING_RANGE;
		} else {
			sensingRange = SIDE_SENSING_RANGE;
		}
		for (y = positionY + ROBOT_HEIGHT - 1; y >= positionY; --y) {
			for (x = positionX - 1; x > positionX - 1 - sensingRange && !stop; --x) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					MapManager.setRobotMapObstacle(x, y);
					stop = true;
				} else {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
					MapManager.setRobotMapOpenSpace(x, y);
					MapManager.setRobotMapExplored(x, y);
				}
			}
			stop = false;
		}
		Enumeration<Integer> keys = results.keys();
		while (keys.hasMoreElements()) {
			Integer key = keys.nextElement();
			moveStrategy.getMapUpdate(key, results.get(key));
		}
	}

	public static void senseEast() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange;
		boolean stop = false;

		if (orientation == ORIENTATION.NORTH || orientation == ORIENTATION.SOUTH) {
			sensingRange = FRONT_SENSING_RANGE;
		} else {
			sensingRange = SIDE_SENSING_RANGE;
		}
		for (y = positionY + ROBOT_HEIGHT - 1; y >= positionY; --y) {
			for (x = positionX + ROBOT_WIDTH; x < positionX + ROBOT_WIDTH + sensingRange
					&& !stop; ++x) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					MapManager.setRobotMapObstacle(x, y);
					stop = true;
				} else {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
					MapManager.setRobotMapOpenSpace(x, y);
					MapManager.setRobotMapExplored(x, y);
				}
			}
			stop = false;
		}
		Enumeration<Integer> keys = results.keys();
		while (keys.hasMoreElements()) {
			Integer key = keys.nextElement();
			moveStrategy.getMapUpdate(key, results.get(key));
		}
	}

	public static int getRobotWidth() {
		return ROBOT_WIDTH;
	}

	public static int getRobotHeight() {
		return ROBOT_HEIGHT;
	}

	protected static void getExplorationPercentage(double percentage) {
		MainControl.mainWindow.setMapExplored(String.format("Map Explored: %.2f%%", percentage * 100.0));
	}

	private static boolean isOutBoundary(int x, int y) {
		return (x >= MAP_WIDTH) || (x < 0) || (y >= MAP_HEIGHT) || (y < 0);
	}

	protected static int XYToId(int x, int y) {
		return y * MAP_WIDTH + x;
	}
}
