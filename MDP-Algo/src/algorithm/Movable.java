package algorithm;

public interface Movable {
	public final int UP = 0;
	public final int DOWN = 1;
	public final int LEFT = 2;
	public final int RIGHT = 3;
	public final int STOP = -1;

	public int nextMove();

	public void getMapUpdate();
}
