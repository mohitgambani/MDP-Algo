package algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;
import java.util.Stack;
import algorithm.RobotManager.ORIENTATION;

public class ZigzagMove extends Movable {

	private ArrayList<Integer> mapTraversed;
	private Deque<MOVE> callStack;
	private int robotPosX;
	private int robotPosY;
	private ORIENTATION robotOri;


	private static Stack<MOVE> listOfBackTrackingMoves = new Stack<MOVE>();
	private static int count = 0;
	
	private int eastExplorable;
	private int westExplorable;
	private int northExplorable;
	private int southExplorable;

	public ZigzagMove() {
		super();
		mapTraversed = new ArrayList<Integer>();
		callStack = new ArrayDeque<MOVE>();
		
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

	private MOVE attemptEast() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;

		eastExplorable = 0;

		for (x = robotPosX + RobotManager.ROBOT_WIDTH, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT
				&& !outBoundary; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			}else if (!getMapExplored().containsKey(id)) {
				explored = false;
				++eastExplorable;
			}
		}
		
		System.out.println(explored);
		if (outBoundary) {
			return MOVE.NO_MOVE;
		} else if (explored) {
			if(obstacle){
				return MOVE.NO_MOVE;
			}else{
//				for (x = robotPosX + RobotManager.ROBOT_WIDTH + SensorDecoder.RANGE_LIMIT; x < RobotManager.MAP_WIDTH; ++x) {
//					for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
//						int id = XYToId(x, y);
//						if(isObstacle(id)){
//							return MOVE.NO_MOVE;
//						}
//						if (!getMapExplored().containsKey(id)) {
//							callStack.add(MOVE.EAST);
//							return MOVE.EAST;
//						}
//					}
//				}
				callStack.add(MOVE.EAST);
				return MOVE.EAST;
			}
		}else if(!explored && !obstacle){
			return MOVE.TURN_EAST;
		}
		return MOVE.NO_MOVE;
	}

	private MOVE attemptNorth() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;

		northExplorable = 0;
		
		for (y = robotPosY - 1, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH && !outBoundary; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
//				++northExplorable;
			}
		}
		if (outBoundary) {
			return MOVE.NO_MOVE;
		}  else if (explored) {
			if(obstacle){
				return MOVE.NO_MOVE;
			}else{
//				for (y = robotPosY - SensorDecoder.RANGE_LIMIT - 1; y >= 0 ; --y) {
//					for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
//						int id = XYToId(x, y);
//						if(isObstacle(id)){
//							return MOVE.NO_MOVE;
//						}
//						if (!getMapExplored().containsKey(id)) {
//							callStack.add(MOVE.NORTH);
//							return MOVE.NORTH;
//						}
//					}
//				}
				callStack.add(MOVE.NORTH);
				return MOVE.NORTH;
			}
		} else if(!explored && !obstacle){
			return MOVE.TURN_NORTH;
		}
		return MOVE.NO_MOVE;
	}

	private MOVE attemptSouth() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;


		southExplorable = 0;
		
		for (y = robotPosY + RobotManager.ROBOT_HEIGHT, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH
				&& !outBoundary; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
//				++southExplorable;
			}
		}

		if (outBoundary) {
			return MOVE.NO_MOVE;
		}  else if (explored) {
			if(obstacle){
				return MOVE.NO_MOVE;
			}else{
//				for (y = robotPosY + RobotManager.ROBOT_HEIGHT + SensorDecoder.RANGE_LIMIT; y < RobotManager.MAP_HEIGHT; ++y) {
//					for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
//						int id = XYToId(x, y);
//						if(isObstacle(id)){
//							return MOVE.NO_MOVE;
//						}
//						if (!getMapExplored().containsKey(id)) {
//							callStack.add(MOVE.SOUTH);
//							return MOVE.SOUTH;
//						}
//					}
//				}
				callStack.add(MOVE.SOUTH);
				return MOVE.SOUTH;
			}
		} else if(!explored && !obstacle){
			return MOVE.TURN_SOUTH;
		}
		return MOVE.NO_MOVE;
	}

	private MOVE attemptWest() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;

		westExplorable = 0;

		for (x = robotPosX - 1, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
//				++westExplorable;
			}
		}

		if (outBoundary) {
			return MOVE.NO_MOVE;
		}  else if (explored) {
			if(obstacle){
				return MOVE.NO_MOVE;
			}else{
//				for (x = robotPosX - SensorDecoder.RANGE_LIMIT - 1; x >= 0 ; --x) {
//					for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
//						int id = XYToId(x, y);
//						if(isObstacle(id)){
//							return MOVE.NO_MOVE;
//						}
//						if (!getMapExplored().containsKey(id)) {
//							callStack.add(MOVE.WEST);
//							return MOVE.WEST;
//						}
//					}
//				}
				callStack.add(MOVE.WEST);
				return MOVE.WEST;
			}
		} else if(!explored && !obstacle){
			return MOVE.TURN_WEST;
		}
		return MOVE.NO_MOVE;
	}

	@Override
	public MOVE nextMove() {
		MOVE nextMove = MOVE.STOP;

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		robotOri = RobotManager.getRobotOrientation();

//		System.out.println(getMapExplored());

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
		nextMove = attemptNorth();
		if (nextMove == MOVE.NO_MOVE) {
			nextMove = attemptEast();
			if ((nextMove == MOVE.NO_MOVE)) {
				nextMove = attemptSouth();
				if ((nextMove == MOVE.NO_MOVE) ) {
					nextMove = attemptWest();
					if ((nextMove == MOVE.NO_MOVE) ) {
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
		for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
			for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
				if (!mapTraversed.contains(XYToId(x, y))) {
					mapTraversed.add(XYToId(x, y));
				}
			}
		}
	}

	// private void addRobotToMapExplored() {
	// int x, y;
	// for (x = RobotManager.getRobotPositionX(); x < ROBOT_WIDTH; ++x) {
	// for (y = RobotManager.getRobotPositionY(); y < ROBOT_HEIGHT; ++y) {
	// getMapUpdate(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
	// }
	// }
	// }

	private boolean isObstacle(int index) {
		return getMapExplored().get(index) == Movable.GRID_TYPE.OBSTACLE ? true : false;
	}

	@Override
	public MOVE peekMove() {
		// TODO Auto-generated method stub
		return null;
	}

}
//package algorithm;
//
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Deque;
//import java.util.Hashtable;
//import java.util.Stack;
//import algorithm.RobotManager.ORIENTATION;
//
//public class ZigzagMove extends Movable {
//
//	private ArrayList<Integer> mapTraversed;
//	private Deque<MOVE> callStack;
//	private int robotPosX;
//	private int robotPosY;
//	private ORIENTATION robotOri;
//
//
//	private static Stack<MOVE> listOfBackTrackingMoves = new Stack<MOVE>();
//	private static int count = 0;
//	
//	private int eastExplorable;
//	private int westExplorable;
//	private int northExplorable;
//	private int southExplorable;
//
//	public ZigzagMove() {
//		super();
//		mapTraversed = new ArrayList<Integer>();
//		callStack = new ArrayDeque<MOVE>();
//		
//	}
//
//	private MOVE backTrack() {
//		MOVE nextMove = MOVE.STOP;
//
//		if (callStack.isEmpty())
//			return nextMove;
//
//		Enum<MOVE> lastMove = callStack.removeLast();
//
//		if (lastMove == MOVE.EAST) {
//			if (robotOri == ORIENTATION.EAST) {
//				nextMove = MOVE.WEST_R;
//			} else {
//				nextMove = MOVE.WEST;
//			}
//		} else if (lastMove == MOVE.NORTH) {
//			if (robotOri == ORIENTATION.NORTH) {
//				nextMove = MOVE.SOUTH_R;
//			} else {
//				nextMove = MOVE.SOUTH;
//			}
//
//		} else if (lastMove == MOVE.SOUTH) {
//			if (robotOri == ORIENTATION.SOUTH) {
//				nextMove = MOVE.NORTH_R;
//			} else {
//				nextMove = MOVE.NORTH;
//			}
//		} else if (lastMove == MOVE.WEST) {
//			if (robotOri == ORIENTATION.WEST) {
//				nextMove = MOVE.EAST_R;
//			} else {
//				nextMove = MOVE.EAST;
//			}
//		}
//		return nextMove;
//	}
//
//	private MOVE attemptEast() {
//		int x, y;
//		boolean traversed = true;
//		boolean obstacle = false;
//		boolean explored = true;
//		boolean outBoundary = false;
//
//		eastExplorable = 0;
//
//		for (x = robotPosX + RobotManager.ROBOT_WIDTH, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT
//				&& !outBoundary; ++y) {
//			int id = XYToId(x, y);
//			if (isOutBoundary(x, y)) {
//				outBoundary = true;
//			} else if (isObstacle(id)) {
//				obstacle = true;
//			}else if (!getMapExplored().containsKey(id)) {
//				explored = false;
//				++eastExplorable;
//			}
//		}
//
//		if (outBoundary) {
//			return MOVE.NO_MOVE;
//		}  else if (obstacle) {
//			return MOVE.NO_MOVE;
//		}
//		
//		if(!explored){
//			return MOVE.TURN_EAST;
//		}
//
//		
//		for (x = robotPosX + RobotManager.ROBOT_WIDTH + 1; x < RobotManager.MAP_WIDTH; ++x) {
//			for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
//				int id = XYToId(x, y);
//				if(isObstacle(id)){
//					return MOVE.NO_MOVE;
//				}
//				if (!getMapExplored().containsKey(id)) {
//					
//					callStack.add(MOVE.EAST);
//					return MOVE.EAST;
//				}
//			}
//		}
//
//		return MOVE.NO_MOVE;
//	}
//
//	private MOVE attemptNorth() {
//		int x, y;
//		boolean traversed = true;
//		boolean obstacle = false;
//		boolean explored = true;
//		boolean outBoundary = false;
//
//		northExplorable = 0;
//		
//		for (y = robotPosY - 1, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH && !outBoundary; ++x) {
//			int id = XYToId(x, y);
//			if (isOutBoundary(x, y)) {
//				outBoundary = true;
//			} else if (isObstacle(id)) {
//				obstacle = true;
//			} else if (!getMapExplored().containsKey(id)) {
//				explored = false;
////				++northExplorable;
//			}
//		}
//		if (outBoundary) {
//			return MOVE.NO_MOVE;
//		}  else if (obstacle) {
//			return MOVE.NO_MOVE;
//		}
//		
//		if(!explored){
//			return MOVE.TURN_NORTH;
//		}
//
//		for (y = robotPosY - 2; y >= 0 ; --y) {
//			for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
//				int id = XYToId(x, y);
//				if(isObstacle(id)){
//					return MOVE.NO_MOVE;
//				}
//				if (!getMapExplored().containsKey(id)) {
//					callStack.add(MOVE.NORTH);
//					return MOVE.NORTH;
//				}
//			}
//		}
//
//		return MOVE.NO_MOVE;
//	}
//
//	private MOVE attemptSouth() {
//		int x, y;
//		boolean traversed = true;
//		boolean obstacle = false;
//		boolean explored = true;
//		boolean outBoundary = false;
//
//
//		southExplorable = 0;
//		
//		for (y = robotPosY + RobotManager.ROBOT_HEIGHT, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH
//				&& !outBoundary; ++x) {
//			int id = XYToId(x, y);
//			if (isOutBoundary(x, y)) {
//				outBoundary = true;
//			} else if (isObstacle(id)) {
//				obstacle = true;
//			} else if (!getMapExplored().containsKey(id)) {
//				explored = false;
////				++southExplorable;
//			}
//		}
//
//		if (outBoundary) {
//			return MOVE.NO_MOVE;
//		}  else if (obstacle) {
//			return MOVE.NO_MOVE;
//		}
//		
//		if(!explored){
//			return MOVE.TURN_SOUTH;
//		}
//
//		for (y = robotPosY + RobotManager.ROBOT_HEIGHT + 1; y < RobotManager.MAP_HEIGHT; ++y) {
//			for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
//				int id = XYToId(x, y);
//				if(isObstacle(id)){
//					return MOVE.NO_MOVE;
//				}
//				if (!getMapExplored().containsKey(id)) {
//					callStack.add(MOVE.SOUTH);
//					return MOVE.SOUTH;
//				}
//			}
//		}
//
//		return MOVE.NO_MOVE;
//	}
//
//	private MOVE attemptWest() {
//		int x, y;
//		boolean traversed = true;
//		boolean obstacle = false;
//		boolean explored = true;
//		boolean outBoundary = false;
//
//		westExplorable = 0;
//
//		for (x = robotPosX - 1, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
//			int id = XYToId(x, y);
//			if (isOutBoundary(x, y)) {
//				outBoundary = true;
//			} else if (isObstacle(id)) {
//				obstacle = true;
//			} else if (!getMapExplored().containsKey(id)) {
//				explored = false;
////				++westExplorable;
//			}
//		}
//
//		if (outBoundary) {
//			return MOVE.NO_MOVE;
//		}  else if (obstacle) {
//			return MOVE.NO_MOVE;
//		}
//		
//		if(!explored){
//			return MOVE.TURN_WEST;
//		}
//
//		for (x = robotPosX - 2; x >= 0 ; --x) {
//			for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
//				int id = XYToId(x, y);
//				if(isObstacle(id)){
//					return MOVE.NO_MOVE;
//				}
//				if (!getMapExplored().containsKey(id)) {
//					callStack.add(MOVE.WEST);
//					return MOVE.WEST;
//				}
//			}
//		}
//
//		return MOVE.NO_MOVE;
//	}
//
//	@Override
//	public MOVE nextMove() {
//		MOVE nextMove = MOVE.STOP;
//
//		robotPosX = RobotManager.getRobotPositionX();
//		robotPosY = RobotManager.getRobotPositionY();
//		robotOri = RobotManager.getRobotOrientation();
//
////		System.out.println(getMapExplored());
//
//		if (isConditionalStop()) {
//			if (count == 0) {
//				backToStartZone();
//				count++;
//			}
//			if (count == 1) {
//				nextMove = listOfBackTrackingMoves.pop();
//				if (nextMove == Movable.MOVE.STOP)
//					count = 0;
//				return nextMove;
//			}
//		}
//
//		if (getMapExplored().size() == MAP_HEIGHT * MAP_WIDTH) {
//			if (count == 0) {
//				backToStartZone();
//				count++;
//			}
//			if (count == 1) {
//				nextMove = listOfBackTrackingMoves.pop();
//				if (nextMove == Movable.MOVE.STOP)
//					count = 0;
//				return nextMove;
//			}
//		}
//		if (!listOfBackTrackingMoves.empty()) {
//			if (listOfBackTrackingMoves.peek() == MOVE.STOP)
//				listOfBackTrackingMoves.pop();
//			else
//				return listOfBackTrackingMoves.pop();
//		}
//
//		addRobotToTraversed();
//		nextMove = attemptNorth();
//		if (nextMove == MOVE.NO_MOVE) {
//			nextMove = attemptEast();
//			if ((nextMove == MOVE.NO_MOVE)) {
//				nextMove = attemptSouth();
//				if ((nextMove == MOVE.NO_MOVE) ) {
//					nextMove = attemptWest();
//					if ((nextMove == MOVE.NO_MOVE) ) {
//						nextMove = backTrack();
//					}
//				}
//			}
//		}
//		return nextMove;
//	}
//
//	private void backToStartZone() {
//		ShortestPath moveBack = new ShortestPath(
//				XYToId(RobotManager.getRobotPositionX(), RobotManager.getRobotPositionY()), 0, getMapExplored());
//		listOfBackTrackingMoves = moveBack.getListOfMoves();
//	}
//
//	@Override
//	public int movesToStartZone() {
//		return callStack.size();
//	}
//
//	private void addRobotToTraversed() {
//		int x, y;
//		for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
//			for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
//				if (!mapTraversed.contains(XYToId(x, y))) {
//					mapTraversed.add(XYToId(x, y));
//				}
//			}
//		}
//	}
//
//	// private void addRobotToMapExplored() {
//	// int x, y;
//	// for (x = RobotManager.getRobotPositionX(); x < ROBOT_WIDTH; ++x) {
//	// for (y = RobotManager.getRobotPositionY(); y < ROBOT_HEIGHT; ++y) {
//	// getMapUpdate(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
//	// }
//	// }
//	// }
//
//	private boolean isObstacle(int index) {
//		return getMapExplored().get(index) == Movable.GRID_TYPE.OBSTACLE ? true : false;
//	}
//
//	@Override
//	public MOVE peekMove() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
//package algorithm;
//
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Deque;
//import java.util.Hashtable;
//import java.util.Stack;
//import algorithm.RobotManager.ORIENTATION;
//
//public class ZigzagMove extends Movable {
//
//	private ArrayList<Integer> mapTraversed;
//	private Deque<MOVE> callStack;
//	private int robotPosX;
//	private int robotPosY;
//	private ORIENTATION robotOri;
//
//	private int maxExplorable;
//	private int eastExplorable;
//	private int westExplorable;
//	private int northExplorable;
//	private int southExplorable;
//
//	private static Stack<MOVE> listOfBackTrackingMoves = new Stack<MOVE>();
//	private static int count = 0;
//
//	public ZigzagMove() {
//		super();
//		mapTraversed = new ArrayList<Integer>();
//		callStack = new ArrayDeque<MOVE>();
//		maxExplorable = 0;
//		eastExplorable = westExplorable = northExplorable = southExplorable = 0;
//	}
//
//	private MOVE backTrack() {
//		MOVE nextMove = MOVE.STOP;
//
//		if (callStack.isEmpty())
//			return nextMove;
//
//		Enum<MOVE> lastMove = callStack.removeLast();
//
//		if (lastMove == MOVE.EAST) {
//			if (robotOri == ORIENTATION.EAST) {
//				nextMove = MOVE.WEST_R;
//			} else {
//				nextMove = MOVE.WEST;
//			}
//		} else if (lastMove == MOVE.NORTH) {
//			if (robotOri == ORIENTATION.NORTH) {
//				nextMove = MOVE.SOUTH_R;
//			} else {
//				nextMove = MOVE.SOUTH;
//			}
//
//		} else if (lastMove == MOVE.SOUTH) {
//			if (robotOri == ORIENTATION.SOUTH) {
//				nextMove = MOVE.NORTH_R;
//			} else {
//				nextMove = MOVE.NORTH;
//			}
//		} else if (lastMove == MOVE.WEST) {
//			if (robotOri == ORIENTATION.WEST) {
//				nextMove = MOVE.EAST_R;
//			} else {
//				nextMove = MOVE.EAST;
//			}
//		}
//		return nextMove;
//	}
//
//	private MOVE attemptEast() {
//		int x, y;
//		boolean traversed = true;
//		boolean obstacle = false;
//		boolean explored = true;
//		boolean outBoundary = false;
//
//		eastExplorable = 0;
//
//		for (x = robotPosX + RobotManager.ROBOT_WIDTH, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT
//				&& !outBoundary; ++y) {
//			int id = XYToId(x, y);
//			if (isOutBoundary(x, y)) {
//				outBoundary = true;
//			} else if (isObstacle(id)) {
//				obstacle = true;
//			} else if (!getMapExplored().containsKey(id)) {
//				explored = false;
//				++eastExplorable;
//			} else if (!mapTraversed.contains(id)) {
//				traversed = false;
//			}
//		}
//
//		if (outBoundary) {
//			return MOVE.NO_MOVE;
//		} else if (!explored) {
//			maxExplorable = Math.max(eastExplorable, maxExplorable);
//			return MOVE.TURN_EAST;
//		} else if (obstacle) {
//			return MOVE.NO_MOVE;
//		}
//
//		System.out.println(getMapExplored());
//		for (x = RobotManager.MAP_WIDTH - 1; x > robotPosX + RobotManager.ROBOT_WIDTH; --x) {
//			for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
//				int id = XYToId(x, y);
//				if (!getMapExplored().containsKey(id)) {
//					System.out.println(id);
//					callStack.add(MOVE.EAST);
//					return MOVE.EAST;
//				}
//			}
//		}
//
//		return MOVE.NO_MOVE;
//	}
//
//	private MOVE attemptNorth() {
//		int x, y;
//		boolean traversed = true;
//		boolean obstacle = false;
//		boolean explored = true;
//		boolean outBoundary = false;
//
//		northExplorable = 0;
//
//		for (y = robotPosY - 1, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH && !outBoundary; ++x) {
//			int id = XYToId(x, y);
//			if (isOutBoundary(x, y)) {
//				outBoundary = true;
//			} else if (isObstacle(id)) {
//				obstacle = true;
//			} else if (!getMapExplored().containsKey(id)) {
//				explored = false;
//				++northExplorable;
//			} else if (!mapTraversed.contains(id)) {
//				traversed = false;
//			}
//		}
//		if (outBoundary) {
//			return MOVE.NO_MOVE;
//		} else if (!explored) {
//			maxExplorable = Math.max(northExplorable, maxExplorable);
//			return MOVE.TURN_NORTH;
//		} else if (obstacle) {
//			return MOVE.NO_MOVE;
//		}
//
//		for (y = 0; y < robotPosY - 1 ; ++y) {
//			for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
//				int id = XYToId(x, y);
//				if (!getMapExplored().containsKey(id)) {
//					callStack.add(MOVE.NORTH);
//					return MOVE.NORTH;
//				}
//			}
//		}
//
//		return MOVE.NO_MOVE;
//	}
//
//	private MOVE attemptSouth() {
//		int x, y;
//		boolean traversed = true;
//		boolean obstacle = false;
//		boolean explored = true;
//		boolean outBoundary = false;
//
//		southExplorable = 0;
//
//		for (y = robotPosY + RobotManager.ROBOT_HEIGHT, x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH
//				&& !outBoundary; ++x) {
//			int id = XYToId(x, y);
//			if (isOutBoundary(x, y)) {
//				outBoundary = true;
//			} else if (isObstacle(id)) {
//				obstacle = true;
//			} else if (!getMapExplored().containsKey(id)) {
//				explored = false;
//				++southExplorable;
//			} else if (!mapTraversed.contains(id)) {
//				traversed = false;
//			}
//		}
//
//		if (outBoundary) {
//			return MOVE.NO_MOVE;
//		} else if (!explored) {
//			maxExplorable = Math.max(southExplorable, maxExplorable);
//			return MOVE.TURN_SOUTH;
//		} else if (obstacle) {
//			return MOVE.NO_MOVE;
//		}
//
//		for (y = RobotManager.MAP_HEIGHT - 1; y > robotPosY + RobotManager.ROBOT_HEIGHT; --y) {
//			for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
//				int id = XYToId(x, y);
//				if (!getMapExplored().containsKey(id)) {
//					callStack.add(MOVE.SOUTH);
//					return MOVE.SOUTH;
//				}
//			}
//		}
//
//		return MOVE.NO_MOVE;
//	}
//
//	private MOVE attemptWest() {
//		int x, y;
//		boolean traversed = true;
//		boolean obstacle = false;
//		boolean explored = true;
//		boolean outBoundary = false;
//
//		westExplorable = 0;
//
//		for (x = robotPosX - 1, y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
//			int id = XYToId(x, y);
//			if (isOutBoundary(x, y)) {
//				outBoundary = true;
//			} else if (isObstacle(id)) {
//				obstacle = true;
//			} else if (!getMapExplored().containsKey(id)) {
//				explored = false;
//				++westExplorable;
//			} else if (!mapTraversed.contains(id)) {
//				traversed = false;
//			}
//		}
//
//		if (outBoundary) {
//			return MOVE.NO_MOVE;
//		} else if (!explored) {
//			maxExplorable = Math.max(westExplorable, maxExplorable);
//			return MOVE.TURN_WEST;
//		} else if (obstacle) {
//			return MOVE.NO_MOVE;
//		}
//
//		for (x = 0; x < robotPosX - 1; ++x) {
//			for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
//				int id = XYToId(x, y);
//				if (!getMapExplored().containsKey(id)) {
//					callStack.add(MOVE.WEST);
//					return MOVE.WEST;
//				}
//			}
//		}
//
//		return MOVE.NO_MOVE;
//	}
//
//	@Override
//	public MOVE nextMove() {
//		MOVE nextMove = MOVE.STOP;
//
//		robotPosX = RobotManager.getRobotPositionX();
//		robotPosY = RobotManager.getRobotPositionY();
//		robotOri = RobotManager.getRobotOrientation();
//
//		maxExplorable = 0;
//
//		if (isConditionalStop()) {
//			if (count == 0) {
//				backToStartZone();
//				count++;
//			}
//			if (count == 1) {
//				nextMove = listOfBackTrackingMoves.pop();
//				if (nextMove == Movable.MOVE.STOP)
//					count = 0;
//				return nextMove;
//			}
//		}
//
//		if (getMapExplored().size() == MAP_HEIGHT * MAP_WIDTH) {
//			if (count == 0) {
//				backToStartZone();
//				count++;
//			}
//			if (count == 1) {
//				nextMove = listOfBackTrackingMoves.pop();
//				if (nextMove == Movable.MOVE.STOP)
//					count = 0;
//				return nextMove;
//			}
//		}
//		if (!listOfBackTrackingMoves.empty()) {
//			if (listOfBackTrackingMoves.peek() == MOVE.STOP)
//				listOfBackTrackingMoves.pop();
//			else
//				return listOfBackTrackingMoves.pop();
//		}
//
//		addRobotToTraversed();
//		nextMove = attemptNorth();
//		if ((nextMove == MOVE.NO_MOVE) || (nextMove == MOVE.TURN_NORTH && northExplorable < maxExplorable)) {
//			nextMove = attemptEast();
//			if ((nextMove == MOVE.NO_MOVE) || (nextMove == MOVE.TURN_EAST && eastExplorable < maxExplorable)) {
//				nextMove = attemptSouth();
//				if ((nextMove == MOVE.NO_MOVE) || (nextMove == MOVE.TURN_SOUTH && southExplorable < maxExplorable)) {
//					nextMove = attemptWest();
//					if ((nextMove == MOVE.NO_MOVE) || (nextMove == MOVE.TURN_WEST && westExplorable < maxExplorable)) {
//						nextMove = backTrack();
//					}
//				}
//			}
//		}
//		return nextMove;
//	}
//
//	private void backToStartZone() {
//		ShortestPath moveBack = new ShortestPath(
//				XYToId(RobotManager.getRobotPositionX(), RobotManager.getRobotPositionY()), 0, getMapExplored());
//		listOfBackTrackingMoves = moveBack.getListOfMoves();
//	}
//
//	@Override
//	public int movesToStartZone() {
//		return callStack.size();
//	}
//
//	private void addRobotToTraversed() {
//		int x, y;
//		for (x = robotPosX; x < robotPosX + RobotManager.ROBOT_WIDTH; ++x) {
//			for (y = robotPosY; y < robotPosY + RobotManager.ROBOT_HEIGHT; ++y) {
//				if (!mapTraversed.contains(XYToId(x, y))) {
//					mapTraversed.add(XYToId(x, y));
//				}
//			}
//		}
//	}
//
//	// private void addRobotToMapExplored() {
//	// int x, y;
//	// for (x = RobotManager.getRobotPositionX(); x < ROBOT_WIDTH; ++x) {
//	// for (y = RobotManager.getRobotPositionY(); y < ROBOT_HEIGHT; ++y) {
//	// getMapUpdate(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
//	// }
//	// }
//	// }
//
//	private boolean isObstacle(int index) {
//		return getMapExplored().get(index) == Movable.GRID_TYPE.OBSTACLE ? true : false;
//	}
//
//	@Override
//	public MOVE peekMove() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}