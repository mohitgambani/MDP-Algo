package algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;
import java.util.Stack;

import algorithm.Movable.GRID_TYPE;
import algorithm.RobotManager.ORIENTATION;

public class FloodFillMove extends Movable {

	private ArrayList<Integer> mapTraversed;
	private Deque<MOVE> callStack;
	private Deque<Integer> robotPositionStack;

	private Hashtable<Integer, GRID_TYPE> mapExplored;

	private static Stack<MOVE> listOfBackTrackingMoves = new Stack<MOVE>();
	private static int count = 0;

	public FloodFillMove() {
		super();
		mapTraversed = new ArrayList<Integer>();
		callStack = new ArrayDeque<MOVE>();
		robotPositionStack = new ArrayDeque<Integer>();
		addRobotToMapExplored();
	}

	
	private MOVE backTrack() {
		MOVE nextMove = MOVE.STOP;

		if (callStack.isEmpty())
			return nextMove;

		Enum<MOVE> lastMove = callStack.removeLast();

		if (lastMove == MOVE.EAST) {
			if (RobotManager.getRobotOrientation() == ORIENTATION.EAST) {
				nextMove = MOVE.WEST_R;
			} else {
				nextMove = MOVE.WEST;
			}
		} else if (lastMove == MOVE.NORTH) {
			if (RobotManager.getRobotOrientation() == ORIENTATION.NORTH) {
				nextMove = MOVE.SOUTH_R;
			} else {
				nextMove = MOVE.SOUTH;
			}

		} else if (lastMove == MOVE.SOUTH) {
			if (RobotManager.getRobotOrientation() == ORIENTATION.SOUTH) {
				nextMove = MOVE.NORTH_R;
			} else {
				nextMove = MOVE.NORTH;
			}
		} else if (lastMove == MOVE.WEST) {
			if (RobotManager.getRobotOrientation() == ORIENTATION.WEST) {
				nextMove = MOVE.EAST_R;
			} else {
				nextMove = MOVE.EAST;
			}
		}
		return nextMove;
	}
	
	/*
	private MOVE backTrack() {
		MOVE nextMove = MOVE.STOP;

		if (callStack.isEmpty())
			return nextMove;
		
		boolean flag = false;
		int counter = 0;
		int id = -1;
		
		while(flag != true && !robotPositionStack.isEmpty()){
			id = robotPositionStack.removeLast();
			counter++;
			int x = idToX(id);
			int y = idToY(id);
			int j = y - 1;
			Hashtable<Integer, GRID_TYPE> map = getMapExplored();
			
			for(int i = x; i < x + RobotManager.ROBOT_WIDTH; i++){
					if(isOutBoundary(i, j) || isObstacle(XYToId(i,j))){
						continue;
					}else{
						if(!mapTraversed.contains(XYToId(i,j))){
							flag = true;
							break;
						}
					}				
			}
			
			j = y + ROBOT_HEIGHT;
			
			for(int i = x; i < x + RobotManager.ROBOT_WIDTH; i++){
				if(isOutBoundary(i, j) || isObstacle(XYToId(i,j))){
					continue;
				}else{
					if(!mapTraversed.contains(XYToId(i,j))){
						flag = true;
						break;
					}
				}				
			}
			
			j = x - 1;
			
			for(int i = y; i < y + RobotManager.ROBOT_HEIGHT; i++){
				if(isOutBoundary(j, i) || isObstacle(XYToId(j, i))){
					continue;
				}else{
					if(!mapTraversed.contains(XYToId(j,i))){
						flag = true;
						break;
					}
				}				
			}
			
			j = x + ROBOT_WIDTH;
			
			for(int i = y; i < y + RobotManager.ROBOT_HEIGHT; i++){
				if(isOutBoundary(j, i) || isObstacle(XYToId(j, i))){
					continue;
				}else{
					if(!mapTraversed.contains(XYToId(j,i))){
						flag = true;
						break;
					}
				}				
			}
			
		}
		
		
		if(flag == true){
			if(counter <= 5){
				for(int i = 1; i <= counter; i++){
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
					listOfBackTrackingMoves.push(nextMove);
					if(i == counter){
						nextMove = listOfBackTrackingMoves.pop();
					}
				}
			} 
			else{
				for(int i = 1; i <= counter; i++)
					callStack.removeLast();
				ShortestPath moveBack = new ShortestPath(
						XYToId(RobotManager.getRobotPositionX(), RobotManager.getRobotPositionY()),
						id, getMapExplored());
				listOfBackTrackingMoves = moveBack.getListOfMoves();
				nextMove = listOfBackTrackingMoves.pop();
			}
			
		}
		
		
		return nextMove;
	}*/

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
		if (!explored) {
			return MOVE.TURN_EAST;
		}
		if (!noMove) {
			if (!traversed) {
				callStack.add(MOVE.EAST);
				return MOVE.EAST;
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
		if (!explored) {
			return MOVE.TURN_NORTH;
		}
		if (!noMove) {
			if (!traversed) {
				callStack.add(MOVE.NORTH);
				return MOVE.NORTH;
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
		if (!explored) {
			return MOVE.TURN_SOUTH;
		}
		if (!noMove) {
			if (!traversed) {
				callStack.add(MOVE.SOUTH);
				return MOVE.SOUTH;
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
		if (!explored) {
			return MOVE.TURN_WEST;
		}
		if (!noMove) {
			if (!traversed) {
				callStack.add(MOVE.WEST);
				return MOVE.WEST;
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
		if(!listOfBackTrackingMoves.empty()){
			if(listOfBackTrackingMoves.peek() == MOVE.STOP)
				listOfBackTrackingMoves.pop();
			else
				return listOfBackTrackingMoves.pop();
		}
		
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

	private void addRobotToMapExplored() {
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