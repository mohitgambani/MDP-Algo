package algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Stack;

import algorithm.RobotManager.ORIENTATION;



public class FloodFillMove extends Movable {

	private ArrayList<Integer> mapTraversed;
	private Deque<MOVE> callStack;

	private static Stack<MOVE> listOfBackTrackingMoves = new Stack<MOVE>();
	private static int count = 0;

	public FloodFillMove() {
		super();
		mapTraversed = new ArrayList<Integer>();
		callStack = new ArrayDeque<MOVE>();
		addRobotToMapExplored();
	}

	private MOVE backTrack() {
		MOVE nextMove = MOVE.STOP;

		if (callStack.isEmpty())
			return nextMove; 

		Enum<MOVE> lastMove = callStack.removeLast();

		if (lastMove == MOVE.EAST) {
			if(RobotManager.getRobotOrientation() == ORIENTATION.EAST){
				nextMove = MOVE.WEST_R;
			}else{
				nextMove = MOVE.WEST;
			}
		} else if (lastMove == MOVE.NORTH) {
			if(RobotManager.getRobotOrientation() == ORIENTATION.NORTH){
				nextMove = MOVE.SOUTH_R;
			}else{
				nextMove = MOVE.SOUTH;
			}
			
		} else if (lastMove == MOVE.SOUTH) {
			if(RobotManager.getRobotOrientation() == ORIENTATION.SOUTH){
				nextMove = MOVE.NORTH_R;
			}else{
				nextMove = MOVE.NORTH;
			}
		} else if (lastMove == MOVE.WEST) {
			if(RobotManager.getRobotOrientation() == ORIENTATION.WEST){
				nextMove = MOVE.EAST_R;
			}else{
				nextMove = MOVE.EAST;
			}
		}
		return nextMove;
	}

	private MOVE attemptEast() {
		int x, y;
		boolean traversed = true;
		boolean noMove = false;
		boolean explored = true;

		for (x = RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH, y = RobotManager
				.getRobotPositionY(); y < RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y) || isObstacle(id)) {
				noMove = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (!noMove) {
			if (!explored) {
				return MOVE.TURN_EAST;
			} else if (!traversed) {
//				if(RobotManager.getRobotOrientation() == ORIENTATION.EAST){
					callStack.add(MOVE.EAST);
					return MOVE.EAST;
//				}else{
//					return MOVE.TURN_EAST;
//				}
			}
		}
		return MOVE.NO_MOVE;
	}

	private MOVE attemptNorth() {
		int x, y;
		boolean traversed = true;
		boolean noMove = false;
		boolean explored = true;

		for (y = RobotManager.getRobotPositionY() - 1, x = RobotManager
				.getRobotPositionX(); x < RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y) || isObstacle(id)) {
				noMove = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (!noMove) {
			if (!explored) {
				return MOVE.TURN_NORTH;
			} else if (!traversed) {
//				if(RobotManager.getRobotOrientation() == ORIENTATION.NORTH){
					callStack.add(MOVE.NORTH);
					return MOVE.NORTH;
//				}else{
//					return MOVE.TURN_NORTH;
//				}
			}
		}
		return MOVE.NO_MOVE;
	}

	private MOVE attemptSouth() {
		int x, y;
		boolean traversed = true;
		boolean noMove = false;
		boolean explored = true;

		for (y = RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT, x = RobotManager
				.getRobotPositionX(); x < RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y) || isObstacle(id)) {
				noMove = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (!noMove) {
			if (!explored) {
				return MOVE.TURN_SOUTH;
			} else if (!traversed) {
//				if(RobotManager.getRobotOrientation() == ORIENTATION.SOUTH){
					callStack.add(MOVE.SOUTH);
					return MOVE.SOUTH;
//				}else{
//					return MOVE.TURN_SOUTH;
//				}
			}
		}
		return MOVE.NO_MOVE;
	}

	private MOVE attemptWest() {
		int x, y;
		boolean traversed = true;
		boolean noMove = false;
		boolean explored = true;

		for (x = RobotManager.getRobotPositionX() - 1, y = RobotManager
				.getRobotPositionY(); y < RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y) || isObstacle(id)) {
				noMove = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (!noMove) {
			if (!explored) {
				return MOVE.TURN_WEST;
			} else if (!traversed) {
//				if(RobotManager.getRobotOrientation() == ORIENTATION.WEST){
					callStack.add(MOVE.WEST);
					return MOVE.WEST;
//				}else{
//					return MOVE.TURN_WEST;
//				}
			}
		}
		return MOVE.NO_MOVE;
	}

	@Override
	public MOVE nextMove() {
		MOVE nextMove = MOVE.STOP;

		if (isConditionalStop()) {
			if (count == 0) {
				backToStartZone();
				count++;
			}
			if (count == 1) {
				nextMove = listOfBackTrackingMoves.pop();
				if (nextMove == Movable.MOVE.STOP)
					count = 0;
				return nextMove;
			}
		}

		if (getMapExplored().size() == MAP_HEIGHT * MAP_WIDTH) {
			if (count == 0) {
				backToStartZone();
				count++;
			}
			if (count == 1) {
				nextMove = listOfBackTrackingMoves.pop();
				if (nextMove == Movable.MOVE.STOP)
					count = 0;
				return nextMove;
			}
		}

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
//		System.out.println(callStack);
		return nextMove;
	}

	private void backToStartZone() {
		ShortestPath moveBack = new ShortestPath(
				XYToId(RobotManager.getRobotPositionX(), RobotManager.getRobotPositionY()), 0, getMapExplored());
		listOfBackTrackingMoves = moveBack.getListOfMoves();
	}

	@Override
	public int movesToStartZone() {
		return callStack.size();
	}

	private void addRobotToTraversed() {
		int x, y;
		for (x = RobotManager.getRobotPositionX(); x < RobotManager.getRobotPositionX()
				+ RobotManager.ROBOT_WIDTH; ++x) {
			for (y = RobotManager.getRobotPositionY(); y < RobotManager.getRobotPositionY()
					+ RobotManager.ROBOT_HEIGHT; ++y) {
				if (!mapTraversed.contains(XYToId(x, y))) {
					mapTraversed.add(XYToId(x, y));
				}
				
			}
		}
	}

	private void addRobotToMapExplored(){
		int x, y;
		for (x = RobotManager.getRobotPositionX(); x < ROBOT_WIDTH; ++x) {
			for (y = RobotManager.getRobotPositionY(); y < ROBOT_HEIGHT; ++y) {
				getMapUpdate(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
			}
		}
	}
	private boolean isObstacle(int index) {
		return getMapExplored().get(index) == Movable.GRID_TYPE.OBSTACLE ? true : false;
	}

}