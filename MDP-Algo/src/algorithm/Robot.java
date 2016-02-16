package algorithm;


public class Robot {
	private final int ROBOT_WIDTH = 2;
	private final int ROBOT_HEIGHT = 2;
	
	private final int FRONT_SENSING_RANGE = 1;
	private final int SIDE_SENSING_RANGE = 1;
	

	public static enum ORIENTATION{
		NORTH, SOUTH, EAST, WEST
	}
	
	private int positionX, positionY;
	private Enum<ORIENTATION> orientation;
	private Movable moveStrategy = null;
	
	public Robot(int posX, int posY, Enum<ORIENTATION> ori){
		setPositionX(posX);
		setPositionY(posY);
		setOrientation(ori);
	}
	
	public void startExploration(){
		moveStrategy = new FloodFillMove();
//		moveStrategy = new SimpleMove();
		Thread thread = new Thread() {
			@Override
			public void run() {
				Enum<Movable.MOVE> nextMove = Movable.MOVE.STOP;
				do {
					if (orientation == Robot.ORIENTATION.NORTH || orientation == Robot.ORIENTATION.SOUTH) {
						senseNorth();
						senseSouth();
					} else {
						senseWest();
						senseEast();
					}
					nextMove = moveStrategy.nextMove();
					if (nextMove == Movable.MOVE.EAST) {
						moveEast();
					} else if (nextMove == Movable.MOVE.WEST) {
						moveWest();
					} else if (nextMove == Movable.MOVE.NORTH) {
						moveNorth();
					} else if (nextMove == Movable.MOVE.SOUTH) {
						moveSouth();
					} else if( nextMove == Movable.MOVE.TURN_EAST){
						headEast();
					} else if( nextMove == Movable.MOVE.TURN_WEST){
						headWest();
					}else if(nextMove == Movable.MOVE.TURN_NORTH){
						headNorth();
					}else if(nextMove == Movable.MOVE.TURN_SOUTH){
						headSouth();
					}
				} while (nextMove != Movable.MOVE.STOP);
			}
		};
		thread.start();
	}
	
	public int getPositionX(){
		return positionX;
	}
	public int getPositionY(){
		return positionY;
	}
	public void setPositionX(int posX){
		positionX = posX;
	}
	public void setPositionY(int posY){
		positionY = posY;
	}
	public Enum<ORIENTATION> getOrientation(){
		return orientation;
	}
	public void setOrientation(Enum<ORIENTATION> ori){
		orientation = ori;
	}
//	public void setMoveStrategy(Movable strategy){
//		moveStrategy = strategy;
//	}
	public void getMapUpdate(int id, Enum<Movable.GRID_TYPE> type){
		moveStrategy.getMapUpdate(id, type);
	}
	public int getWidth(){
		return ROBOT_WIDTH;
	}
	public int getHeight(){
		return ROBOT_HEIGHT;
	}
	public int getFrontSensingRange(){
		return FRONT_SENSING_RANGE;
	}
	public int getSideSensingRange(){
		return SIDE_SENSING_RANGE;
	}
	
	private void moveNorth() {
		RobotManager.moveNorth();
	}
	private void moveSouth() {
		RobotManager.moveSouth();
	}
	private void moveWest() {
		RobotManager.moveWest();
	}
	private void moveEast() {
		RobotManager.moveEast();
	}
	
	private void headNorth(){
		RobotManager.headNorth();
	}
	private void headSouth(){
		RobotManager.headSouth();
	}
	private void headWest(){
		RobotManager.headWest();
	}
	private void headEast(){
		RobotManager.headEast();
	}

	private void senseNorth() {
		RobotManager.senseNorth();
	}

	private void senseSouth() {
		RobotManager.senseSouth();
	}

	private void senseWest() {
		RobotManager.senseWest();
	}

	private void senseEast() {
		RobotManager.senseEast();
	}
}
