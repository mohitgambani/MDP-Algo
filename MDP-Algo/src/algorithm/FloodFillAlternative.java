package algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;


public class FloodFillAlternative extends Movable {

	private ArrayList<Integer> mapTraversed;

	private int robotPosX;
	private int robotPosY;
	private Hashtable<Integer, Enum<Movable.GRID_TYPE>> mapExplored;

	private Deque<MOVE> callStack;
	
//	private boolean[] untraversed;
	
	private Enum<MOVE> untraversed;

	public FloodFillAlternative() {
		super();
		mapTraversed = new ArrayList<Integer>();
		mapExplored = getMapExplored();
		callStack = new ArrayDeque<MOVE>();
//		untraversed = new boolean[4];
		untraversed = null;
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

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		int x, y;
		boolean unexplored = false;
		int nearestUnexplored = 0;

		boolean noMove = false;
		
		for (x = robotPosX + RobotManager.getRobotWidth(); x < RobotManager.MAP_WIDTH && !noMove && !unexplored; ++x){
			for(y = robotPosY; y < robotPosY + RobotManager.getRobotHeight(); ++y){
				int id = XYToId(x, y);
				if(!mapExplored.containsKey(id)){
					nearestUnexplored = id;
					unexplored = true;
				}else if(mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE){
					noMove = true;
				}else if(!mapTraversed.contains(id)){
//					untraversed[0] = true;
					untraversed = MOVE.EAST;
				}
				
			}
		}
		if(unexplored && !noMove){
			if(idToX(nearestUnexplored) > robotPosX + RobotManager.getRobotWidth()){
				callStack.add(MOVE.EAST);
					return MOVE.EAST;
			}else{
				return MOVE.TURN_EAST;
			}
		}
		return MOVE.NO_MOVE;
	}

	private Enum<MOVE> attemptWest() {

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		int x, y;
		boolean unexplored = false;
		int nearestUnexplored = 0;
		boolean noMove = false;
		
		for (x = robotPosX - 1; x >= 0 && !noMove && !unexplored; --x){
			for(y = robotPosY; y < robotPosY + RobotManager.getRobotHeight(); ++y){
				int id = XYToId(x, y);
				if(!mapExplored.containsKey(id)){
					nearestUnexplored = id;
					unexplored = true;
				}else if(mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE){
					noMove = true;
				}else if(!mapTraversed.contains(id)){
//					untraversed[3] = true;
					if(untraversed == null)
						untraversed = MOVE.WEST;
				}
			}
		}
		if(unexplored && !noMove){
			if(idToX(nearestUnexplored) < robotPosX - 1){
				callStack.add(MOVE.WEST);
				return MOVE.WEST;
			}else{
				return MOVE.TURN_WEST;
			}
		}
		return MOVE.NO_MOVE;
	}

	

	private Enum<MOVE> attemptNorth() {

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		int x, y;
		boolean unexplored = false;
		int nearestUnexplored = 0;
		boolean noMove = false;
		
		for (y = robotPosY - 1; y >= 0 && !noMove && !unexplored; --y){
			for(x = robotPosX; x < robotPosX + RobotManager.getRobotWidth(); ++x){
				int id = XYToId(x, y);
				if(!mapExplored.containsKey(id)){
					nearestUnexplored = id;
					unexplored = true;
				}else if(mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE){
					noMove = true;
				}else if(!mapTraversed.contains(id)){
//					untraversed[1] = true;
					if(untraversed == null)
						untraversed = MOVE.NORTH;
				}
			}
		}
		if(unexplored && !noMove){
			if(idToY(nearestUnexplored) < robotPosY - 1){
				callStack.add(MOVE.NORTH);
				return MOVE.NORTH;
			}else{
				return MOVE.TURN_NORTH;
			}
		}
		return MOVE.NO_MOVE;
	}

	private Enum<MOVE> attemptSouth() {

		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		int x, y;
		boolean unexplored = false;
		int nearestUnexplored = 0;
		boolean noMove = false;
		
		for (y = robotPosY + RobotManager.getRobotHeight(); y < RobotManager.MAP_HEIGHT && !noMove && !unexplored; ++y){
			for(x = robotPosX; x < robotPosX + RobotManager.getRobotWidth(); ++x){
				int id = XYToId(x, y);
				if(!mapExplored.containsKey(id)){
					nearestUnexplored = id;
					unexplored = true;
				}else if(mapExplored.get(id) == Movable.GRID_TYPE.OBSTACLE){
					noMove = true;
				}else if(!mapTraversed.contains(id)){
//					untraversed[2] = true;
					if(untraversed == null)
						untraversed = MOVE.SOUTH;
				}
			}
		}
		if(unexplored && !noMove){
			if(idToY(nearestUnexplored) > robotPosY + RobotManager.getRobotHeight()){
				callStack.add(MOVE.SOUTH);
				return MOVE.SOUTH;
			}else{
				return MOVE.TURN_SOUTH;
			}
		}
		return MOVE.NO_MOVE;
	}

	@Override
	public Enum<MOVE> nextMove() {

		Enum<MOVE> nextMove = MOVE.STOP;

//		if(isConditionalStop())
//			return backTrack();
		
		if (getMapExplored().size() == MAP_HEIGHT * MAP_WIDTH)
			return backTrack();
		
		
		
		robotPosX = RobotManager.getRobotPositionX();
		robotPosY = RobotManager.getRobotPositionY();
		int x, y;
		for(x = robotPosX; x < robotPosX + RobotManager.getRobotWidth(); ++x){
			for(y = robotPosY; y < robotPosY + RobotManager.getRobotHeight(); ++y){
				if(!mapTraversed.contains(XYToId(x, y))){
					mapTraversed.add(XYToId(x, y));
				}
			}
		}
		
//		ArrayList<Enum<MOVE>> moves = new ArrayList<>();
//		
//		
//		moves.add(attemptEast());
//		moves.add(attemptNorth());
//		moves.add(attemptSouth());
//		moves.add(attemptWest());
//		
//		int index = 0;
//		
//		for(Enum<MOVE> move : moves){
//			if(move == MOVE.UNTRAVERSED)
//		}
		untraversed = null;
		
		nextMove = attemptEast();
		if (nextMove == MOVE.NO_MOVE) {
			nextMove = attemptNorth();
			if (nextMove == MOVE.NO_MOVE) {
				nextMove = attemptSouth();
				if (nextMove == MOVE.NO_MOVE) {
					nextMove = attemptWest();
					if(nextMove == MOVE.NO_MOVE){
						nextMove = untraversed;
						if(nextMove == null){
							nextMove = backTrack();
						}
					}
				}
			}
		}
		System.out.println(callStack);
		return nextMove;
	}

	@Override
	public int movesToStartZone() {
		// TODO Auto-generated method stub
		return 0;
	}
	
//	private void initialiseUntraversed(){
//		int index;
//		for(index = 0; index < untraversed.length; ++index)
//			untraversed[index] = false;
//	}
}