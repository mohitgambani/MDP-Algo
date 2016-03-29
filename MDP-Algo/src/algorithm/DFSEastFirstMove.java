package algorithm;

public class DFSEastFirstMove extends DFSMove {

	private MOVE eastDecision;
	private MOVE westDecision;
	private MOVE northDecision;
	private MOVE southDecision;

	@Override
	public MOVE nextMove() {
		MOVE nextMove = MOVE.STOP;

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();

		if(backToStartMode){
			return backToStart();
		}
		
		if (numOfExploredSpace() >= MAP_HEIGHT * MAP_WIDTH - COMPRO_AMOUNT) {
			return backToStart();
		}

		if (isConditionalStop()) {
			return backToStart();
		}

		if (backTrackingMode) {
			nextMove = backTrack();
			if (nextMove != MOVE.NO_BACKTRACKING_MOVE) {
				return nextMove;
			}
		}

		addRobotToTraversed();
		eastDecision = attemptEast();
		westDecision = attemptWest();
		northDecision = attemptNorth();
		southDecision = attemptSouth();
		if (!isGoalZoneTraversed()) {

			if (westDecision == MOVE.TURN_WEST_B) {
				return westDecision;
			} else if (southDecision == MOVE.TURN_SOUTH_B) {
				return southDecision;
			} else if (eastDecision == MOVE.TURN_EAST_B) {
				return eastDecision;
			} else if (northDecision == MOVE.TURN_NORTH_B) {
				return northDecision;
			}

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

			if (eastDecision == MOVE.TURN_EAST_B) {
				return eastDecision;
			} else if (northDecision == MOVE.TURN_NORTH_B) {
				return northDecision;
			} else if (westDecision == MOVE.TURN_WEST_B) {
				return westDecision;
			} else if (southDecision == MOVE.TURN_SOUTH_B) {
				return southDecision;
			}

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

			MOVE backTrackMove = backTrack();
//			MOVE backToStartMove = backToStart();
//			
//			System.out.println("x:" + robotPosX + "y:" + robotPosY);
//			System.out.println(backToStartStrategy.getListOfMoves());
//			System.out.println(backTrackingStrategy.getListOfMoves());
//			
//			if(backToStartStrategy.getListOfMoves().size()  < backTrackingStrategy.getListOfMoves().size()){
//				backTrackingMode = false;
//				return backToStartMove;
//			}
//			backToStartMode = false;
			return backTrackMove;
		}
	}
}
// package algorithm;
//
// import java.util.ArrayDeque;
// import java.util.ArrayList;
// import java.util.Deque;
// import java.util.Stack;
// import ui.MapManager;
//
// public class DFSEastFirstMove extends Movable {
//
// private ArrayList<Integer> mapTraversed;
// private Deque<MOVE> callStack;
// private int robotPosX;
// private int robotPosY;
//
// private boolean backTrackingMode;
// private Movable backTrackingStrategy;
//
// private MOVE eastDecision;
// private MOVE westDecision;
// private MOVE northDecision;
// private MOVE southDecision;
//
// private static Stack<MOVE> listOfBackTrackingMoves = new Stack<MOVE>();
// private static int count = 0;
//
// public DFSEastFirstMove() {
// super();
// mapTraversed = new ArrayList<Integer>();
// callStack = new ArrayDeque<MOVE>();
// backTrackingMode = false;
// }
//
// private MOVE backTrack() {
//
// int currentRobotX = robotPosX;
// int currentRobotY = robotPosY;
// int goalX, goalY;
// MOVE nextMove = MOVE.STOP;
//
// if (backTrackingMode) {
// nextMove = backTrackingStrategy.nextMove();
// if (nextMove == MOVE.STOP) {
// backTrackingMode = false;
// return MOVE.NO_BACKTRACKING_MOVE;
// }
// return nextMove;
// }
//
// if (callStack.isEmpty()) {
// return MOVE.STOP;
// }
//
// MOVE lastMove;
//
// do {
// lastMove = callStack.removeLast();
// switch (lastMove) {
// case EAST:
// --robotPosX;
// break;
// case WEST:
// ++robotPosX;
// break;
// case NORTH:
// ++robotPosY;
// break;
// case SOUTH:
// --robotPosY;
// break;
// default:
// break;
// }
// // System.out.println("CurrentPos:" + robotPosX + "," + robotPosY);
// nextMove = attemptNorth();
// // System.out.println("North:" + nextMove);
// if (nextMove == MOVE.NO_MOVE) {
// nextMove = attemptEast();
// // System.out.println("East:" + nextMove);
// if (nextMove == MOVE.NO_MOVE) {
// nextMove = attemptSouth();
// // System.out.println("South:" + nextMove);
// if (nextMove == MOVE.NO_MOVE) {
// nextMove = attemptWest();
// // System.out.println("West:" + nextMove);
// }
// }
// }
// } while (!callStack.isEmpty() && nextMove == MOVE.NO_MOVE);
//
// // System.out.println("nextmove:" + nextMove);
//
// goalX = robotPosX;
// goalY = robotPosY;
// robotPosX = currentRobotX;
// robotPosY = currentRobotY;
//
// // System.out.println("Start:" + robotPosX + "," + robotPosY);
// // System.out.println("Goal:" + goalX + "," + goalY);
// backTrackingStrategy = new ShortestPath(XYToId(robotPosX, robotPosY),
// XYToId(goalX, goalY), getMapExplored());
// backTrackingMode = true;
// // System.out.println(callStack);
//
// return backTrackingStrategy.nextMove();
// }
//
// private MOVE attemptEast() {
// int x, y;
// boolean traversed = true;
// boolean obstacle = false;
// boolean explored = true;
// boolean outBoundary = false;
//
// boolean atBoundary = false;
//
// for (x = robotPosX + RobotManager.ROBOT_WIDTH, y = robotPosY; y < robotPosY +
// RobotManager.ROBOT_HEIGHT
// && !outBoundary; ++y) {
// int id = XYToId(x, y);
// if (isOutBoundary(x, y)) {
// outBoundary = true;
// } else if (isObstacle(id)) {
// obstacle = true;
// } else if (!getMapExplored().containsKey(id)) {
// explored = false;
// if (isAtBoundary(x, y)) {
// atBoundary = true;
// }
// } else if (!mapTraversed.contains(id)) {
// traversed = false;
// }
//
// }
// if (outBoundary) {
// return MOVE.NO_MOVE;
// }
// if (obstacle) {
// if (explored) {
// return MOVE.NO_MOVE;
// } else {
// if (atBoundary) {
// return MOVE.TURN_EAST_B;
// } else {
// return MOVE.TURN_EAST;
// }
// }
// }
// if (!explored) {
// if (atBoundary) {
// return MOVE.TURN_EAST_B;
// } else {
// return MOVE.TURN_EAST_M;
// }
// }
// if (!traversed) {
// return MOVE.EAST;
// }
// return MOVE.NO_MOVE;
// }
//
// private MOVE attemptNorth() {
// int x, y;
// boolean traversed = true;
// boolean obstacle = false;
// boolean explored = true;
// boolean outBoundary = false;
// boolean atBoundary = false;
//
// for (y = robotPosY - 1, x = robotPosX; x < robotPosX +
// RobotManager.ROBOT_WIDTH && !outBoundary; ++x) {
// int id = XYToId(x, y);
// if (isOutBoundary(x, y)) {
// outBoundary = true;
// } else if (isObstacle(id)) {
// obstacle = true;
// } else if (!getMapExplored().containsKey(id)) {
// explored = false;
// if (isAtBoundary(x, y)) {
// atBoundary = true;
// }
// } else if (!mapTraversed.contains(id)) {
// traversed = false;
// }
// }
//
// if (outBoundary) {
// return MOVE.NO_MOVE;
// }
// if (obstacle) {
// if (explored) {
// return MOVE.NO_MOVE;
// } else {
// if (atBoundary) {
// return MOVE.TURN_NORTH_B;
// } else {
// return MOVE.TURN_NORTH;
// }
// }
// }
// if (!explored) {
// if (atBoundary) {
// return MOVE.TURN_NORTH_B;
// } else {
// return MOVE.TURN_NORTH_M;
// }
// }
// if (!traversed) {
// return MOVE.NORTH;
// }
// return MOVE.NO_MOVE;
// }
//
// private MOVE attemptSouth() {
// int x, y;
// boolean traversed = true;
// boolean obstacle = false;
// boolean explored = true;
// boolean outBoundary = false;
// boolean atBoundary = false;
//
// for (y = robotPosY + RobotManager.ROBOT_HEIGHT, x = robotPosX; x < robotPosX
// + RobotManager.ROBOT_WIDTH
// && !outBoundary; ++x) {
// int id = XYToId(x, y);
// if (isOutBoundary(x, y)) {
// outBoundary = true;
// } else if (isObstacle(id)) {
// obstacle = true;
// } else if (!getMapExplored().containsKey(id)) {
// explored = false;
// if (isAtBoundary(x, y)) {
// atBoundary = true;
// }
// } else if (!mapTraversed.contains(id)) {
// traversed = false;
// }
// }
// if (outBoundary) {
// return MOVE.NO_MOVE;
// }
// if (obstacle) {
// if (explored) {
// return MOVE.NO_MOVE;
// } else {
// if (atBoundary) {
// return MOVE.TURN_SOUTH_B;
// } else {
// return MOVE.TURN_SOUTH;
// }
// }
// }
// if (!explored) {
// if (atBoundary) {
// return MOVE.TURN_SOUTH_B;
// } else {
// return MOVE.TURN_SOUTH_M;
// }
// }
// if (!traversed) {
// return MOVE.SOUTH;
// }
// return MOVE.NO_MOVE;
// }
//
// private MOVE attemptWest() {
// int x, y;
// boolean traversed = true;
// boolean obstacle = false;
// boolean explored = true;
// boolean outBoundary = false;
// boolean atBoundary = false;
//
// for (x = robotPosX - 1, y = robotPosY; y < robotPosY +
// RobotManager.ROBOT_HEIGHT && !outBoundary; ++y) {
// int id = XYToId(x, y);
// if (isOutBoundary(x, y)) {
// outBoundary = true;
// } else if (isObstacle(id)) {
// obstacle = true;
// } else if (!getMapExplored().containsKey(id)) {
// explored = false;
// if (isAtBoundary(x, y)) {
// atBoundary = true;
// }
// } else if (!mapTraversed.contains(id)) {
// traversed = false;
// }
// }
// if (outBoundary) {
// return MOVE.NO_MOVE;
// }
// if (obstacle) {
// if (explored) {
// return MOVE.NO_MOVE;
// } else {
// if (atBoundary) {
// return MOVE.TURN_WEST_B;
// } else {
// return MOVE.TURN_WEST;
// }
// }
// }
// if (!explored) {
// if (atBoundary) {
// return MOVE.TURN_WEST_B;
// } else {
// return MOVE.TURN_WEST_M;
// }
// }
// if (!traversed) {
// return MOVE.WEST;
// }
// return MOVE.NO_MOVE;
// }
//
// @Override
// public MOVE nextMove() {
// MOVE nextMove = MOVE.STOP;
//
// robotPosX = RobotManager.getRobotPositionX();
// robotPosY = RobotManager.getRobotPositionY();
//
// if (isConditionalStop()) {
// if (count == 0) {
// backToStartZone();
// count++;
// }
// if (count == 1) {
// nextMove = listOfBackTrackingMoves.pop();
// if (nextMove == Movable.MOVE.STOP)
// count = 0;
// return nextMove;
// }
// }
//
// if (getMapExplored().size() == MAP_HEIGHT * MAP_WIDTH) {
// if (count == 0) {
// backToStartZone();
// count++;
// }
// if (count == 1) {
// nextMove = listOfBackTrackingMoves.pop();
// if (nextMove == Movable.MOVE.STOP)
// count = 0;
// return nextMove;
// }
// }
// if (!listOfBackTrackingMoves.empty()) {
// if (listOfBackTrackingMoves.peek() == MOVE.STOP)
// listOfBackTrackingMoves.pop();
// else
// return listOfBackTrackingMoves.pop();
// }
//
// if (backTrackingMode) {
// nextMove = backTrack();
// if (nextMove != MOVE.NO_BACKTRACKING_MOVE) {
// return nextMove;
// }
// }
//
// addRobotToTraversed();
// eastDecision = attemptEast();
// westDecision = attemptWest();
// northDecision = attemptNorth();
// southDecision = attemptSouth();
// if (!isGoalZoneTraversed()) {
//
// if (westDecision == MOVE.TURN_WEST_B) {
// return westDecision;
// } else if (southDecision == MOVE.TURN_SOUTH_B) {
// return southDecision;
// } else if (eastDecision == MOVE.TURN_EAST_B) {
// return eastDecision;
// } else if (northDecision == MOVE.TURN_NORTH_B) {
// return northDecision;
// }
//
// if (northDecision == MOVE.NORTH) {
// callStack.add(northDecision);
// return northDecision;
// } else if (northDecision == MOVE.TURN_NORTH_M) {
// return northDecision;
// } else if (eastDecision == MOVE.EAST) {
// callStack.add(eastDecision);
// return eastDecision;
// } else if (eastDecision == MOVE.TURN_EAST_M) {
// return eastDecision;
// } else if (southDecision == MOVE.SOUTH) {
// callStack.add(southDecision);
// return southDecision;
// } else if (southDecision == MOVE.TURN_SOUTH_M) {
// return southDecision;
// } else if (westDecision == MOVE.WEST) {
// callStack.add(westDecision);
// return westDecision;
// } else if (westDecision == MOVE.TURN_WEST_M) {
// return westDecision;
// }
//
// if (northDecision == MOVE.TURN_NORTH) {
// return northDecision;
// }
// if (eastDecision == MOVE.TURN_EAST) {
// return eastDecision;
// }
// if (southDecision == MOVE.TURN_SOUTH) {
// return southDecision;
// }
// if (westDecision == MOVE.TURN_WEST) {
// return westDecision;
// }
//
// return backTrack();
//
// } else {
//
// if (eastDecision == MOVE.TURN_EAST_B) {
// return eastDecision;
// } else if (northDecision == MOVE.TURN_NORTH_B) {
// return northDecision;
// } else if (westDecision == MOVE.TURN_WEST_B) {
// return westDecision;
// } else if (southDecision == MOVE.TURN_SOUTH_B) {
// return southDecision;
// }
//
// if (southDecision == MOVE.SOUTH) {
// callStack.add(southDecision);
// return southDecision;
// } else if (southDecision == MOVE.TURN_SOUTH_M) {
// return southDecision;
// } else if (westDecision == MOVE.WEST) {
// callStack.add(westDecision);
// return westDecision;
// } else if (westDecision == MOVE.TURN_WEST_M) {
// return westDecision;
// } else if (northDecision == MOVE.NORTH) {
// callStack.add(northDecision);
// return northDecision;
// } else if (northDecision == MOVE.TURN_NORTH_M) {
// return northDecision;
// } else if (eastDecision == MOVE.EAST) {
// callStack.add(eastDecision);
// return eastDecision;
// } else if (eastDecision == MOVE.TURN_EAST_M) {
// return eastDecision;
// }
// if (southDecision == MOVE.TURN_SOUTH) {
// return southDecision;
// } else if (westDecision == MOVE.TURN_WEST) {
// return westDecision;
// } else if (northDecision == MOVE.TURN_NORTH) {
// return northDecision;
// } else if (eastDecision == MOVE.TURN_EAST) {
// return eastDecision;
// }
//
// return backTrack();
// }
// }
//
// private void backToStartZone() {
// ShortestPath moveBack = new ShortestPath(
// XYToId(RobotManager.getRobotPositionX(), RobotManager.getRobotPositionY()),
// 0, getMapExplored());
// listOfBackTrackingMoves = moveBack.getListOfMoves();
// }
//
// @Override
// public int movesToStartZone() {
// return callStack.size();
// }
//
// private void addRobotToTraversed() {
// int x, y;
// for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
// for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
// if (!mapTraversed.contains(XYToId(x, y))) {
// mapTraversed.add(XYToId(x, y));
// }
// }
// }
// }
//
// private boolean isObstacle(int index) {
// return getMapExplored().get(index) == Movable.GRID_TYPE.OBSTACLE ? true :
// false;
// }
//
// @Override
// public MOVE peekMove() {
// return null;
// }
//
// private boolean isGoalZoneTraversed() {
// boolean traversed = true;
// int x, y;
// for (x = RobotManager.MAP_WIDTH - 1; x >= RobotManager.MAP_WIDTH -
// MapManager.GOAL_ZONE_WIDTH; --x) {
// for (y = RobotManager.MAP_HEIGHT - 1; y >= RobotManager.MAP_HEIGHT -
// MapManager.GOAL_ZONE_HEIGHT; --y) {
// if (!mapTraversed.contains(XYToId(x, y))) {
// traversed = false;
// }
// }
// }
// return traversed;
// }
//
// private boolean isAtBoundary(int x, int y) {
// return x == 0 || y == 0 || x == RobotManager.MAP_WIDTH - 1 || y ==
// RobotManager.MAP_HEIGHT - 1;
// }
// }
