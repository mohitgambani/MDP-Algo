package algorithm;

import java.util.ArrayList;
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
				if (RobotManager.isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
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
			int reading = 1;
			for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT; y < RobotManager.getRobotPositionY()
					+ RobotManager.ROBOT_HEIGHT + sensingRange && !stop; ++y) {
				if (RobotManager.isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
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

	private static ArrayList<Integer> senseWest() {
		ArrayList<Integer> results = new ArrayList<Integer>();
		int x, y;
		int sensingRange = RANGE_LIMIT;
		boolean stop = false;

		for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT - 1; y >= RobotManager
				.getRobotPositionY(); --y) {
			int reading = 1;
			for (x = RobotManager.getRobotPositionX() - 1; x > RobotManager.getRobotPositionX() - 1 - sensingRange
					&& !stop; --x) {
				if (RobotManager.isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
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

	private static ArrayList<Integer> senseEast() {
		ArrayList<Integer> results = new ArrayList<Integer>();
		int x, y;
		int sensingRange = RANGE_LIMIT;
		boolean stop = false;

		for (y = RobotManager.getRobotPositionY(); y < RobotManager.getRobotPositionY()
				+ RobotManager.ROBOT_HEIGHT; ++y) {
			int reading = 1;
			for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH; x < RobotManager.getRobotPositionX()
					+ RobotManager.ROBOT_WIDTH + sensingRange && !stop; ++x) {
				if (RobotManager.isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
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

	public static String getSensoryInfo() {
		ArrayList<Integer> north = senseNorth();
		ArrayList<Integer> south = senseSouth();
		ArrayList<Integer> west = senseWest();
		ArrayList<Integer> east = senseEast();

		String result = "";
		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
			result += west.get(west.size() / 2) + ",";
			for (Integer row : north)
				result += row + ",";
			result += east.get(east.size() / 2) + ",";
			break;
		case SOUTH:
			result += east.get(east.size() / 2) + ",";
			for (Integer row : south)
				result += row + ",";
			result += west.get(west.size() / 2) + ",";
			break;
		case EAST:
			result += north.get(north.size() / 2) + ",";
			for (Integer row : east)
				result += row + ",";
			result += south.get(south.size() / 2) + ",";
			break;
		case WEST:
			result += south.get(south.size() / 2) + ",";
			for (Integer row : west)
				result += row + ",";
			result += north.get(north.size() / 2) + ",";
			break;
		}
		return result;
	}
}