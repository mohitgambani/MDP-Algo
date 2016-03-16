package algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;
import java.util.Stack;
import algorithm.RobotManager.ORIENTATION;
import ui.MapManager;

public class DoubleLMove extends Movable {

	private ArrayList<Integer> mapTraversed;
	private Deque<MOVE> callStack;
	private int robotPosX;
	private int robotPosY;
	private ORIENTATION robotOri;

	private Deque<Integer> robotPositionStack;

	private Hashtable<Integer, GRID_TYPE> mapExplored;

	private MOVE eastDecision;
	private MOVE westDecision;
	private MOVE northDecision;
	private MOVE southDecision;

	private static Stack<MOVE> listOfBackTrackingMoves = new Stack<MOVE>();
	private static int count = 0;

	public DoubleLMove() {
		super();
		mapTraversed = new ArrayList<Integer>();
		callStack = new ArrayDeque<MOVE>();
		robotPositionStack = new ArrayDeque<Integer>();
	}

	private MOVE backTrack() {
		MOVE nextMove = MOVE.STOP;

		if (callStack.isEmpty())
			return nextMove;

		Enum<MOVE> lastMove = callStack.removeLast();

		if (lastMove == MOVE.EAST) {
			if (robotOri == ORIENTATION.EAST) {
				nextMove = MOVE.WEST_R;
			} else {
				nextMove = MOVE.WEST;
			}
		} else if (lastMove == MOVE.NORTH) {
			if (robotOri == ORIENTATION.NORTH) {
				nextMove = MOVE.SOUTH_R;
			} else {
				nextMove = MOVE.SOUTH;
			}

		} else if (lastMove == MOVE.SOUTH) {
			if (robotOri == ORIENTATION.SOUTH) {
				nextMove = MOVE.NORTH_R;
			} else {
				nextMove = MOVE.NORTH;
			}
		} else if (lastMove == MOVE.WEST) {
			if (robotOri == ORIENTATION.WEST) {
				nextMove = MOVE.EAST_R;
			} else {
				nextMove = MOVE.EAST;
			}
		}
		return nextMove;
	}

	/*
	 * private MOVE backTrack() { MOVE nextMove = MOVE.STOP;
	 * 
	 * if (callStack.isEmpty()) return nextMove;
	 * 
	 * boolean flag = false; int counter = 0; int id = -1;
	 * 
	 * while(flag != true && !robotPositionStack.isEmpty()){ id =
	 * robotPositionStack.removeLast(); counter++; int x = idToX(id); int y =
	 * idToY(id); int j = y - 1; Hashtable<Integer, GRID_TYPE> map =
	 * getMapExplored();
	 * 
	 * for(int i = x; i < x + RobotManager.ROBOT_WIDTH; i++){
	 * if(isOutBoundary(i, j) || isObstacle(XYToId(i,j))){ continue; }else{
	 * if(!mapTraversed.contains(XYToId(i,j))){ flag = true; break; } } }
	 * 
	 * j = y + ROBOT_HEIGHT;
	 * 
	 * for(int i = x; i < x + RobotManager.ROBOT_WIDTH; i++){
	 * if(isOutBoundary(i, j) || isObstacle(XYToId(i,j))){ continue; }else{
	 * if(!mapTraversed.contains(XYToId(i,j))){ flag = true; break; } } }
	 * 
	 * j = x - 1;
	 * 
	 * for(int i = y; i < y + RobotManager.ROBOT_HEIGHT; i++){
	 * if(isOutBoundary(j, i) || isObstacle(XYToId(j, i))){ continue; }else{
	 * if(!mapTraversed.contains(XYToId(j,i))){ flag = true; break; } } }
	 * 
	 * j = x + ROBOT_WIDTH;
	 * 
	 * for(int i = y; i < y + RobotManager.ROBOT_HEIGHT; i++){
	 * if(isOutBoundary(j, i) || isObstacle(XYToId(j, i))){ continue; }else{
	 * if(!mapTraversed.contains(XYToId(j,i))){ flag = true; break; } } }
	 * 
	 * }
	 * 
	 * 
	 * if(flag == true){ if(counter <= 5){ for(int i = 1; i <= counter; i++){
	 * Enum<MOVE> lastMove = callStack.removeLast(); if (lastMove == MOVE.EAST)
	 * { if(RobotManager.getRobotOrientation() == ORIENTATION.EAST){ nextMove =
	 * MOVE.WEST_R; }else{ nextMove = MOVE.WEST; } } else if (lastMove ==
	 * MOVE.NORTH) { if(RobotManager.getRobotOrientation() ==
	 * ORIENTATION.NORTH){ nextMove = MOVE.SOUTH_R; }else{ nextMove =
	 * MOVE.SOUTH; }
	 * 
	 * } else if (lastMove == MOVE.SOUTH) {
	 * if(RobotManager.getRobotOrientation() == ORIENTATION.SOUTH){ nextMove =
	 * MOVE.NORTH_R; }else{ nextMove = MOVE.NORTH; } } else if (lastMove ==
	 * MOVE.WEST) { if(RobotManager.getRobotOrientation() == ORIENTATION.WEST){
	 * nextMove = MOVE.EAST_R; }else{ nextMove = MOVE.EAST; } }
	 * listOfBackTrackingMoves.push(nextMove); if(i == counter){ nextMove =
	 * listOfBackTrackingMoves.pop(); } } } else{ for(int i = 1; i <= counter;
	 * i++) callStack.removeLast(); ShortestPath moveBack = new ShortestPath(
	 * XYToId(RobotManager.getRobotPositionX(),
	 * RobotManager.getRobotPositionY()), id, getMapExplored());
	 * listOfBackTrackingMoves = moveBack.getListOfMoves(); nextMove =
	 * listOfBackTrackingMoves.pop(); }
	 * 
	 * }
	 * 
	 * 
	 * return nextMove; }
	 */

	private MOVE attemptEast() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;

		for (x = robotPosX + RobotManager.ROBOT_WIDTH, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT
				&& !outBoundary; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (outBoundary) {
			return MOVE.NO_MOVE;
		}
		if (obstacle) {
			if (explored) {
				return MOVE.NO_MOVE;
			} else {
				return MOVE.TURN_EAST;
			}
		}
		if (!explored) {
			return MOVE.TURN_EAST_M;
		}
		if (!traversed) {
			return MOVE.EAST;
		}
		return MOVE.NO_MOVE;
	}

	private MOVE attemptNorth() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;

		for (y = robotPosY - 1, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH && !outBoundary; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}

		if (outBoundary) {
			return MOVE.NO_MOVE;
		}
		if (obstacle) {
			if (explored) {
				return MOVE.NO_MOVE;
			} else {
				return MOVE.TURN_NORTH;
			}
		}
		if (!explored) {

			return MOVE.TURN_NORTH_M;

		}
		if (!traversed) {
			return MOVE.NORTH;
		}
		return MOVE.NO_MOVE;
	}

	private MOVE attemptSouth() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;

		for (y = robotPosY + RobotManager.ROBOT_HEIGHT, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH
				&& !outBoundary; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (outBoundary) {
			return MOVE.NO_MOVE;
		}
		if (obstacle) {
			if (explored) {
				return MOVE.NO_MOVE;
			} else {
				return MOVE.TURN_SOUTH;
			}
		}
		if (!explored) {

			return MOVE.TURN_SOUTH_M;

		}
		if (!traversed) {
			return MOVE.SOUTH;
		}
		return MOVE.NO_MOVE;
	}

	private MOVE attemptWest() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;

		for (x = robotPosX - 1, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT && !outBoundary; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
			} else if (!mapTraversed.contains(id)) {
				traversed = false;
			}
		}
		if (outBoundary) {
			return MOVE.NO_MOVE;
		}
		if (obstacle) {
			if (explored) {
				return MOVE.NO_MOVE;
			} else {
				return MOVE.TURN_WEST;
			}
		}
		if (!explored) {
			return MOVE.TURN_WEST_M;
		}
		if (!traversed) {
			return MOVE.WEST;
		}
		return MOVE.NO_MOVE;
	}

	@Override
	public MOVE nextMove() {
		MOVE nextMove = MOVE.STOP;

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		robotOri = RobotManager.getRobotOrientation();

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
		if (!listOfBackTrackingMoves.empty()) {
			if (listOfBackTrackingMoves.peek() == MOVE.STOP)
				listOfBackTrackingMoves.pop();
			else
				return listOfBackTrackingMoves.pop();
		}

		addRobotToTraversed();

		eastDecision = attemptEast();
		westDecision = attemptWest();
		northDecision = attemptNorth();
		southDecision = attemptSouth();

		if (!isGoalZoneTraversed()) {
			if (northDecision == MOVE.NORTH) {
				callStack.add(northDecision);
				return northDecision;
			} else if (northDecision == MOVE.TURN_NORTH_M) {
				return northDecision;
			} else if (eastDecision == MOVE.EAST) {
				callStack.add(eastDecision);
				return eastDecision;
			} else if (eastDecision == MOVE.TURN_EAST_M) {
				return eastDecision;
			} else if (southDecision == MOVE.SOUTH) {
				callStack.add(southDecision);
				return southDecision;
			} else if (southDecision == MOVE.TURN_SOUTH_M) {
				return southDecision;
			} else if (westDecision == MOVE.WEST) {
				callStack.add(westDecision);
				return westDecision;
			} else if (westDecision == MOVE.TURN_WEST_M) {
				return westDecision;
			}

			if (northDecision == MOVE.TURN_NORTH) {
				return northDecision;
			}
			if (eastDecision == MOVE.TURN_EAST) {
				return eastDecision;
			}
			if (southDecision == MOVE.TURN_SOUTH) {
				return southDecision;
			}
			if (westDecision == MOVE.TURN_WEST) {
				return westDecision;
			}

			return backTrack();

		} else {
			if (southDecision == MOVE.SOUTH) {
				callStack.add(southDecision);
				return southDecision;
			} else if (southDecision == MOVE.TURN_SOUTH_M) {
				return southDecision;
			} else if (westDecision == MOVE.WEST) {
				callStack.add(westDecision);
				return westDecision;
			} else if (westDecision == MOVE.TURN_WEST_M) {
				return westDecision;
			} else if (northDecision == MOVE.NORTH) {
				callStack.add(northDecision);
				return northDecision;
			} else if (northDecision == MOVE.TURN_NORTH_M) {
				return northDecision;
			} else if (eastDecision == MOVE.EAST) {
				callStack.add(eastDecision);
				return eastDecision;
			} else if (eastDecision == MOVE.TURN_EAST_M) {
				return eastDecision;
			}
			if (southDecision == MOVE.TURN_SOUTH) {
				return southDecision;
			} else if (westDecision == MOVE.TURN_WEST) {
				return westDecision;
			} else if (northDecision == MOVE.TURN_NORTH) {
				return northDecision;
			} else if (eastDecision == MOVE.TURN_EAST) {
				return eastDecision;
			}

			return backTrack();
		}
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
		for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
				if (!mapTraversed.contains(XYToId(x, y))) {
					mapTraversed.add(XYToId(x, y));
				}
			}
		}
	}

	private boolean isObstacle(int index) {
		return getMapExplored().get(index) == Movable.GRID_TYPE.OBSTACLE ? true : false;
	}

	@Override
	public MOVE peekMove() {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean isGoalZoneTraversed() {
		boolean traversed = true;
		int x, y;
		for (x = RobotManager.MAP_WIDTH - 1; x >= RobotManager.MAP_WIDTH - MapManager.GOAL_ZONE_WIDTH; --x) {
			for (y = RobotManager.MAP_HEIGHT - 1; y >= RobotManager.MAP_HEIGHT - MapManager.GOAL_ZONE_HEIGHT; --y) {
				if (!mapTraversed.contains(XYToId(x, y))) {
					traversed = false;
				}
			}
		}
		return traversed;
	}
}