package algorithm;

import java.util.Hashtable;
import java.util.Random;

public class SimpleMove implements Movable {

	private static final int STEP_LIMIT = 70;
	private static int current_move = 1;

	
	@Override
	public int nextMove() {

		int nextMove;
		if (current_move == STEP_LIMIT) {
			nextMove = STOP;
			current_move = 1;
		} else {
			Random rand = new Random();
			nextMove = rand.nextInt(4);
//			textualOutput("Move " + current_move + ": " + )
			++current_move;
		}
		return nextMove;
	}

	@Override
	public void getMapUpdate(Hashtable<Integer, Integer> senseUpResult) {
		

	}

	@Override
	public String textualOutput(String output) {
		return output;
	}

}
