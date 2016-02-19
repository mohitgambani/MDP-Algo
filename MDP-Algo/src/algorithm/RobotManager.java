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
	
	

	public static enum ORIENTATION {
		NORTH, SOUTH, EAST, WEST
	}

	private static int positionX, positionY;
	private static Enum<ORIENTATION> orientation;
	private static Movable moveStrategy = null;

	private static Timer timer;
	private static final int TIMER_DELAY = 1;
	private static long startTime;
	private static long timeElapsed;
	
	private static long timeLimit = 0;
	private static double percentageLimit = 100.0;
	private static boolean conditionalStop = false;

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

	private static void unsetRobot() {
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

	public static void startExploration() {
		reset();
		timeLimit = MainControl.mainWindow.getTimeLimit();
		System.out.println(timeLimit);
		startTime = System.currentTimeMillis();
		timer = new Timer(TIMER_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeElapsed = System.currentTimeMillis() - startTime;
				if(timeElapsed >= timeLimit || getPercentageExplored() >= percentageLimit)
					conditionalStop = true;
				MainControl.mainWindow.setTimerDisplay(String.format("%d min %d s %d ms",
						timeElapsed / 1000 / 60, timeElapsed / 1000 % 60, timeElapsed % 1000));
			}
		});
		moveStrategy = new FloodFillMove();
		Thread thread = new Thread() {
			@Override
			public void run() {
				timer.start();
				int counter = 0;
				Enum<Movable.MOVE> nextMove = Movable.MOVE.STOP;
				do {
					if(orientation == ORIENTATION.EAST){
						senseEast();
					}else if(orientation == ORIENTATION.NORTH){
						senseNorth();
					}else if(orientation == ORIENTATION.SOUTH){
						senseSouth();
					}else{
						senseWest();
					}
					moveStrategy.setConditionalStop(conditionalStop);
					nextMove = moveStrategy.nextMove();
					if (nextMove == Movable.MOVE.EAST) {
						moveEast();
					} else if (nextMove == Movable.MOVE.WEST) {
						moveWest();
					} else if (nextMove == Movable.MOVE.NORTH) {
						moveNorth();
					} else if (nextMove == Movable.MOVE.SOUTH) {
						moveSouth();
					} else if (nextMove == Movable.MOVE.TURN_EAST) {
						headEast();
					} else if (nextMove == Movable.MOVE.TURN_WEST) {
						headWest();
					} else if (nextMove == Movable.MOVE.TURN_NORTH) {
						headNorth();
					} else if (nextMove == Movable.MOVE.TURN_SOUTH) {
						headSouth();
					}
					++counter;
					displayExplorationPercentage();
					displayMoves(nextMove, counter);
				} while (nextMove != Movable.MOVE.STOP);
				timer.stop();
			}
		};
		thread.start();

	}
	
	public static void startFastestRun() {
		moveStrategy = new ShortestPath();
		Thread thread = new Thread() {
			@Override
			public void run() {
				int counter = 0;
				Enum<Movable.MOVE> nextMove = Movable.MOVE.STOP;
				do {
					nextMove = moveStrategy.nextMove();
					if (nextMove == Movable.MOVE.EAST) {
						moveEast();
					} else if (nextMove == Movable.MOVE.WEST) {
						moveWest();
					} else if (nextMove == Movable.MOVE.NORTH) {
						moveNorth();
					} else if (nextMove == Movable.MOVE.SOUTH) {
						moveSouth();
					} 
					++counter;
					displayMoves(nextMove, counter);
				} while (nextMove != Movable.MOVE.STOP);
			}
		};
		thread.start();
	}

	private static void headWest() {
		orientation = ORIENTATION.WEST;
		MapManager.headWest();
	}

	private static void headEast() {
		orientation = ORIENTATION.EAST;
		MapManager.headEast();
	}

	private static void headNorth() {
		orientation = ORIENTATION.NORTH;
		MapManager.headNorth();
	}

	private static void headSouth() {
		orientation = ORIENTATION.SOUTH;
		MapManager.headSouth();
	}

	private static void moveWest() {
		unsetRobot();
		setRobot(positionX - 1, positionY, ORIENTATION.WEST);
		headWest();
	}

	private static void moveEast() {
		unsetRobot();
		setRobot(positionX + 1, positionY, ORIENTATION.EAST);
		headEast();
	}

	private static void moveNorth() {
		unsetRobot();
		setRobot(positionX, positionY - 1, ORIENTATION.NORTH);
		headNorth();
	}

	private static void moveSouth() {
		unsetRobot();
		setRobot(positionX, positionY + 1, ORIENTATION.SOUTH);
		headSouth();
	}

	private static void senseNorth() {
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

	private static void senseSouth() {
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
			for (y = positionY + ROBOT_HEIGHT; y < positionY + ROBOT_HEIGHT + sensingRange && !stop; ++y) {
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

	private static void senseWest() {
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

	private static void senseEast() {
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
			for (x = positionX + ROBOT_WIDTH; x < positionX + ROBOT_WIDTH + sensingRange && !stop; ++x) {
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

	public static void reset() {
		setRobot(0, 0, RobotManager.ORIENTATION.EAST);
		headEast();
		MainControl.mainWindow.setRobotPosition("unknown");
		MainControl.mainWindow.setTimerDisplay(String.format("%d min %d s %d ms", 0, 0, 0));
		MainControl.mainWindow.setMapExplored(String.format("Map Explored: %.2f%%", 0.0));
		MainControl.mainWindow.clearFreeOutput();
	}
	
	private static void displayMoves(Enum<Movable.MOVE> nextMove, int counter){
		MainControl.mainWindow.setFreeOutput(String.format("%d %s\n", counter, nextMove.toString()));
	}

	public static int getRobotWidth() {
		return ROBOT_WIDTH;
	}

	public static int getRobotHeight() {
		return ROBOT_HEIGHT;
	}

	protected static void displayExplorationPercentage() {
		MainControl.mainWindow.setMapExplored(String.format("Map Explored: %.2f%%", getPercentageExplored()));
	}
	
	private static double getPercentageExplored(){
		return 100.0 * moveStrategy.numOfExploredSpace() / (MAP_WIDTH * MAP_HEIGHT);
	}

	private static boolean isOutBoundary(int x, int y) {
		return (x >= MAP_WIDTH) || (x < 0) || (y >= MAP_HEIGHT) || (y < 0);
	}

	protected static int XYToId(int x, int y) {
		return y * MAP_WIDTH + x;
	}
	
	public static void setTimeLimit(long tLimit){
		timeLimit = tLimit;
	}
	public static void setPercentageLimit(double pLimit){
		percentageLimit = pLimit;
	}
	
	
	
}
