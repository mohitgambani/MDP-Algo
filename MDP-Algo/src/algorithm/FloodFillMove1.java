package algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;
import java.util.Stack;
import algorithm.RobotManager.ORIENTATION;

public class FloodFillMove1 extends Movable {

	
	private ArrayList<Integer> mapTraversed;
	private Deque<MOVE> callStack;
	private int robotPosX;
	private int robotPosY;
	private ORIENTATION robotOri;
	
	private Deque<Integer> robotPositionStack;

	private Hashtable<Integer, GRID_TYPE> mapExplored;
	

	private static Stack<MOVE> listOfBackTrackingMoves = new Stack<MOVE>();
	private static int count = 0;

	public FloodFillMove1() {
		super();
		mapTraversed = new ArrayList<Integer>();
		callStack = new ArrayDeque<MOVE>();
		robotPositionStack = new ArrayDeque<Integer>();
//		addRobotToMapExplored();
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
	
	private MOVE attemptTurnEast() {
		int x,y;
		boolean obstaclePosition[] = {false, false, false};
		
		for (x = robotPosX + RobotManager.ROBOT_WIDTH, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				return MOVE.NO_MOVE;
			else if (!getMapExplored().containsKey(id)) {
				return MOVE.TURN_EAST;
			}
			else if (isObstacle(id)) {
				obstaclePosition[y - robotPosY] = true;
			}
		}
		
		for (x = robotPosX + RobotManager.ROBOT_WIDTH + 1, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				break;
			else if (!getMapExplored().containsKey(id)){
				if(!obstaclePosition[y - robotPosY]){
					return MOVE.TURN_EAST;
				}
			}
		}
		
		return MOVE.NO_MOVE;
	}
	
	private MOVE attemptEast() {
		int x, y;
		boolean obstaclePosition[] = {false, false, false};
		
		for (x = robotPosX + RobotManager.ROBOT_WIDTH, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				return MOVE.NO_MOVE;
			else if (isObstacle(id))
				return MOVE.NO_MOVE;
		}
		
		for (x = robotPosX + RobotManager.ROBOT_WIDTH + 1, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				break;
			else if (isObstacle(id)) {
				obstaclePosition[y - robotPosY] = true;
			}
		}
		
		for (x = robotPosX + RobotManager.ROBOT_WIDTH + 2, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				break;
			else if (!getMapExplored().containsKey(id)){
				if(!obstaclePosition[y - robotPosY]){
					callStack.add(MOVE.EAST);
					return MOVE.EAST;
				}
			}
		}
		
		return MOVE.NO_MOVE;
	}
	
	private MOVE attemptTurnNorth() {
		int x,y;
		boolean obstaclePosition[] = {false, false, false};
		
		for (y = robotPosY - 1, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				return MOVE.NO_MOVE;
			else if (!getMapExplored().containsKey(id)) {
				return MOVE.TURN_NORTH;
			}
			else if (isObstacle(id)) {
				obstaclePosition[x - robotPosX] = true;
			}
		}
		
		for (y = robotPosY - 2, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				break;
			else if (!getMapExplored().containsKey(id)){
				if(!obstaclePosition[x - robotPosX]){
					return MOVE.TURN_NORTH;
				}
			}
		}
		
		return MOVE.NO_MOVE;
	}
	
	private MOVE attemptNorth() {
		int x, y;
		boolean obstaclePosition[] = {false, false, false};
		
		for (y = robotPosY - 1, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				return MOVE.NO_MOVE;
			else if (isObstacle(id))
				return MOVE.NO_MOVE;
		}
		
		for (y = robotPosY - 2, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				break;
			else if (isObstacle(id)) {
				obstaclePosition[x - robotPosX] = true;
			}
		}
		
		for (y = robotPosY - 3, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				break;
			else if (!getMapExplored().containsKey(id)){
				if(!obstaclePosition[x - robotPosX]){
					callStack.add(MOVE.NORTH);
					return MOVE.NORTH;
				}
			}
		}
			
		return MOVE.NO_MOVE;
	}
	
	private MOVE attemptTurnWest() {
		int x,y;
		boolean obstaclePosition[] = {false, false, false};
		
		for (x = robotPosX - 1, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				return MOVE.NO_MOVE;
			else if (!getMapExplored().containsKey(id)) {
				return MOVE.TURN_WEST;
			}
			else if (isObstacle(id)) {
				obstaclePosition[y - robotPosY] = true;
			}
		}
		
		for (x = robotPosX - 2, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				break;
			else if (!getMapExplored().containsKey(id)){
				if(!obstaclePosition[y - robotPosY]){
					return MOVE.TURN_WEST;
				}
			}
		}
		
		return MOVE.NO_MOVE;
	}
	
	private MOVE attemptWest() {
		int x, y;
		boolean obstaclePosition[] = {false, false, false};
		
		for (x = robotPosX - 1, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				return MOVE.NO_MOVE;
			else if (isObstacle(id))
				return MOVE.NO_MOVE;
		}
		
		for (x = robotPosX - 2, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				break;
			else if (isObstacle(id)) {
				obstaclePosition[y - robotPosY] = true;
			}
		}
		
		for (x = robotPosX - 3, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				break;
			else if (!getMapExplored().containsKey(id)){
				if(!obstaclePosition[y - robotPosY]){
					callStack.add(MOVE.WEST);
					return MOVE.WEST;
				}
			}
		}
			
		return MOVE.NO_MOVE;
	}
	
	private MOVE attemptTurnSouth() {
		int x,y;
		boolean obstaclePosition[] = {false, false, false};
		
		for (y = robotPosY + RobotManager.ROBOT_HEIGHT, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				return MOVE.NO_MOVE;
			else if (!getMapExplored().containsKey(id)) {
				return MOVE.TURN_SOUTH;
			}
			else if (isObstacle(id)) {
				obstaclePosition[x - robotPosX] = true;
			}
		}
		
		for (y = robotPosY + RobotManager.ROBOT_HEIGHT + 1, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				break;
			else if (!getMapExplored().containsKey(id)){
				if(!obstaclePosition[x - robotPosX]){
					return MOVE.TURN_SOUTH;
				}
			}
		}
		
		return MOVE.NO_MOVE;
	}
	
	private MOVE attemptSouth() {
		int x, y;
		boolean obstaclePosition[] = {false, false, false};
		
		for (y = robotPosY + RobotManager.ROBOT_HEIGHT, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y))
				return MOVE.NO_MOVE;
			else if (isObstacle(id))
				return MOVE.NO_MOVE;
		}
		
		for (y = robotPosY + RobotManager.ROBOT_HEIGHT + 1, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)){
				break;
			}
			else if (isObstacle(id)) {
				obstaclePosition[x - robotPosX] = true;
			}
		}
		
		for (y = robotPosY + RobotManager.ROBOT_HEIGHT + 2, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)){
				break;
			}
			else if (!getMapExplored().containsKey(id)){
				if(!obstaclePosition[x - robotPosX]){
					callStack.add(MOVE.SOUTH);
					return MOVE.SOUTH;
				}
			}
		}
		
		return MOVE.NO_MOVE;
	}
	
	private MOVE checkDiagonal(){
		int x, y;
		boolean flagNorth = false;
		boolean flagEast = false;
		boolean flagSouth = false;
		boolean flagWest = false;
		int firstDiagonalX = robotPosX - 1;
		int firstDiagonalY = robotPosY - 1;
		int secondDiagonalX = robotPosX + RobotManager.ROBOT_WIDTH;
		int secondDiagonalY = robotPosY - 1;
		int thirdDiagonalX = robotPosX - 1;
		int thirdDiagonalY = robotPosY + RobotManager.ROBOT_HEIGHT;
		int fourthDiagonalX = robotPosX + RobotManager.ROBOT_WIDTH;
		int fourthDiagonalY = robotPosY + RobotManager.ROBOT_HEIGHT;
		int firstDiagonal = XYToId(firstDiagonalX, firstDiagonalY);
		int secondDiagonal = XYToId(secondDiagonalX, secondDiagonalY);
		int thirdDiagonal = XYToId(thirdDiagonalX, thirdDiagonalY);
		int fourthDiagonal = XYToId(fourthDiagonalX, fourthDiagonalY);
		
		for (y = robotPosY - 1, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			int id = XYToId(x, y);
			if (isObstacle(id)){
				flagNorth = true;
				break;
			}
		}
		
		for (y = robotPosY + RobotManager.ROBOT_HEIGHT, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x){
			int id = XYToId(x, y);
			if (isObstacle(id)){
				flagSouth = true;
				break;
			}
		}
		
		for (x = robotPosX - 1, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isObstacle(id)){
				flagWest = true;
				break;
			}
		}
		
		for (x = robotPosX + RobotManager.ROBOT_WIDTH, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isObstacle(id)){
				flagEast = true;
				break;
			}
		}
		
		if (!isOutBoundary(firstDiagonalX, firstDiagonalY) && !getMapExplored().containsKey(firstDiagonal)) {
			if (!flagNorth) {
				callStack.add(MOVE.NORTH);
				return MOVE.NORTH;
			}
			else if (!flagWest) {
				callStack.add(MOVE.WEST);
				return MOVE.WEST;
			}
		}
		
		if (!isOutBoundary(secondDiagonalX, secondDiagonalY) && !getMapExplored().containsKey(secondDiagonal)) {
			if (!flagNorth) {
				callStack.add(MOVE.NORTH);
				return MOVE.NORTH;
			}
			else if (!flagEast) {
				callStack.add(MOVE.EAST);
				return MOVE.EAST;
			}
		}
		
		if (!isOutBoundary(thirdDiagonalX, thirdDiagonalY) && !getMapExplored().containsKey(thirdDiagonal)) {
			if (!flagWest) {
				callStack.add(MOVE.WEST);
				return MOVE.WEST;
			}
			else if (!flagSouth) {
				callStack.add(MOVE.SOUTH);
				return MOVE.SOUTH;
			}
		}
		
		if (!isOutBoundary(fourthDiagonalX, fourthDiagonalY) && !getMapExplored().containsKey(fourthDiagonal)) {
			if (!flagEast) {
				callStack.add(MOVE.EAST);
				return MOVE.EAST;
			}
			else if (!flagSouth) {
				callStack.add(MOVE.SOUTH);
				return MOVE.SOUTH;
			}
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
		if(!listOfBackTrackingMoves.empty()){
			if(listOfBackTrackingMoves.peek() == MOVE.STOP)
				listOfBackTrackingMoves.pop();
			else
				return listOfBackTrackingMoves.pop();
		}
		
		addRobotToTraversed();
		
		nextMove = attemptTurnNorth();
		if (nextMove == MOVE.NO_MOVE) {
			nextMove = attemptNorth();
			if (nextMove == MOVE.NO_MOVE) {
				nextMove = attemptTurnEast();
				if (nextMove == MOVE.NO_MOVE) {
					nextMove = attemptEast();
					if (nextMove == MOVE.NO_MOVE) {
						nextMove = attemptTurnWest();
						if (nextMove == MOVE.NO_MOVE) {
							nextMove = attemptWest();
							if (nextMove == MOVE.NO_MOVE) {
								nextMove = attemptTurnSouth();
								if (nextMove == MOVE.NO_MOVE) {
									nextMove = attemptSouth();
									if (nextMove == MOVE.NO_MOVE) {
										nextMove = checkDiagonal();
										if (nextMove == MOVE.NO_MOVE) {
											nextMove = backTrack();
										}
									}
								}
							}
						}
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
		for (x = robotPosX; x < robotPosX
				+ RobotManager.ROBOT_WIDTH; ++x) {
			for (y = robotPosY; y < robotPosY
					+ RobotManager.ROBOT_HEIGHT; ++y) {
				if (!mapTraversed.contains(XYToId(x, y))) {
					mapTraversed.add(XYToId(x, y));
				}
			}
		}
	}

//	private void addRobotToMapExplored() {
//		int x, y;
//		for (x = RobotManager.getRobotPositionX(); x < ROBOT_WIDTH; ++x) {
//			for (y = RobotManager.getRobotPositionY(); y < ROBOT_HEIGHT; ++y) {
//				getMapUpdate(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
//			}
//		}
//	}

	private boolean isObstacle(int index) {
		return getMapExplored().get(index) == Movable.GRID_TYPE.OBSTACLE ? true : false;
	}


	@Override
	public MOVE peekMove() {
		// TODO Auto-generated method stub
		return null;
	}

}