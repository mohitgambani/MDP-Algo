package algorithm;

import java.util.ArrayList;
import java.util.Hashtable;

import algorithm.Movable.GRID_TYPE;

public class RealSensor extends Sensor {

	private static final int numOfSensors = 5;
	private static final int RANGE_LIMIT = 4;
	private int[] readings;

	// private boolean readingsReady;

	ArrayList<Integer> leftReadings, rightReadings, frontReadings;

	public RealSensor() {
		super();
		readings = new int[numOfSensors];
		initialiseReadings();
		leftReadings = new ArrayList<Integer>();
		rightReadings = new ArrayList<Integer>();
		frontReadings = new ArrayList<Integer>();
	}

	@Override
	public Hashtable<Integer, GRID_TYPE> getSensoryInfo() {
		Hashtable<Integer, GRID_TYPE> results = new Hashtable<Integer, GRID_TYPE>();
		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
			results.putAll(decodeWest(leftReadings));
			results.putAll(decodeNorth(frontReadings));
			results.putAll(decodeEast(rightReadings));

			break;
		case SOUTH:
			results.putAll(decodeEast(leftReadings));
			results.putAll(decodeSouth(frontReadings));
			results.putAll(decodeWest(rightReadings));
			break;
		case EAST:
			results.putAll(decodeNorth(leftReadings));
			results.putAll(decodeEast(frontReadings));
			results.putAll(decodeSouth(rightReadings));

			break;
		case WEST:
			results.putAll(decodeSouth(leftReadings));
			results.putAll(decodeWest(frontReadings));
			results.putAll(decodeNorth(rightReadings));
			break;
		}
		return results;
	}

	private Hashtable<Integer, GRID_TYPE> decodeNorth(ArrayList<Integer> readings) {
		Hashtable<Integer, GRID_TYPE> results = new Hashtable<Integer, GRID_TYPE>();
		int x, y;
		int counter = 0;
		int reading;
		// for (int reading : readings) {
		for (x = RobotManager.getRobotPositionX(); x < RobotManager.getRobotPositionX()
				+ RobotManager.ROBOT_WIDTH; ++x) {
			reading = readings.get(counter);
			for (y = RobotManager.getRobotPositionY() - Math.min(reading, RANGE_LIMIT); y < RobotManager
					.getRobotPositionY(); ++y) {
				if (!isOutBoundary(x, y)) {
					if (y == RobotManager.getRobotPositionY() - Math.min(reading, RANGE_LIMIT)
							&& reading <= RANGE_LIMIT) {
						results.put(XYToId(x, y), GRID_TYPE.OBSTACLE);
					} else {
						results.put(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
					}
				}
			}
			++counter;
		}
		// }
		return results;
	}

	private Hashtable<Integer, GRID_TYPE> decodeSouth(ArrayList<Integer> readings) {
		Hashtable<Integer, GRID_TYPE> results = new Hashtable<Integer, GRID_TYPE>();
		int x, y;
		int counter = 0;
		int reading;
		// for (int reading : readings) {
		for (x = RobotManager.getRobotPositionX(); x < RobotManager.getRobotPositionX()
				+ RobotManager.ROBOT_WIDTH; ++x) {
			reading = readings.get(counter);
			for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT + Math.min(reading, RANGE_LIMIT)
					- 1; y > RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT - 1; --y) {
				if (!isOutBoundary(x, y)) {
					if (y == RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT
							+ Math.min(reading, RANGE_LIMIT) - 1 && reading <= RANGE_LIMIT) {
						results.put(XYToId(x, y), GRID_TYPE.OBSTACLE);
					} else {
						results.put(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
					}
				}
			}
			++counter;
		}
		// }
		return results;
	}

	private Hashtable<Integer, GRID_TYPE> decodeEast(ArrayList<Integer> readings) {
		Hashtable<Integer, GRID_TYPE> results = new Hashtable<Integer, GRID_TYPE>();
		int x, y;
		int counter = 0;
		int reading;
		// for (int reading : readings) {
		for (y = RobotManager.getRobotPositionY(); y < RobotManager.getRobotPositionY()
				+ RobotManager.ROBOT_HEIGHT; ++y) {
			reading = readings.get(counter);
			for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH + Math.min(reading, RANGE_LIMIT)
					- 1; x > RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH - 1; --x) {
				if (!isOutBoundary(x, y)) {
					if (x == RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH
							+ Math.min(reading, RANGE_LIMIT) - 1 && reading <= RANGE_LIMIT) {
						results.put(XYToId(x, y), GRID_TYPE.OBSTACLE);
					} else {
						results.put(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
					}
				}
			}
			++counter;
		}
		// }
		return results;
	}

	private Hashtable<Integer, GRID_TYPE> decodeWest(ArrayList<Integer> readings) {
		Hashtable<Integer, GRID_TYPE> results = new Hashtable<Integer, GRID_TYPE>();
		int x, y;
		int counter = 0;
		int reading;
		// for (int reading : readings) {
		for (y = RobotManager.getRobotPositionY(); y < RobotManager.getRobotPositionY()
				+ RobotManager.ROBOT_HEIGHT; ++y) {
			reading = readings.get(counter);
			for (x = RobotManager.getRobotPositionX() - Math.min(reading, RANGE_LIMIT); x < RobotManager
					.getRobotPositionX(); ++x) {
				if (!isOutBoundary(x, y)) {
					if (x == RobotManager.getRobotPositionX() - Math.min(reading, RANGE_LIMIT)
							&& reading <= RANGE_LIMIT) {
						results.put(XYToId(x, y), GRID_TYPE.OBSTACLE);
					} else {
						results.put(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
					}
				}
			}
			++counter;
		}
		// }
		return results;
	}

	private void initialiseReadings() {
		for (int reading : readings) {
			reading = -1;
		}
		// readingsReady = false;
	}

	@Override
	public void getReadingsFromExt(String readingStr) {
		readingStr = readingStr.substring(0, readingStr.length() - 1);
		String[] readings = readingStr.split(",");

		clearAll();
		addReadings(leftReadings, Integer.parseInt(readings[0]));
		addReadings(frontReadings, Integer.parseInt(readings[1]), Integer.parseInt(readings[2]),
				Integer.parseInt(readings[3]));
		addReadings(rightReadings, Integer.parseInt(readings[4]));

		RobotManager.extDataReady();
	}

	private void addReadings(ArrayList<Integer> readingArrayList, int reading) {
		readingArrayList.add(0);
		readingArrayList.add(reading);
		readingArrayList.add(0);
	}

	private void addReadings(ArrayList<Integer> readingArrayList, int reading1, int reading2, int reading3) {
		readingArrayList.add(reading1);
		readingArrayList.add(reading2);
		readingArrayList.add(reading3);
	}

	private void clearAll() {
		leftReadings.clear();
		frontReadings.clear();
		rightReadings.clear();
	}

}
