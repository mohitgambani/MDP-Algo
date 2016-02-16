package algorithm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.Timer;

import ui.MainControl;
import ui.MapManager;

public class RobotManager {

	public static final int MAP_WIDTH = 20;
	public static final int MAP_HEIGHT = 15;

	private static Robot robot = new Robot(0, 0, Robot.ORIENTATION.NORTH);

	private static final int START_X = 0;
	private static final int START_Y = 0;
	private static final int END_X = START_X + MapManager.MAP_WIDTH - 1;
	private static final int END_Y = START_Y + MapManager.MAP_HEIGHT - 1;

	private static Timer timer;
	private static final int TIMER_DELAY = 1;
	private static long startTime;
	private static long timeElapsed;

	public static void setRobot(int posX, int posY, Enum<Robot.ORIENTATION> orientation) {
		int x, y;
		boolean failed = false;

		for (x = posX; x < posX + getRobotWidth() && !failed; ++x) {
			for (y = posY; y < posY + getRobotHeight() && !failed; ++y) {
				if (isOutBoundary(x, y) || MapManager.isObstacle(x, y)) {
					failed = true;
				}
			}
		}
		if (!failed) {
			unsetRobot();
			robot.setPositionX(posX);
			robot.setPositionY(posY);
			robot.setOrientation(orientation);
			MainControl.mainWindow.setRobotPosition(posX + "," + posY);
		}
	}

	public static void unsetRobot() {
		MapManager.unSetRobot();
	}

	public static int getRobotPositionX() {
		return robot.getPositionX();
	}

	public static int getRobotPositionY() {
		return robot.getPositionY();
	}

	public static Enum<Robot.ORIENTATION> getRobotOrientation() {
		return robot.getOrientation();
	}

	public static void setRobotOrientation(Enum<Robot.ORIENTATION> orientation) {
		robot.setOrientation(orientation);
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
		timer.start();
		robot.startExploration();
		timer.stop();
	}

	// private static void move() {
	// moveStrategy = new FloodFillMove();
	// Enum<Movable.MOVE> nextMove = Movable.MOVE.STOP;

	// do{
	// nextMove = moveStrategy.nextMove();
	// if(nextMove == Movable.MOVE.EAST){
	// moveEast();
	// }else if(nextMove == Movable.MOVE.WEST){
	// moveWest();
	// }else if(nextMove == Movable.MOVE.NORTH){
	// moveNorth();
	// }else if(nextMove == Movable.MOVE.SOUTH){
	// moveSouth();
	// }
	// pause();
	// }while(nextMove != Movable.MOVE.STOP);

	// int robotPos = robotPosition;
	// int robotPosX = MapManager.idToX(robotPos);
	// int robotPosY = MapManager.idToY(robotPos);
	//
	// if(robotPosX>START_X)
	//
	// {
	// if (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX - 1,
	// robotPosY)) == false
	// || MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX - 1,
	// robotPosY + 1)) == false
	// || ((robotPosX > START_X + 1)
	// && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX - 2,
	// robotPosY)) == false
	// || MapManager.exploredSpaces
	// .contains(MapManager.XYToId(robotPosX - 2, robotPosY + 1)) == false))
	// || ((robotPosY < END_Y) && (MapManager.exploredSpaces
	// .contains(MapManager.XYToId(robotPosX - 1, robotPosY + 2)) == false))) {
	// if (moveLeft()) {
	// pause();
	//
	// move();
	//
	// if (MapManager.exploredSpaces.size() == MapManager.MAP_HEIGHT *
	// MapManager.MAP_WIDTH)
	// return;
	//
	// moveRight();
	//
	// pause();
	// }
	// }
	// } if(robotPosX<END_X-1)
	//
	// {
	// if (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 2,
	// robotPosY)) == false
	// || MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 2,
	// robotPosY + 1)) == false
	// || ((robotPosX < END_X - 2)
	// && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 3,
	// robotPosY)) == false
	// || MapManager.exploredSpaces
	// .contains(MapManager.XYToId(robotPosX + 3, robotPosY + 1)) == false))
	// || ((robotPosY < END_Y) && (MapManager.exploredSpaces
	// .contains(MapManager.XYToId(robotPosX + 2, robotPosY + 2)) == false))) {
	// if (moveRight()) {
	// pause();
	//
	// move();
	//
	// if (MapManager.exploredSpaces.size() == MapManager.MAP_HEIGHT *
	// MapManager.MAP_WIDTH)
	// return;
	//
	// moveLeft();
	//
	// pause();
	// }
	// }
	// } if(robotPosY>START_Y)
	//
	// {
	// if (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX,
	// robotPosY - 1)) == false
	// || MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 1,
	// robotPosY - 1)) == false
	// || ((robotPosY > START_Y + 1)
	// && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX,
	// robotPosY - 2)) == false
	// || MapManager.exploredSpaces
	// .contains(MapManager.XYToId(robotPosX + 1, robotPosY - 2)) == false))
	// || ((robotPosX > START_X) && (MapManager.exploredSpaces
	// .contains(MapManager.XYToId(robotPosX - 1, robotPosY - 1)) == false))
	// || ((robotPosX < END_X) && (MapManager.exploredSpaces
	// .contains(MapManager.XYToId(robotPosX + 2, robotPosY - 1)) == false))) {
	// if (moveUp()) {
	// pause();
	//
	// move();
	//
	// if (MapManager.exploredSpaces.size() == MapManager.MAP_HEIGHT *
	// MapManager.MAP_WIDTH)
	// return;
	//
	// moveDown();
	//
	// pause();
	// }
	// }
	// } if(robotPosY<END_Y-1)
	//
	// {
	// if (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX,
	// robotPosY + 2)) == false
	// || MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX + 1,
	// robotPosY + 2)) == false
	// || ((robotPosY < END_Y - 2)
	// && (MapManager.exploredSpaces.contains(MapManager.XYToId(robotPosX,
	// robotPosY + 3)) == false
	// || MapManager.exploredSpaces
	// .contains(MapManager.XYToId(robotPosX + 1, robotPosY + 3)) == false))) {
	// if (moveDown()) {
	//
	// pause();
	//
	// move();
	//
	// if (MapManager.exploredSpaces.size() == MapManager.MAP_HEIGHT *
	// MapManager.MAP_WIDTH)
	// return;
	//
	// moveUp();
	//
	// pause();
	// }
	// }
	// }
	//
	// }

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
		setRobot(getRobotPositionX() - 1, getRobotPositionY(), Robot.ORIENTATION.WEST);
		MapManager.headWest();
	}

	public static void moveEast() {
		unsetRobot();
		setRobot(getRobotPositionX() + 1, getRobotPositionY(), Robot.ORIENTATION.EAST);
		MapManager.headEast();
	}

	public static void moveNorth() {
		unsetRobot();
		setRobot(getRobotPositionX(), getRobotPositionY() - 1, Robot.ORIENTATION.NORTH);
		MapManager.headNorth();
	}

	public static void moveSouth() {
		unsetRobot();
		setRobot(getRobotPositionX(), getRobotPositionY() + 1, Robot.ORIENTATION.SOUTH);
		MapManager.headSouth();
	}

	public static void senseNorth() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange;
		boolean stop = false;
		Enum<Robot.ORIENTATION> orientation = getRobotOrientation();

		if (orientation == Robot.ORIENTATION.NORTH || orientation == Robot.ORIENTATION.SOUTH) {
			sensingRange = robot.getFrontSensingRange();
		} else {
			sensingRange = robot.getSideSensingRange();
		}
		for (x = getRobotPositionX() + getRobotWidth() - 1; x >= getRobotPositionX(); --x) {
			for (y = getRobotPositionY() - 1; y > getRobotPositionY() - 1 - sensingRange && !stop; --y) {
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
			robot.getMapUpdate(key, results.get(key));
		}
	}

	public static void senseSouth() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange;
		boolean stop = false;
		Enum<Robot.ORIENTATION> orientation = getRobotOrientation();

		if (orientation == Robot.ORIENTATION.NORTH || orientation == Robot.ORIENTATION.SOUTH) {
			sensingRange = robot.getFrontSensingRange();
		} else {
			sensingRange = robot.getSideSensingRange();
		}
		for (x = getRobotPositionX() + getRobotWidth() - 1; x >= getRobotPositionX(); --x) {
			for (y = getRobotPositionY() + getRobotHeight(); y < getRobotPositionY() + getRobotHeight() + sensingRange
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
			robot.getMapUpdate(key, results.get(key));
		}
	}

	public static void senseWest() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange;
		boolean stop = false;
		Enum<Robot.ORIENTATION> orientation = getRobotOrientation();

		if (orientation == Robot.ORIENTATION.NORTH || orientation == Robot.ORIENTATION.SOUTH) {
			sensingRange = robot.getSideSensingRange();
		} else {
			sensingRange = robot.getFrontSensingRange();
		}
		for (y = getRobotPositionY() + getRobotHeight() - 1; y >= getRobotPositionY(); --y) {
			for (x = getRobotPositionX() - 1; x > getRobotPositionX() - 1 - sensingRange && !stop; --x) {
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
			robot.getMapUpdate(key, results.get(key));
		}
	}

	public static void senseEast() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange;
		boolean stop = false;
		Enum<Robot.ORIENTATION> orientation = getRobotOrientation();

		if (orientation == Robot.ORIENTATION.NORTH || orientation == Robot.ORIENTATION.SOUTH) {
			sensingRange = robot.getSideSensingRange();
		} else {
			sensingRange = robot.getFrontSensingRange();
		}
		for (y = getRobotPositionY() + getRobotHeight() - 1; y >= getRobotPositionY(); --y) {
			for (x = getRobotPositionX() + getRobotWidth(); x < getRobotPositionX() + getRobotWidth() + sensingRange
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
			robot.getMapUpdate(key, results.get(key));
		}
	}

	public static int getRobotWidth() {
		return robot.getWidth();
	}

	public static int getRobotHeight() {
		return robot.getHeight();
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
