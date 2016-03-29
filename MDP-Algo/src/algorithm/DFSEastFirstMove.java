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