package algorithm;

import java.util.ArrayList;
import java.util.Random;

public class SimpleMove implements Movable {

	private static int current_move = 0;
	private static ArrayList<Integer> moves = new ArrayList<Integer>();

	public SimpleMove() {
		int counter = 10;
		Random rand = new Random();

		while (counter > 0) {
			moves.add(rand.nextInt(4));
			--counter;
		}
		moves.add(STOP);
	}

	@Override
	public int nextMove() {
		return moves.get(current_move++);
	}

	@Override
	public void getMapUpdate() {
		// TODO Auto-generated method stub

	}

}
