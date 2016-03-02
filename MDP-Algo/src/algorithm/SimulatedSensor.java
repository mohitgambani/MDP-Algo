package algorithm;

import java.util.ArrayList;
import java.util.Hashtable;


import algorithm.Movable.GRID_TYPE;
import ui.MapManager;

public class SimulatedSensor extends Sensor{
	private ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> senseNorth() {
		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> results = new ArrayList<Hashtable<Integer, Movable.GRID_TYPE>>();
		int x, y;
		int sensingRange = 0;
		boolean stop = false;

		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
		case SOUTH:
			sensingRange = getFrontRange();
			break;
		case EAST:
		case WEST:
			sensingRange = getSideRange();
		}
		for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH - 1; x >= RobotManager
				.getRobotPositionX(); --x) {
			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
			for (y = RobotManager.getRobotPositionY() - 1; y > RobotManager.getRobotPositionY() - 1 - sensingRange
					&& !stop; --y) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					stop = true;
				} else {
					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
				}
			}
			stop = false;
			results.add(row);
		}
		return results;
	}

	private ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> senseSouth() {
		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> results = new ArrayList<Hashtable<Integer, Movable.GRID_TYPE>>();
		int x, y;
		int sensingRange = 0;
		boolean stop = false;

		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
		case SOUTH:
			sensingRange = getFrontRange();
			break;
		case EAST:
		case WEST:
			sensingRange = getSideRange();
		}
		for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH - 1; x >= RobotManager
				.getRobotPositionX(); --x) {
			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
			for (y = RobotManager.getRobotPositionY()
					+ RobotManager.ROBOT_HEIGHT; y < RobotManager.getRobotPositionY()
							+ RobotManager.ROBOT_HEIGHT + sensingRange && !stop; ++y) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					stop = true;
				} else {
					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
				}
			}
			stop = false;
			results.add(row);
		}
		
		return results;
	}

	private ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> senseWest() {
		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> results = new ArrayList<Hashtable<Integer, Movable.GRID_TYPE>>();
		int x, y;
		int sensingRange = 0;
		boolean stop = false;

		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
		case SOUTH:
			sensingRange = getSideRange();
			break;
		case EAST:
		case WEST:
			sensingRange = getFrontRange();
		}
		for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT - 1; y >= RobotManager
				.getRobotPositionY(); --y) {
			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
			for (x = RobotManager.getRobotPositionX() - 1; x > RobotManager.getRobotPositionX() - 1 - sensingRange
					&& !stop; --x) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					stop = true;
				} else {
					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
				}
			}
			stop = false;
			results.add(row);
		}
		return results;
	}

	private ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> senseEast() {
		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> results = new ArrayList<Hashtable<Integer, Movable.GRID_TYPE>>();
		int x, y;
		int sensingRange = 0;
		boolean stop = false;

		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
		case SOUTH:
			sensingRange = getSideRange();
			break;
		case EAST:
		case WEST:
			sensingRange = getFrontRange();
		}
		for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT - 1; y >= RobotManager
				.getRobotPositionY(); --y) {
			Hashtable<Integer, Movable.GRID_TYPE> row = new Hashtable<Integer, Movable.GRID_TYPE>();
			for (x = RobotManager.getRobotPositionX() + RobotManager
					.ROBOT_WIDTH; x < RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH + sensingRange
							&& !stop; ++x) {
				if (isOutBoundary(x, y)) {
				} else if (MapManager.isObstacle(x, y)) {
					row.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
					stop = true;
				} else {
					row.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
				}
			}
			stop = false;
			results.add(row);
		}
		return results;
	}

	@Override
	public Hashtable<Integer, GRID_TYPE> getSensoryInfo() {
		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> north = senseNorth();
		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> south = senseSouth();
		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> west = senseWest();
		ArrayList<Hashtable<Integer, Movable.GRID_TYPE>> east = senseEast();
		Hashtable<Integer, Movable.GRID_TYPE> updates = new Hashtable<Integer, Movable.GRID_TYPE>();
		switch (RobotManager.getRobotOrientation()) {
		case NORTH:
			for(Hashtable<Integer, Movable.GRID_TYPE> row : north)
				updates.putAll(row);
			updates.putAll(west.get(west.size() / 2));
			updates.putAll(east.get(east.size() / 2));
			break;
		case SOUTH:
			for(Hashtable<Integer, Movable.GRID_TYPE> row : south)
				updates.putAll(row);
			updates.putAll(west.get(west.size() / 2));
			updates.putAll(east.get(east.size() / 2));
			break;
		case EAST:
			for(Hashtable<Integer, Movable.GRID_TYPE> row : east)
				updates.putAll(row);
			updates.putAll(north.get(north.size() / 2));
			updates.putAll(south.get(south.size() / 2));
			break;
		case WEST:
			for(Hashtable<Integer, Movable.GRID_TYPE> row : west)
				updates.putAll(row);
			updates.putAll(north.get(north.size() / 2));
			updates.putAll(south.get(south.size() / 2));
			break;
		}
		return updates;
	}
	@Override
	public void getReadingsFromExt(String readingStr) {
		// TODO Auto-generated method stub
		
	}
}
