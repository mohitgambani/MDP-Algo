package algorithm;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import algorithm.Movable.GRID_TYPE;
import ui.MapManager;

public class SimulatedSensor {

	private static final int RANGE_LIMIT = SensorDecoder.RANGE_LIMIT;

	private static ArrayList<Integer> senseNorth() {
		ArrayList<Integer> results = new ArrayList<Integer>();
		int x, y;
		int sensingRange = RANGE_LIMIT;
		boolean stop = false;

		for (x = RobotManager.getRobotPositionX(); x < RobotManager.getRobotPositionX()
				+ RobotManager.ROBOT_WIDTH; ++x) {
			int reading = 1;
			for (y = RobotManager.getRobotPositionY() - 1; y > RobotManager.getRobotPositionY() - 1 - sensingRange
					&& !stop; --y) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
//					++reading;
					stop = true;
				} else {
					++reading;
				}
			}
			
			stop = false;
			results.add(reading);
		}
		return results;
	}

	private static ArrayList<Integer> senseSouth() {
		ArrayList<Integer> results = new ArrayList<Integer>();
		int x, y;
		int sensingRange = RANGE_LIMIT;
		boolean stop = false;
		
		for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH - 1; x >= RobotManager
				.getRobotPositionX(); --x) {
//			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
			int reading = 1;
			for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT; y < RobotManager.getRobotPositionY()
					+ RobotManager.ROBOT_HEIGHT + sensingRange && !stop; ++y) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
//					++reading;
					stop = true;
				} else {
					++reading;
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
				}
			}
			stop = false;
//			results.add(row);
			results.add(reading);
		}

		return results;
	}

	private static ArrayList<Integer> senseWest() {
		ArrayList<Integer> results = new ArrayList<Integer>();
		int x, y;
		int sensingRange = RANGE_LIMIT;
		boolean stop = false;

	
		for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT - 1; y >= RobotManager
				.getRobotPositionY(); --y) {
//			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
			int reading = 1;
			for (x = RobotManager.getRobotPositionX() - 1; x > RobotManager.getRobotPositionX() - 1 - sensingRange
					&& !stop; --x) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
//					++reading;
					stop = true;
				} else {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
					++reading;
				}
			}
			stop = false;
//			results.add(row);
			results.add(reading);
		}
		return results;
	}

	private static ArrayList<Integer> senseEast() {
		ArrayList<Integer> results = new ArrayList<Integer>();
		int x, y;
		int sensingRange = RANGE_LIMIT;
		boolean stop = false;

		for (y = RobotManager.getRobotPositionY(); y < RobotManager.getRobotPositionY()
				+ RobotManager.ROBOT_HEIGHT; ++y) {
//			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
			int reading = 1;
			for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH; x < RobotManager.getRobotPositionX()
					+ RobotManager.ROBOT_WIDTH + sensingRange && !stop; ++x) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
//					++reading;
					stop = true;
				} else {
					++reading;
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
				}
			}
			stop = false;
			results.add(reading);
//			results.add(row);
		}
		return results;
	}

	// @Override
	public static String getSensoryInfo() {
		ArrayList<Integer> north = senseNorth();
		ArrayList<Integer> south = senseSouth();
		ArrayList<Integer> west = senseWest();
		ArrayList<Integer> east = senseEast();

		String result = "";
		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
			result += west.get(west.size() / 2) + ",";
			// updates.putAll(west.get(west.size() / 2));
			for (Integer row : north)
				// updates.putAll(row);
				result += row + ",";
			// updates.putAll(east.get(east.size() / 2));
			result += east.get(east.size() / 2) + ",";
			break;
		case SOUTH:
			// updates.putAll(east.get(east.size() / 2));
			result += east.get(east.size() / 2) + ",";
			for (Integer row : south)
				// updates.putAll(row);
				result += row + ",";
			// updates.putAll(west.get(west.size() / 2));
			result += west.get(west.size() / 2) + ",";
			break;
		case EAST:
			// updates.putAll(north.get(north.size() / 2));
			result += north.get(north.size() / 2) + ",";
			for (Integer row : east)
				// updates.putAll(row);
				result += row + ",";
			// updates.putAll(south.get(south.size() / 2));
			result += south.get(south.size() / 2) + ",";
			break;
		case WEST:
			// updates.putAll(south.get(south.size() / 2));
			result += south.get(south.size() / 2) + ",";
			for (Integer row : west)
				// updates.putAll(row);
				result += row + ",";
			// updates.putAll(north.get(north.size() / 2));
			result += north.get(north.size() / 2) + ",";
			break;
		}
		// return updates;
		return result;
	}

	private static int convertToSensorReading(Hashtable<Integer, Movable.GRID_TYPE> row) {
		int reading = 1;
//		if(row.isEmpty())
//			return 0;
		System.out.println(row);
		Enumeration<Integer> keys = row.keys();
		while (keys.hasMoreElements()) {
			Integer key = keys.nextElement();
			GRID_TYPE type = row.get(key);
//			System.out.println(key + "," + type);
			if (type == GRID_TYPE.OBSTACLE) {
//				System.out.println("break " + reading);
				break;
			}
			++reading;
		}
		return reading;
	}

	private static boolean isOutBoundary(int x, int y) {
		return (x >= RobotManager.MAP_WIDTH) || (x < 0) || (y >= RobotManager.MAP_HEIGHT) || (y < 0);
	}

	private static int XYToId(int x, int y) {
		return y * RobotManager.MAP_WIDTH + x;
	}
}

//package algorithm;
//
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.Hashtable;
//
//import algorithm.Movable.GRID_TYPE;
//import ui.MapManager;
//
//public class SimulatedSensor {
//
//	private static final int RANGE_LIMIT = SensorDecoder.RANGE_LIMIT;
//
//	private static ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> senseNorth() {
//		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> results = new ArrayList<Hashtable<Integer, Movable.GRID_TYPE>>();
//		int x, y;
//		int sensingRange = RANGE_LIMIT;
//		boolean stop = false;
//
//		// switch (RobotManager.getRobotOrientation()) {
//		// case NORTH:
//		// case SOUTH:
//		// sensingRange = getFrontRange();
//		// break;
//		// case EAST:
//		// case WEST:
//		// sensingRange = getSideRange();
//		// }
//		for (x = RobotManager.getRobotPositionX(); x < RobotManager.getRobotPositionX()
//				+ RobotManager.ROBOT_WIDTH; ++x) {
//			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
//			for (y = RobotManager.getRobotPositionY() - 1; y > RobotManager.getRobotPositionY() - 1 - sensingRange
//					&& !stop; --y) {
//				if (isOutBoundary(x, y)) {
//				} else if (MapManager.isObstacle(x, y)) {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
//					stop = true;
//				} else {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
//				}
//			}
//			stop = false;
//			results.add(row);
//		}
//		return results;
//	}
//
//	private static ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> senseSouth() {
//		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> results = new ArrayList<Hashtable<Integer, Movable.GRID_TYPE>>();
//		int x, y;
//		int sensingRange = RANGE_LIMIT;
//		boolean stop = false;
//
//		// switch (RobotManager.getRobotOrientation()) {
//		// case NORTH:
//		// case SOUTH:
//		// sensingRange = getFrontRange();
//		// break;
//		// case EAST:
//		// case WEST:
//		// sensingRange = getSideRange();
//		// }
//		for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH - 1; x >= RobotManager
//				.getRobotPositionX(); --x) {
//			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
//			for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT; y < RobotManager.getRobotPositionY()
//					+ RobotManager.ROBOT_HEIGHT + sensingRange && !stop; ++y) {
//				if (isOutBoundary(x, y)) {
//				} else if (MapManager.isObstacle(x, y)) {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
//					stop = true;
//				} else {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
//				}
//			}
//			stop = false;
//			results.add(row);
//		}
//
//		return results;
//	}
//
//	private static ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> senseWest() {
//		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> results = new ArrayList<Hashtable<Integer, Movable.GRID_TYPE>>();
//		int x, y;
//		int sensingRange = RANGE_LIMIT;
//		boolean stop = false;
//
//		// switch (RobotManager.getRobotOrientation()) {
//		// case NORTH:
//		// case SOUTH:
//		// sensingRange = getSideRange();
//		// break;
//		// case EAST:
//		// case WEST:
//		// sensingRange = getFrontRange();
//		// }
//		for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT - 1; y >= RobotManager
//				.getRobotPositionY(); --y) {
//			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
//			for (x = RobotManager.getRobotPositionX() - 1; x > RobotManager.getRobotPositionX() - 1 - sensingRange
//					&& !stop; --x) {
//				if (isOutBoundary(x, y)) {
//				} else if (MapManager.isObstacle(x, y)) {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
//					stop = true;
//				} else {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
//				}
//			}
//			stop = false;
//			results.add(row);
//		}
//		return results;
//	}
//
//	private static ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> senseEast() {
//		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> results = new ArrayList<Hashtable<Integer, Movable.GRID_TYPE>>();
//		int x, y;
//		int sensingRange = RANGE_LIMIT;
//		boolean stop = false;
//
//		// switch (RobotManager.getRobotOrientation()) {
//		// case NORTH:
//		// case SOUTH:
//		// sensingRange = getSideRange();
//		// break;
//		// case EAST:
//		// case WEST:
//		// sensingRange = getFrontRange();
//		// }
//		for (y = RobotManager.getRobotPositionY(); y < RobotManager.getRobotPositionY()
//				+ RobotManager.ROBOT_HEIGHT; ++y) {
//			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
//			for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH; x < RobotManager.getRobotPositionX()
//					+ RobotManager.ROBOT_WIDTH + sensingRange && !stop; ++x) {
//				if (isOutBoundary(x, y)) {
//				} else if (MapManager.isObstacle(x, y)) {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
//					stop = true;
//				} else {
//					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
//				}
//			}
//			stop = false;
//			
//			results.add(row);
//		}
//		return results;
//	}
//
//	// @Override
//	public static String getSensoryInfo() {
//		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> north = senseNorth();
//		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> south = senseSouth();
//		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> west = senseWest();
//		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> east = senseEast();
//
//		String result = "";
//		// Hashtable<Integer, Movable.GRID_TYPE> updates = new
//		// Hashtable<Integer, Movable.GRID_TYPE>();
//		switch (RobotManager.getRobotOrientation()) {
//		case NORTH:
//			result += convertToSensorReading(west.get(west.size() / 2)) + ",";
//			// updates.putAll(west.get(west.size() / 2));
//			for (Hashtable<Integer, Movable.GRID_TYPE> row : north)
//				// updates.putAll(row);
//				result += convertToSensorReading(row) + ",";
//			// updates.putAll(east.get(east.size() / 2));
//			result += convertToSensorReading(east.get(east.size() / 2)) + ",";
//			break;
//		case SOUTH:
//			// updates.putAll(east.get(east.size() / 2));
//			result += convertToSensorReading(east.get(east.size() / 2)) + ",";
//			for (Hashtable<Integer, Movable.GRID_TYPE> row : south)
//				// updates.putAll(row);
//				result += convertToSensorReading(row) + ",";
//			// updates.putAll(west.get(west.size() / 2));
//			result += convertToSensorReading(west.get(west.size() / 2)) + ",";
//			break;
//		case EAST:
//			// updates.putAll(north.get(north.size() / 2));
//			result += convertToSensorReading(north.get(north.size() / 2)) + ",";
//			for (Hashtable<Integer, Movable.GRID_TYPE> row : east)
//				// updates.putAll(row);
//				result += convertToSensorReading(row) + ",";
//			// updates.putAll(south.get(south.size() / 2));
//			result += convertToSensorReading(south.get(south.size() / 2)) + ",";
//			break;
//		case WEST:
//			// updates.putAll(south.get(south.size() / 2));
//			result += convertToSensorReading(south.get(south.size() / 2)) + ",";
//			for (Hashtable<Integer, Movable.GRID_TYPE> row : west)
//				// updates.putAll(row);
//				result += convertToSensorReading(row) + ",";
//			// updates.putAll(north.get(north.size() / 2));
//			result += convertToSensorReading(north.get(north.size() / 2)) + ",";
//			break;
//		}
//		// return updates;
//		return result;
//	}
//
//	private static int convertToSensorReading(Hashtable<Integer, Movable.GRID_TYPE> row) {
//		int reading = 1;
////		if(row.isEmpty())
////			return 0;
//		System.out.println(row);
//		Enumeration<Integer> keys = row.keys();
//		while (keys.hasMoreElements()) {
//			Integer key = keys.nextElement();
//			GRID_TYPE type = row.get(key);
////			System.out.println(key + "," + type);
//			if (type == GRID_TYPE.OBSTACLE) {
////				System.out.println("break " + reading);
//				break;
//			}
//			++reading;
//		}
//		return reading;
//	}
//
//	private static boolean isOutBoundary(int x, int y) {
//		return (x >= RobotManager.MAP_WIDTH) || (x < 0) || (y >= RobotManager.MAP_HEIGHT) || (y < 0);
//	}
//
//	private static int XYToId(int x, int y) {
//		return y * RobotManager.MAP_WIDTH + x;
//	}
//}
