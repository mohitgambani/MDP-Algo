package algorithm;

import java.util.Hashtable;

import algorithm.Movable.MOVE;
import ui.MapManager;

public abstract class Movable {

	public final int MAP_WIDTH = MapManager.MAP_WIDTH;
	public final int MAP_HEIGHT = MapManager.MAP_HEIGHT;
	public final int ROBOT_WIDTH = RobotManager.ROBOT_WIDTH;
	public final int ROBOT_HEIGHT = RobotManager.ROBOT_HEIGHT;

	private boolean conditionalStop;
	private Hashtable<Integer, GRID_TYPE> mapExplored;

	public Movable() {
		mapExplored = new Hashtable<Integer, GRID_TYPE>();
		conditionalStop = false;
	}

	public enum MOVE {
		NORTH, SOUTH, EAST, WEST, STOP, TURN_NORTH, TURN_SOUTH, TURN_EAST, TURN_WEST, NO_MOVE, NORTH_R, SOUTH_R, EAST_R, WEST_R
	}

	public enum GRID_TYPE {
		OPEN_SPACE, OBSTACLE, WALL
	}

	public abstract MOVE nextMove();

	public abstract int movesToStartZone();
	
	public abstract MOVE peekMove();

	public int idToX(int id) {
		return id % MAP_WIDTH;
	}

	public int idToY(int id) {
		return id / MAP_WIDTH;
	}

	public int XYToId(int x, int y) {
		return y * MAP_WIDTH + x;
	}

	public void getMapUpdate(int id, GRID_TYPE type) {
		mapExplored.put(id, type);
	}

	public Hashtable<Integer, GRID_TYPE> getMapExplored() {
		return mapExplored;
	}

	public void setMapExplored(Hashtable<Integer, GRID_TYPE> map) {
		mapExplored = map;
	}

	public boolean isOutBoundary(int x, int y) {
		return (x >= MAP_WIDTH) || (x < 0) || (y >= MAP_HEIGHT) || (y < 0);
	}

	public int numOfExploredSpace() {
		return mapExplored.size();
	}

	public boolean isConditionalStop() {
		return conditionalStop;
	}

	public void setConditionalStop() {
		conditionalStop = true;
	}

}