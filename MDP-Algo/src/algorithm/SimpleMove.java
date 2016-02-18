package algorithm;

import java.util.Random;


public class SimpleMove extends Movable {

	private static final int STEP_LIMIT = 70;
	private static int current_move = 1;

	
	@Override
	public Enum<Movable.MOVE> nextMove() {

		Enum<Movable.MOVE> nextMove = MOVE.STOP;
		if (current_move == STEP_LIMIT) {
			nextMove = MOVE.STOP;
			current_move = 1;
		} else {
			Random rand = new Random();
			do{
				nextMove = Movable.MOVE.values()[rand.nextInt(Movable.MOVE.values().length)];
			}while(nextMove == Movable.MOVE.STOP);
			++current_move;
		}
		return nextMove;
	}

	@Override
	public void getMapUpdate(int id, Enum<GRID_TYPE> type) {
		super.getMapUpdate(id, type);
		System.out.println(getMapExplored());
	}

	@Override
	public int movesToStartZone() {
		// TODO Auto-generated method stub
		return 0;
	}

}
