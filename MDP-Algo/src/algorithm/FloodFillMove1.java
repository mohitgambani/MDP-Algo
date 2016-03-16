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

	
	private MOVE backTrack1() {
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
	
	private MOVE backtrack() {
		MOVE nextMove = MOVE.STOP;
		
		if (callStack.isEmpty())
			return nextMove;
		
		boolean flag = false;
		int counter = 0;
		int idToCheck = -1;
		
		while(flag != true && !robotPositionStack.isEmpty()){
			idToCheck = robotPositionStack.removeLast();
			int robotX = idToX(idToCheck);
			int robotY = idToY(idToCheck);
			counter++;
			int x = idToX(idToCheck);
			int y = idToY(idToCheck);
			boolean obstaclePosition[] = {false, false, false};
			boolean obstacle1Position[] = {false, false, false};
			Hashtable<Integer, GRID_TYPE> map = getMapExplored();
			
			for (x = robotX + RobotManager.ROBOT_WIDTH, y = robotY; y < robotY + RobotManager.ROBOT_HEIGHT; ++y) {
				int id = XYToId(x, y);
				if (isOutBoundary(x,y))
					break;
				else if (!getMapExplored().containsKey(id)) {
					flag = true;
					break;
				}
				else if (isObstacle(id)) {
					obstaclePosition[y - robotY] = true;
				}
			}
			
			for (x = robotX + RobotManager.ROBOT_WIDTH + 1, y = robotY; y < robotY + RobotManager.ROBOT_HEIGHT; ++y) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y))
					break;
				else if (!getMapExplored().containsKey(id)){
					if(!obstaclePosition[y - robotY]){
						flag = true;
						break;
					}
				}
				else if (isObstacle(id)) {
					obstacle1Position[y - robotY] = true;
				}
			}
			
			for (x = robotX + RobotManager.ROBOT_WIDTH + 2, y = robotY; y < robotY + RobotManager.ROBOT_HEIGHT; ++y) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y))
					break;
				else if (!getMapExplored().containsKey(id)){
					if(!obstacle1Position[y - robotY]){
						flag = true;
						break;
					}
				}
			}
			
			obstaclePosition[0] = obstaclePosition[1] = obstaclePosition[2] = false;
			obstacle1Position[0] = obstacle1Position[1] = obstacle1Position[2] = false;
			
			for (y = robotY - 1, x = robotX; x < robotX + RobotManager.ROBOT_WIDTH; ++x) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y))
					break;
				else if (!getMapExplored().containsKey(id)) {
					flag = true;
					break;
				}
				else if (isObstacle(id)) {
					obstaclePosition[x - robotX] = true;
				}
			}
			
			for (y = robotY - 2, x = robotX; x < robotX + RobotManager.ROBOT_WIDTH; ++x) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y))
					break;
				else if (!getMapExplored().containsKey(id)){
					if(!obstaclePosition[x - robotX]){
						flag = true;
						break;
					}
				}
				else if (isObstacle(id)) {
					obstacle1Position[x - robotX] = true;
				}
			}
			
			for (y = robotY - 3, x = robotX; x < robotX + RobotManager.ROBOT_WIDTH; ++x) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y))
					break;
				else if (!getMapExplored().containsKey(id)){
					if(!obstacle1Position[x - robotX]){
						flag = true;
						break;
					}
				}
			}
			
			obstaclePosition[0] = obstaclePosition[1] = obstaclePosition[2] = false;
			obstacle1Position[0] = obstacle1Position[1] = obstacle1Position[2] = false;
			
			for (x = robotX - 1, y = robotY; y < robotY + RobotManager.ROBOT_HEIGHT; ++y) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y))
					break;
				else if (!getMapExplored().containsKey(id)) {
					flag = true;
					break;
				}
				else if (isObstacle(id)) {
					obstaclePosition[y - robotY] = true;
				}
			}
			
			for (x = robotX - 2, y = robotY; y < robotY + RobotManager.ROBOT_HEIGHT; ++y) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y))
					break;
				else if (!getMapExplored().containsKey(id)){
					if(!obstaclePosition[y - robotY]){
						flag = true;
						break;
					}
				}
				else if (isObstacle(id)) {
					obstacle1Position[y - robotY] = true;
				}
			}
			
			for (x = robotX - 3, y = robotY; y < robotY + RobotManager.ROBOT_HEIGHT; ++y) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y))
					break;
				else if (!getMapExplored().containsKey(id)){
					if(!obstacle1Position[y - robotY]){
						flag = true;
						break;
					}
				}
			}
			
			obstaclePosition[0] = obstaclePosition[1] = obstaclePosition[2] = false;
			obstacle1Position[0] = obstacle1Position[1] = obstacle1Position[2] = false;
			
			for (y = robotY + RobotManager.ROBOT_HEIGHT, x = robotX; x < robotX + RobotManager.ROBOT_WIDTH; ++x) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y))
					break;
				else if (!getMapExplored().containsKey(id)) {
					flag = true;
					break;
				}
				else if (isObstacle(id)) {
					obstaclePosition[x - robotX] = true;
				}
			}
			
			for (y = robotY + RobotManager.ROBOT_HEIGHT + 1, x = robotX; x < robotX + RobotManager.ROBOT_WIDTH; ++x) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y))
					break;
				else if (!getMapExplored().containsKey(id)){
					if(!obstaclePosition[x - robotX]){
						flag = true;
						break;
					}
				}
				else if (isObstacle(id)) {
					obstacle1Position[x - robotX] = true;
				}
			}
			
			for (y = robotY + RobotManager.ROBOT_HEIGHT + 2, x = robotX; x < robotX + RobotManager.ROBOT_WIDTH; ++x) {
				int id = XYToId(x, y);
				if (isOutBoundary(x, y)){
					break;
				}
				else if (!getMapExplored().containsKey(id)){
					if(!obstaclePosition[x - robotX]){
						flag = true;
						break;
					}
				}
			}
			
			boolean flagNorth = false;
			boolean flagEast = false;
			boolean flagSouth = false;
			boolean flagWest = false;
			int firstDiagonalX = robotX - 1;
			int firstDiagonalY = robotY - 1;
			int secondDiagonalX = robotX + RobotManager.ROBOT_WIDTH;
			int secondDiagonalY = robotY - 1;
			int thirdDiagonalX = robotX - 1;
			int thirdDiagonalY = robotY + RobotManager.ROBOT_HEIGHT;
			int fourthDiagonalX = robotX + RobotManager.ROBOT_WIDTH;
			int fourthDiagonalY = robotY + RobotManager.ROBOT_HEIGHT;
			int firstDiagonal = XYToId(firstDiagonalX, firstDiagonalY);
			int secondDiagonal = XYToId(secondDiagonalX, secondDiagonalY);
			int thirdDiagonal = XYToId(thirdDiagonalX, thirdDiagonalY);
			int fourthDiagonal = XYToId(fourthDiagonalX, fourthDiagonalY);
			
			for (y = robotY - 1, x = robotX; x < robotX + RobotManager.ROBOT_WIDTH; ++x) {
				int id = XYToId(x, y);
				if (isObstacle(id)){
					flagNorth = true;
					break;
				}
			}
			
			for (y = robotY + RobotManager.ROBOT_HEIGHT, x = robotX; x < robotX + RobotManager.ROBOT_WIDTH; ++x){
				int id = XYToId(x, y);
				if (isObstacle(id)){
					flagSouth = true;
					break;
				}
			}
			
			for (x = robotX - 1, y = robotY; y < robotY + RobotManager.ROBOT_HEIGHT; ++y) {
				int id = XYToId(x, y);
				if (isObstacle(id)){
					flagWest = true;
					break;
				}
			}
			
			for (x = robotX + RobotManager.ROBOT_WIDTH, y = robotY; y < robotY + RobotManager.ROBOT_HEIGHT; ++y) {
				int id = XYToId(x, y);
				if (isObstacle(id)){
					flagEast = true;
					break;
				}
			}
			
			if (!isOutBoundary(firstDiagonalX, firstDiagonalY) && !getMapExplored().containsKey(firstDiagonal)) {
				if (!flagNorth) {
					flag = true;
					break;
				}
				else if (!flagWest) {
					flag = true;
					break;
				}
			}
			
			if (!isOutBoundary(secondDiagonalX, secondDiagonalY) && !getMapExplored().containsKey(secondDiagonal)) {
				if (!flagNorth) {
					flag = true;
					break;
				}
				else if (!flagEast) {
					flag = true;
					break;
				}
			}
			
			if (!isOutBoundary(thirdDiagonalX, thirdDiagonalY) && !getMapExplored().containsKey(thirdDiagonal)) {
				if (!flagWest) {
					flag = true;
					break;
				}
				else if (!flagSouth) {
					flag = true;
					break;
				}
			}
			
			if (!isOutBoundary(fourthDiagonalX, fourthDiagonalY) && !getMapExplored().containsKey(fourthDiagonal)) {
				if (!flagEast) {
					flag = true;
					break;
				}
				else if (!flagSouth) {
					flag = true;
					break;
				}
			}
			
		}
		
		if(flag == true){
			for(int i = 1; i <= counter; i++)
				callStack.removeLast();
			ShortestPath moveBack = new ShortestPath(
					XYToId(RobotManager.getRobotPositionX(), RobotManager.getRobotPositionY()),
					idToCheck, getMapExplored());
			listOfBackTrackingMoves = moveBack.getListOfMoves();
			nextMove = listOfBackTrackingMoves.pop();
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
					robotPositionStack.add(XYToId(robotPosX, robotPosY));
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
					robotPositionStack.add(XYToId(robotPosX, robotPosY));
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
					robotPositionStack.add(XYToId(robotPosX, robotPosY));
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
					robotPositionStack.add(XYToId(robotPosX, robotPosY));
					return MOVE.SOUTH;
				}
			}
		}
		
		return MOVE.NO_MOVE;
	}
	
	/*
	private MOVE checkDiagonal(){
		int x, y;
		boolean flagNorth = false;
		boolean flagEast = false;
		boolean flagSouth = false;
		boolean flagWest = false;
		int firstDiagonalX[] = {robotPosX - 1, robotPosX - 1, robotPosX - 2, robotPosX - 2};
		int firstDiagonalY[] = {robotPosY - 1, robotPosY - 2, robotPosY - 1, robotPosY - 2};
		int secondDiagonalX[] = {robotPosX + RobotManager.ROBOT_WIDTH, robotPosX + RobotManager.ROBOT_WIDTH,
				robotPosX + RobotManager.ROBOT_WIDTH + 1, robotPosX + RobotManager.ROBOT_WIDTH + 1};
		int secondDiagonalY[] = {robotPosY - 1, robotPosY - 2, robotPosY - 1, robotPosY - 2};
		int thirdDiagonalX[] = {robotPosX - 1, robotPosX - 1, robotPosX - 2, robotPosX - 2};
		int thirdDiagonalY[] = {robotPosY + RobotManager.ROBOT_HEIGHT, robotPosY + RobotManager.ROBOT_HEIGHT + 1,
				robotPosY + RobotManager.ROBOT_HEIGHT, robotPosY + RobotManager.ROBOT_HEIGHT + 1};
		int fourthDiagonalX[] = {robotPosX + RobotManager.ROBOT_WIDTH, robotPosX + RobotManager.ROBOT_WIDTH,
				robotPosX + RobotManager.ROBOT_WIDTH + 1, robotPosX + RobotManager.ROBOT_WIDTH + 1};
		int fourthDiagonalY[] = {robotPosY + RobotManager.ROBOT_HEIGHT, robotPosY + RobotManager.ROBOT_HEIGHT + 1,
				robotPosY + RobotManager.ROBOT_HEIGHT, robotPosY + RobotManager.ROBOT_HEIGHT + 1};
		int firstDiagonal[] = new int[4];
		int secondDiagonal[] = new int[4];
		int thirdDiagonal[] = new int[4];
		int fourthDiagonal[] = new int[4];
		for(int i = 0; i < 4; i++){
			firstDiagonal[i] = XYToId(firstDiagonalX[i], firstDiagonalY[i]);
			secondDiagonal[i] = XYToId(secondDiagonalX[i], secondDiagonalY[i]);
			thirdDiagonal[i] = XYToId(thirdDiagonalX[i], thirdDiagonalY[i]);
			fourthDiagonal[i] = XYToId(fourthDiagonalX[i], fourthDiagonalY[i]);
		}
		
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
		
		for(int i = 0; i < 4; i++){
			if (!isOutBoundary(firstDiagonalX[i], firstDiagonalY[i]) && !getMapExplored().containsKey(firstDiagonal[i])) {
				if (!flagNorth) {
					callStack.add(MOVE.NORTH);
					return MOVE.NORTH;
				}
				else if (!flagWest) {
					callStack.add(MOVE.WEST);
					return MOVE.WEST;
				}
			}
			
			if (!isOutBoundary(secondDiagonalX[i], secondDiagonalY[i]) && !getMapExplored().containsKey(secondDiagonal[i])) {
				if (!flagNorth) {
					callStack.add(MOVE.NORTH);
					return MOVE.NORTH;
				}
				else if (!flagEast) {
					callStack.add(MOVE.EAST);
					return MOVE.EAST;
				}
			}
			
			if (!isOutBoundary(thirdDiagonalX[i], thirdDiagonalY[i]) && !getMapExplored().containsKey(thirdDiagonal[i])) {
				if (!flagWest) {
					callStack.add(MOVE.WEST);
					return MOVE.WEST;
				}
				else if (!flagSouth) {
					callStack.add(MOVE.SOUTH);
					return MOVE.SOUTH;
				}
			}
			
			if (!isOutBoundary(fourthDiagonalX[i], fourthDiagonalY[i]) && !getMapExplored().containsKey(fourthDiagonal[i])) {
				if (!flagEast) {
					callStack.add(MOVE.EAST);
					return MOVE.EAST;
				}
				else if (!flagSouth) {
					callStack.add(MOVE.SOUTH);
					return MOVE.SOUTH;
				}
			}
		}
		
		
		return MOVE.NO_MOVE;
	}
	*/
	
	private MOVE checkDiagonal(){
		int robotId = XYToId(RobotManager.getRobotPositionX(), RobotManager.getRobotPositionY());
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
				robotPositionStack.add(robotId);
				return MOVE.NORTH;
			}
			else if (!flagWest) {
				callStack.add(MOVE.WEST);
				robotPositionStack.add(robotId);
				return MOVE.WEST;
			}
		}
		
		if (!isOutBoundary(secondDiagonalX, secondDiagonalY) && !getMapExplored().containsKey(secondDiagonal)) {
			if (!flagNorth) {
				callStack.add(MOVE.NORTH);
				robotPositionStack.add(robotId);
				return MOVE.NORTH;
			}
			else if (!flagEast) {
				callStack.add(MOVE.EAST);
				robotPositionStack.add(robotId);
				return MOVE.EAST;
			}
		}
		
		if (!isOutBoundary(thirdDiagonalX, thirdDiagonalY) && !getMapExplored().containsKey(thirdDiagonal)) {
			if (!flagWest) {
				callStack.add(MOVE.WEST);
				robotPositionStack.add(robotId);
				return MOVE.WEST;
			}
			else if (!flagSouth) {
				callStack.add(MOVE.SOUTH);
				robotPositionStack.add(robotId);
				return MOVE.SOUTH;
			}
		}
		
		if (!isOutBoundary(fourthDiagonalX, fourthDiagonalY) && !getMapExplored().containsKey(fourthDiagonal)) {
			if (!flagEast) {
				callStack.add(MOVE.EAST);
				robotPositionStack.add(robotId);
				return MOVE.EAST;
			}
			else if (!flagSouth) {
				callStack.add(MOVE.SOUTH);
				robotPositionStack.add(robotId);
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
											nextMove = backtrack();
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		if(nextMove == MOVE.STOP) {
			if (XYToId(robotPosX, robotPosY) != 0){
				backToStartZone();
				nextMove = listOfBackTrackingMoves.pop();
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