package algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;

public class FloodFillMove extends Movable {

	private ArrayList<Integer> mapTraversed;

	private int robotPosX;
	private int robotPosY;
	private Hashtable<Integer, Enum<Movable.GRID_TYPE>> mapExplored;

	private Deque<MOVE> callStack;

	public FloodFillMove() {
		super();
		mapTraversed = new ArrayList<Integer>();
		mapExplored = getMapExplored();
		callStack = new ArrayDeque<MOVE>();
	}

	private Enum<MOVE> backTrack() {
		Enum<MOVE> nextMove = MOVE.STOP;
		
		if (callStack.isEmpty())
			return nextMove;

		Enum<MOVE> lastMove = callStack.removeLast();
		
		if (lastMove == MOVE.EAST) {
			nextMove = MOVE.WEST;
		} else if (lastMove == MOVE.NORTH) {
			nextMove = MOVE.SOUTH;
		} else if (lastMove == MOVE.SOUTH) {
			nextMove = MOVE.NORTH;
		} else if (lastMove == MOVE.WEST) {
			nextMove = MOVE.EAST;
		}
		return nextMove;
	}

	private Enum<MOVE> attemptWest() {

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		int x, y;
		boolean explored = true;
		boolean traversed = true;
		boolean noMove = false;
		ArrayList<Integer> exploredIndex = new ArrayList<Integer>();

		for (x = robotPosX - 1, y = robotPosY; y < robotPosY + RobotManager.getRobotHeight() && explored
				&& !noMove; ++y) {
			if (!isOutBoundary(x, y)) {
				int id = XYToId(x, y);
				if (!mapExplored.containsKey(id)) {
					explored = false;
				} else if (mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE) {
					noMove = true;
				} else {
					exploredIndex.add(id);
				}
			} else {
				noMove = true;
			}
		}
		if (!noMove) {
			if (!explored) {
				return MOVE.TURN_WEST;
			} else {
				for (int index : exploredIndex) {
					if (!mapTraversed.contains(index)) {
						mapTraversed.add(index);
						traversed = false;
					}
				}
				if (!traversed) {
					callStack.add(MOVE.WEST);
					return MOVE.WEST;
				}
			}
		}
		return MOVE.NO_MOVE;
	}

	private Enum<MOVE> attemptEast() {

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		int x, y;
		boolean explored = true;
		boolean traversed = true;

		boolean noMove = false;
		ArrayList<Integer> exploredIndex = new ArrayList<Integer>();

		for (x = robotPosX + RobotManager.getRobotWidth(), y = robotPosY; y < robotPosY + RobotManager.getRobotHeight()
				&& explored && !noMove; ++y) {
			if (!isOutBoundary(x, y)) {
				int id = XYToId(x, y);
				if (!mapExplored.containsKey(id)) {
					explored = false;
				} else if (mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE) {
					noMove = true;
				} else {
					exploredIndex.add(id);
				}
			} else {
				noMove = true;
			}
		}
		if (!noMove) {
			if (!explored) {
				return MOVE.TURN_EAST;
			} else {
				for (int index : exploredIndex) {
					if (!mapTraversed.contains(index)) {
						mapTraversed.add(index);
						traversed = false;
					}
				}
				if (!traversed) {
					callStack.add(MOVE.EAST);
					return MOVE.EAST;
				}
			}
		}
		return MOVE.NO_MOVE;
	}

	private Enum<MOVE> attemptNorth() {

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		int x, y;
		boolean explored = true;
		boolean traversed = true;
		boolean noMove = false;
		ArrayList<Integer> exploredIndex = new ArrayList<Integer>();
		for (y = robotPosY - 1, x = robotPosX; x < robotPosX + RobotManager.getRobotWidth() && explored
				&& !noMove; ++x) {
			if (!isOutBoundary(x, y)) {
				int id = XYToId(x, y);
				if (!mapExplored.containsKey(id)) {
					explored = false;
				} else if (mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE) {
					noMove = true;
				} else {
					exploredIndex.add(id);
				}
			} else {
				noMove = true;
			}
		}
		if (!noMove) {
			if (!explored) {
				return MOVE.TURN_NORTH;
			} else {
				for (int index : exploredIndex) {
					if (!mapTraversed.contains(index)) {
						mapTraversed.add(index);
						traversed = false;
					}
				}
				if (!traversed) {
					callStack.add(MOVE.NORTH);
					return MOVE.NORTH;
				}
			}
		}
		return MOVE.NO_MOVE;
	}

	private Enum<MOVE> attemptSouth() {

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		int x, y;
		boolean explored = true;
		boolean traversed = true;
		boolean noMove = false;
		ArrayList<Integer> exploredIndex = new ArrayList<Integer>();
		for (y = robotPosY + RobotManager.getRobotHeight(), x = robotPosX; x < robotPosX + RobotManager.getRobotWidth()
				&& explored && !noMove; ++x) {
			if (!isOutBoundary(x, y)) {
				int id = XYToId(x, y);
				if (!mapExplored.containsKey(id)) {
					explored = false;
				} else if (mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE) {
					noMove = true;
				} else {
					exploredIndex.add(id);
				}
			} else {
				noMove = true;
			}
		}
		if (!noMove) {
			if (!explored) {
				return MOVE.TURN_SOUTH;
			} else {
				for (int index : exploredIndex) {
					if (!mapTraversed.contains(index)) {
						mapTraversed.add(index);
						traversed = false;
					}
				}
				if (!traversed) {
					callStack.add(MOVE.SOUTH);
					return MOVE.SOUTH;
				}
			}
		}
		return MOVE.NO_MOVE;
	}

	@Override
	public Enum<MOVE> nextMove() {

		Enum<MOVE> nextMove = MOVE.STOP;

		if(isConditionalStop())
			return backTrack();
		
		if (getMapExplored().size() == MAP_HEIGHT * MAP_WIDTH)
			return backTrack();

		nextMove = attemptEast();
		if (nextMove == MOVE.NO_MOVE) {
			nextMove = attemptNorth();
			if (nextMove == MOVE.NO_MOVE) {
				nextMove = attemptSouth();
				if (nextMove == MOVE.NO_MOVE) {
					nextMove = attemptWest();
					if(nextMove == MOVE.NO_MOVE){
						nextMove = backTrack();
					}
				}
			}
		}
		System.out.println(callStack);
		return nextMove;
	}
	
}
