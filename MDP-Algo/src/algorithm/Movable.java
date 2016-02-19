package algorithm;

import java.util.Hashtable;


public abstract class Movable {
	
	public final int MAP_WIDTH = 20;
	public final int MAP_HEIGHT = 15;
	
	public final int START_X = 0;
	public final int START_Y = 0;
	public final int END_X = START_X + MAP_WIDTH - 1;
	public final int END_Y = START_Y + MAP_HEIGHT - 1;
	
	private boolean conditionalStop;
	private Hashtable<Integer, Enum<GRID_TYPE>> mapExplored;
	
	public Movable(){
		mapExplored = new Hashtable<Integer, Enum<GRID_TYPE>>();
		conditionalStop = false;
	}
	
	public enum MOVE {
		NORTH, SOUTH, EAST, WEST, STOP, TURN_NORTH, TURN_SOUTH, TURN_EAST, TURN_WEST, NO_MOVE
	}
	
	public enum GRID_TYPE {
		OPEN_SPACE, OBSTACLE, WALL
	}
	
	
	public abstract Enum<MOVE> nextMove();
	
	public abstract int movesToStartZone();
	
	/***
	 * Decide when to get sensing information
	 */
	public void sense(){
		RobotManager.sense();
	}

	
	public int idToX(int id) {
		return id % MAP_WIDTH;
	}

	public int idToY(int id) {
		return id / MAP_WIDTH;
	}

	public int XYToId(int x, int y) {
		return y * MAP_WIDTH + x;
	}

	public void getMapUpdate(int id, Enum<GRID_TYPE> type){
		mapExplored.put(id, type);
	}

	public Hashtable<Integer, Enum<GRID_TYPE>> getMapExplored(){
		return mapExplored;
	}
	public boolean isOutBoundary(int x, int y) {
		return (x >= MAP_WIDTH) || (x < 0) || (y >= MAP_HEIGHT) || (y < 0);
	}
	public int numOfExploredSpace(){
		return mapExplored.size();
	}
	public boolean isConditionalStop(){
		return conditionalStop;
	}
	public void setConditionalStop(){
		conditionalStop = true;
	}
	
}