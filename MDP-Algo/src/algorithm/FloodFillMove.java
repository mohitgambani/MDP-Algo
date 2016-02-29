package algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;
import java.util.Stack;


public class FloodFillMove extends Movable {

	private ArrayList<Integer> mapTraversed;

	private int robotPosX = 0;
	private int robotPosY = 0;
	private Hashtable<Integer, Enum<Movable.GRID_TYPE>> mapExplored;

	private Deque<MOVE> callStack;
	
	private static Stack<Enum<MOVE>> listOfBackTrackingMoves = new Stack<Enum<MOVE>>();
	private static int count = 0;

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

	private Enum<MOVE> attemptEast() {
		int x, y;
		boolean traversed = true;
		boolean noMove = false;

		for (x = robotPosX + RobotManager.getRobotWidth(), y = robotPosY; y < robotPosY + RobotManager.getRobotHeight(); ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y) || !mapExplored.containsKey(id)
					|| mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE) {
				noMove = true;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (!noMove && !traversed) {
			callStack.add(MOVE.EAST);
			return MOVE.EAST;
		}
		return MOVE.NO_MOVE;
	}

	private Enum<MOVE> attemptNorth() {
		int x, y;
		boolean traversed = true;
		boolean noMove = false;
		for (y = robotPosY - 1, x = robotPosX; x < robotPosX + RobotManager.getRobotWidth() ; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y) || !mapExplored.containsKey(id)
					|| mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE) {
				noMove = true;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (!noMove && !traversed) {
			callStack.add(MOVE.NORTH);
			return MOVE.NORTH;
		}
		return MOVE.NO_MOVE;
	}

	private Enum<MOVE> attemptSouth() {
		int x, y;
		boolean traversed = true;
		boolean noMove = false;
		for (y = robotPosY + RobotManager.getRobotHeight(), x = robotPosX; x < robotPosX + RobotManager.getRobotWidth(); ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y) || !mapExplored.containsKey(id)
					|| mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE) {
				noMove = true;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (!noMove && !traversed) {
			callStack.add(MOVE.SOUTH);
			return MOVE.SOUTH;
		}
		return MOVE.NO_MOVE;
	}

	private Enum<MOVE> attemptWest() {
		int x, y;
		boolean traversed = true;
		boolean noMove = false;

		for (x = robotPosX - 1, y = robotPosY; y < robotPosY + RobotManager.getRobotHeight(); ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y) || !mapExplored.containsKey(id)
					|| mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE) {
				noMove = true;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (!noMove && !traversed) {
			callStack.add(MOVE.WEST);
			return MOVE.WEST;
		}
		return MOVE.NO_MOVE;
	}

	@Override
	public Enum<MOVE> nextMove() {
		Enum<MOVE> nextMove = MOVE.STOP;
		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();

		if (isConditionalStop()){
			if(count == 0){
				backToStartZone();
				count++;
			}
			if(count == 1){
				nextMove = listOfBackTrackingMoves.pop();
				if(nextMove == Movable.MOVE.STOP)
					count = 0;
				return nextMove;
			}
		}

		if (getMapExplored().size() == MAP_HEIGHT * MAP_WIDTH){
			if(count == 0){
				backToStartZone();
				count++;
			}
			if(count == 1){
				nextMove = listOfBackTrackingMoves.pop();
				if(nextMove == Movable.MOVE.STOP)
					count = 0;
				return nextMove;
			}
		}
			
		sense();
		addRobotToTraversed();
		nextMove = attemptEast();
		if (nextMove == MOVE.NO_MOVE) {
			nextMove = attemptSouth();
			if (nextMove == MOVE.NO_MOVE) {
				nextMove = attemptNorth();
				if (nextMove == MOVE.NO_MOVE) {
					nextMove = attemptWest();
					if (nextMove == MOVE.NO_MOVE) {
						nextMove = backTrack();
					}
				}
			}
		}
		System.out.println(callStack);
		return nextMove;
	}

	private void backToStartZone() {
		ShortestPath moveBack = new ShortestPath(
				XYToId(RobotManager.getRobotPositionX(), RobotManager.getRobotPositionY()),
				0, getMapExplored());
		listOfBackTrackingMoves = moveBack.getListOfMoves();
	}

	@Override
	public int movesToStartZone() {
		return callStack.size();
	}

	private void addRobotToTraversed() {
		int x, y;
		for (x = robotPosX; x < robotPosX + RobotManager.getRobotWidth(); ++x) {
			for (y = robotPosY; y < robotPosY + RobotManager.getRobotHeight(); ++y) {
				if (!mapTraversed.contains(XYToId(x, y))) {
					mapTraversed.add(XYToId(x, y));
				}
			}
		}
	}

}