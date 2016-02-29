package ui;

import java.util.Hashtable;
import algorithm.Movable;
import algorithm.RobotManager;

public class SensorManager {
	private static final int FRONT_SENSING_RANGE = 1;
	private static final int SIDE_SENSING_RANGE = 1;

	public static Hashtable<Integer, Movable.GRID_TYPE> senseNorth() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange = 0;
		boolean stop = false;

		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
		case SOUTH:
			sensingRange = FRONT_SENSING_RANGE;
			break;
		case EAST:
		case WEST:
			sensingRange = SIDE_SENSING_RANGE;
		}
		for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH - 1; x >= RobotManager
				.getRobotPositionX(); --x) {
			for (y = RobotManager.getRobotPositionY() - 1; y > RobotManager.getRobotPositionY() - 1 - sensingRange
					&& !stop; --y) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					MapManager.setObstacle(x, y);
					stop = true;
				} else {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
					MapManager.setMapExplored(x, y);
				}
			}
			stop = false;
		}
		return results;
	}

	public static Hashtable<Integer, Movable.GRID_TYPE> senseSouth() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange = 0;
		boolean stop = false;

		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
		case SOUTH:
			sensingRange = FRONT_SENSING_RANGE;
			break;
		case EAST:
		case WEST:
			sensingRange = SIDE_SENSING_RANGE;
		}
		for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH - 1; x >= RobotManager
				.getRobotPositionX(); --x) {
			for (y = RobotManager.getRobotPositionY()
					+ RobotManager.ROBOT_HEIGHT; y < RobotManager.getRobotPositionY()
							+ RobotManager.ROBOT_HEIGHT + sensingRange && !stop; ++y) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					MapManager.setObstacle(x, y);
					stop = true;
				} else {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
					MapManager.setMapExplored(x, y);
				}
			}
			stop = false;
		}
		return results;
	}

	public static Hashtable<Integer, Movable.GRID_TYPE> senseWest() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange = 0;
		boolean stop = false;

		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
		case SOUTH:
			sensingRange = SIDE_SENSING_RANGE;
			break;
		case EAST:
		case WEST:
			sensingRange = FRONT_SENSING_RANGE;
		}
		for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT - 1; y >= RobotManager
				.getRobotPositionY(); --y) {
			for (x = RobotManager.getRobotPositionX() - 1; x > RobotManager.getRobotPositionX() - 1 - sensingRange
					&& !stop; --x) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					MapManager.setObstacle(x, y);
					stop = true;
				} else {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
					MapManager.setMapExplored(x, y);
				}
			}
			stop = false;
		}
		return results;
	}

	public static Hashtable<Integer, Movable.GRID_TYPE> senseEast() {
		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
		int x, y;
		int sensingRange = 0;
		boolean stop = false;

		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
		case SOUTH:
			sensingRange = SIDE_SENSING_RANGE;
			break;
		case EAST:
		case WEST:
			sensingRange = FRONT_SENSING_RANGE;
		}
		for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT - 1; y >= RobotManager
				.getRobotPositionY(); --y) {
			for (x = RobotManager.getRobotPositionX() + RobotManager
					.ROBOT_WIDTH; x < RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH + sensingRange
							&& !stop; ++x) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					MapManager.setObstacle(x, y);
					stop = true;
				} else {
					results.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
					MapManager.setMapExplored(x, y);
				}
			}
			stop = false;
		}
		return results;
	}

	public static boolean isOutBoundary(int x, int y) {
		return (x >= MapManager.MAP_WIDTH) || (x < 0) || (y >= MapManager.MAP_HEIGHT) || (y < 0);
	}

	protected static int XYToId(int x, int y) {
		return y * MapManager.MAP_WIDTH + x;
	}
}
