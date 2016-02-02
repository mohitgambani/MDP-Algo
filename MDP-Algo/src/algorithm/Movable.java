package algorithm;

import java.util.Hashtable;

public interface Movable {
	public final int UP = 0;
	public final int DOWN = 1;
	public final int LEFT = 2;
	public final int RIGHT = 3;
	public final int STOP = -1;
	
//	public enum Movement {
//		UP, DOWN, LEFT, RIGHT, STOP
//	}

	public int nextMove();

	public void getMapUpdate(Hashtable<Integer, Integer> results);
	
	public String textualOutput(String output);
}
