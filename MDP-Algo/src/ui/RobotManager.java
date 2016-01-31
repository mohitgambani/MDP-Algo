package ui;

import algorithm.Movable;
import algorithm.SimpleMove;

public class RobotManager {
	protected static final int HEAD_UP = 0;
	protected static final int HEAD_DOWN = 1;
	protected static final int HEAD_LEFT = 2;
	protected static final int HEAD_RIGHT = 3;

	private static int robotUpLeft = 0;
	private static int robotOrientation = HEAD_UP;
	
	private static Movable moveStrategy = new SimpleMove();
	
	public static int getRobotUpLeft(){
		return robotUpLeft;
	}
	public static void setRobotUpLeft(int upLeft){
		robotUpLeft = upLeft;
	}
	public static int getRobotOrientation(){
		return robotOrientation;
	}
	public static void setRobotOrientation(int orientation){
		robotOrientation = orientation;
	}
	public static void move(){
		moveStrategy = new SimpleMove();
		int nextMove = moveStrategy.nextMove();

		while (nextMove != Movable.STOP) {
			if (nextMove == Movable.LEFT) {
				moveLeft();
			} else if (nextMove == Movable.RIGHT) {
				moveRight();
			} else if (nextMove == Movable.UP) {
				moveUp();
			} else if (nextMove == Movable.DOWN) {
				moveDown();
			}
			nextMove = moveStrategy.nextMove();
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	private static void moveLeft(){
		MapManager.moveLeft();
	}
	private static void moveRight(){
		MapManager.moveRight();
	}
	private static void moveUp(){
		MapManager.moveUp();
	}
	private static void moveDown(){
		MapManager.moveDown();
	}
}
