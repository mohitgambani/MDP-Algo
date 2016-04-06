package algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import ui.MapManager;

public abstract class DFSMove extends Movable{

	public final int COMPRO_AMOUNT = 0;
	
	private ArrayList<Integer> mapTraversed;
	protected Deque<MOVE> callStack;
	protected int robotPosX;
	protected int robotPosY;

	protected boolean backTrackingMode;
	protected ShortestPath1 backTrackingStrategy;

	protected boolean backToStartMode;
	protected ShortestPath1 backToStartStrategy;
	
	
	public DFSMove() {
		callStack = new ArrayDeque<MOVE>();
		mapTraversed = new ArrayList<Integer>();
		backTrackingMode = false;
		backToStartMode = false;
	}
	
	@Override
	public abstract MOVE nextMove();

	@Override
	public int movesToStartZone() {
		return 0;
	}
	
	protected MOVE backToStart() {
		if (backToStartMode) {
			return backToStartStrategy.nextMove();
		}
		backToStartStrategy = new ShortestPath1(XYToId(robotPosX, robotPosY), 0, getMapExplored());
		backToStartMode = true;
		return backToStartStrategy.nextMove();
	}
	
	protected MOVE backTrack() {

		int currentRobotX = robotPosX;
		int currentRobotY = robotPosY;
		int goalX, goalY;
		MOVE nextMove = MOVE.STOP;

		if (backTrackingMode) {
			nextMove = backTrackingStrategy.nextMove();
			if (nextMove == MOVE.STOP) {
				backTrackingMode = false;
				return MOVE.NO_BACKTRACKING_MOVE;
			}
			return nextMove;
		}
		if (callStack.isEmpty()) {
			return MOVE.STOP;
		}
		MOVE lastMove;
		do {
			lastMove = callStack.removeLast();
			switch (lastMove) {
			case EAST:
				--robotPosX;
				break;
			case WEST:
				++robotPosX;
				break;
			case NORTH:
				++robotPosY;
				break;
			case SOUTH:
				--robotPosY;
				break;
			default:
				break;
			}
			// System.out.println("CurrentPos:" + robotPosX + "," + robotPosY);
			nextMove = attemptNorth();
			// System.out.println("North:" + nextMove);
			if (nextMove == MOVE.NO_MOVE) {
				nextMove = attemptEast();
				// System.out.println("East:" + nextMove);
				if (nextMove == MOVE.NO_MOVE) {
					nextMove = attemptSouth();
					// System.out.println("South:" + nextMove);
					if (nextMove == MOVE.NO_MOVE) {
						nextMove = attemptWest();
						// System.out.println("West:" + nextMove);
					}
				}
			}
		} while (!callStack.isEmpty() && nextMove == MOVE.NO_MOVE);

		// System.out.println("nextmove:" + nextMove);

		goalX = robotPosX;
		goalY = robotPosY;
		robotPosX = currentRobotX;
		robotPosY = currentRobotY;

		 System.out.println("Start:" + robotPosX + "," + robotPosY);
		 System.out.println("Goal:" + goalX + "," + goalY);
		backTrackingStrategy = new ShortestPath1(XYToId(robotPosX, robotPosY), XYToId(goalX, goalY), getMapExplored());
		backTrackingMode = true;
		// System.out.println(callStack);

		return backTrackingStrategy.nextMove();
	}

	protected MOVE attemptEast() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;

		boolean atBoundary = false;

		for (x = robotPosX + ROBOT_WIDTH, y = robotPosY; y < robotPosY + ROBOT_HEIGHT
				&& !outBoundary; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
				if (isAtBoundary(x, y)) {
					if(singleWayOfSensing(x, y))
						atBoundary = true;
				}
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
				if (atBoundary) {
					return MOVE.TURN_EAST_B;
				} else {
					return MOVE.TURN_EAST;
				}
			}
		}
		if (!explored) {
			if (atBoundary) {
				return MOVE.TURN_EAST_B;
			} else {
				return MOVE.TURN_EAST_M;
			}
		}
		if (!traversed) {
			return MOVE.EAST;
		}
		return MOVE.NO_MOVE;
	}

	protected MOVE attemptNorth() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;
		boolean atBoundary = false;

		for (y = robotPosY - 1, x = robotPosX; x < robotPosX + ROBOT_WIDTH && !outBoundary; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
				if (isAtBoundary(x, y)) {
					if(singleWayOfSensing(x, y))
						atBoundary = true;
				}
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
				if (atBoundary) {
					return MOVE.TURN_NORTH_B;
				} else {
					return MOVE.TURN_NORTH;
				}
			}
		}
		if (!explored) {
			if (atBoundary) {
				return MOVE.TURN_NORTH_B;
			} else {
				return MOVE.TURN_NORTH_M;
			}
		}
		if (!traversed) {
			return MOVE.NORTH;
		}
		return MOVE.NO_MOVE;
	}

	protected MOVE attemptSouth() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;
		boolean atBoundary = false;

		for (y = robotPosY + ROBOT_HEIGHT, x = robotPosX; x < robotPosX + ROBOT_WIDTH
				&& !outBoundary; ++x) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
				if (isAtBoundary(x, y)) {
					if(singleWayOfSensing(x, y))
						atBoundary = true;
				}
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
				if (atBoundary) {
					return MOVE.TURN_SOUTH_B;
				} else {
					return MOVE.TURN_SOUTH;
				}
			}
		}
		if (!explored) {
			if (atBoundary) {
				return MOVE.TURN_SOUTH_B;
			} else {
				return MOVE.TURN_SOUTH_M;
			}
		}
		if (!traversed) {
			return MOVE.SOUTH;
		}
		return MOVE.NO_MOVE;
	}

	protected MOVE attemptWest() {
		int x, y;
		boolean traversed = true;
		boolean obstacle = false;
		boolean explored = true;
		boolean outBoundary = false;
		boolean atBoundary = false;

		for (x = robotPosX - 1, y = robotPosY; y < robotPosY + ROBOT_HEIGHT && !outBoundary; ++y) {
			int id = XYToId(x, y);
			if (isOutBoundary(x, y)) {
				outBoundary = true;
			} else if (isObstacle(id)) {
				obstacle = true;
			} else if (!getMapExplored().containsKey(id)) {
				explored = false;
				if (isAtBoundary(x, y)) {
					if(singleWayOfSensing(x, y))
						atBoundary = true;
				}
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
				if (atBoundary) {
					return MOVE.TURN_WEST_B;
				} else {
					return MOVE.TURN_WEST;
				}
			}
		}
		if (!explored) {
			if (atBoundary) {
				return MOVE.TURN_WEST_B;
			} else {
				return MOVE.TURN_WEST_M;
			}
		}
		if (!traversed) {
			return MOVE.WEST;
		}
		return MOVE.NO_MOVE;
	}
	private boolean isObstacle(int index) {
		return getMapExplored().get(index) == Movable.GRID_TYPE.OBSTACLE ? true : false;
	}
	
	protected boolean isGoalZoneTraversed() {
		boolean traversed = true;
		int x, y;
		for (x = MAP_WIDTH - 1; x >= MAP_WIDTH - MapManager.GOAL_ZONE_WIDTH; --x) {
			for (y = MAP_HEIGHT - 1; y >= MAP_HEIGHT - MapManager.GOAL_ZONE_HEIGHT; --y) {
				if (!mapTraversed.contains(XYToId(x, y))) {
					traversed = false;
				}
			}
		}
		return traversed;
	}
	
	protected void addRobotToTraversed() {
		int x, y;
		for (x = robotPosX; x < robotPosX + ROBOT_WIDTH; ++x) {
			for (y = robotPosY; y < robotPosY + ROBOT_HEIGHT; ++y) {
				if (!mapTraversed.contains(XYToId(x, y))) {
					mapTraversed.add(XYToId(x, y));
				}
			}
		}
	}

	private boolean isAtBoundary(int x, int y) {
		return x == 0 || y == 0 || x == MAP_WIDTH - 1 || y == MAP_HEIGHT - 1;
	}

	private boolean singleWayOfSensing(int x, int y) {
		int counter = 0;

		counter = numOfWaysFromEast(x, y) + numOfWaysFromNorth(x, y) + numOfWaysFromSouth(x, y)
				+ numOfWaysFromWest(x, y);
		return counter == 1;
	}

	private int numOfWaysFromEast(int targetX, int targetY) {
		int counter = 0;
		int x, y, top;
		boolean impossible = false;

		if (targetX == 0) {
			return 0;
		}

		for (top = targetY - ROBOT_HEIGHT + 1; top <= targetY; ++top) {
			for (y = top; y < top + ROBOT_HEIGHT; ++y) {
				for (x = targetX - ROBOT_WIDTH; x < targetX; ++x) {
					if (isOutBoundary(x, y) || isObstacle(XYToId(x, y))
							|| !getMapExplored().containsKey(XYToId(x, y))) {
						impossible = true;
						break;
					}
				}
				if (impossible) {
					break;
				}
			}
			if (!impossible) {
				++counter;
			}
			impossible = false;
		}
		return counter;
	}

	private int numOfWaysFromNorth(int targetX, int targetY) {
		int counter = 0;
		int x, y, top;
		boolean impossible = false;

		if (targetY == 0) {
			return 0;
		}

		for (top = targetX - ROBOT_WIDTH + 1; top <= targetX; ++top) {
			for (x = top; x < top + ROBOT_WIDTH; ++x) {
				for (y = targetY - ROBOT_HEIGHT; y < targetY; ++y) {
					if (isOutBoundary(x, y) || isObstacle(XYToId(x, y))
							|| !getMapExplored().containsKey(XYToId(x, y))) {
						impossible = true;
						break;
					}
				}
				if (impossible) {
					break;
				}
			}
			if (!impossible) {
				++counter;
			}
			impossible = false;
		}
		return counter;
	}

	private int numOfWaysFromSouth(int targetX, int targetY) {
		int counter = 0;
		int x, y, top;
		boolean impossible = false;

		if (targetY == MAP_HEIGHT - 1) {
			return 0;
		}

		for (top = targetX - ROBOT_WIDTH + 1; top <= targetX; ++top) {
			for (x = top; x < top + ROBOT_WIDTH; ++x) {
				for (y = targetY + 1; y <= targetY + ROBOT_HEIGHT; ++y) {
					if (isOutBoundary(x, y) || isObstacle(XYToId(x, y))
							|| !getMapExplored().containsKey(XYToId(x, y))) {
						impossible = true;
						break;
					}
				}
				if (impossible) {
					break;
				}
			}
			if (!impossible) {
				++counter;
			}
			impossible = false;
		}
		return counter;
	}

	private int numOfWaysFromWest(int targetX, int targetY) {
		int counter = 0;
		int x, y, top;
		boolean impossible = false;

		if (targetX == MAP_WIDTH - 1) {
			return 0;
		}

		for (top = targetY - ROBOT_HEIGHT + 1; top <= targetY; ++top) {
			for (y = top; y < top + ROBOT_HEIGHT; ++y) {
				for (x = targetX + 1; x <= targetX + ROBOT_WIDTH; ++x) {
					if (isOutBoundary(x, y) || isObstacle(XYToId(x, y))
							|| !getMapExplored().containsKey(XYToId(x, y))) {
						impossible = true;
						break;
					}
				}
				if (impossible) {
					break;
				}
			}
			if (!impossible) {
				++counter;
			}
			impossible = false;
		}
		return counter;
	}
	
	@Override
	public void getMapUpdate(int id, GRID_TYPE type) {
		if(!(mapTraversed.contains(id) && type == GRID_TYPE.OBSTACLE))
			getMapExplored().put(id, type);
	}
}
