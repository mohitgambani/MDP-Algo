package ui;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import algorithm.Movable;
import algorithm.Robot;
import algorithm.RobotManager;

public class MapManager {
	public static final int MAP_WIDTH = 20;
	public static final int MAP_HEIGHT = 15;

	private static final int START_ZONE_WIDTH = 3;
	private static final int START_ZONE_HEIGHT = 3;
	private static final int GOAL_ZONE_WIDTH = 3;
	private static final int GOAL_ZONE_HEIGHT = 3;

//	public static final int MAP_WALL = 1;
//	public static final int MAP_OBSTACLE = 2;
//	public static final int MAP_OPENSPACE = 3;
//	public static final int MAP_STARTZONE = 4;
//	public static final int MAP_GOALZONE = 5;

	protected static ArrayList<MapComponent> humanMap = new ArrayList<MapComponent>();
	protected static ArrayList<MapComponent> robotMap = new ArrayList<MapComponent>();

	private static int numOfObstacles = 0;
	private static int obstaclesExplored = 0;

	protected static void drawStartZone() {
		final int START_X = 0;
		final int START_Y = 0;

		int x, y;
		for (x = START_X; x < START_ZONE_WIDTH; ++x) {
			for (y = START_Y; y < START_ZONE_HEIGHT; ++y) {
				humanMap.get(XYToId(x, y)).setOpenSpace();
				humanMap.get(XYToId(x, y)).setStartZone();
				robotMap.get(XYToId(x, y)).setOpenSpace();
			}
		}
	}

	protected static void drawGoalZone() {
		final int GOAL_X = MAP_WIDTH - 1;
		final int GOAL_Y = MAP_HEIGHT - 1;

		int x, y;
		for (x = GOAL_X; x >= MAP_WIDTH - GOAL_ZONE_WIDTH; --x) {
			for (y = GOAL_Y; y >= MAP_HEIGHT - GOAL_ZONE_HEIGHT; --y) {
				humanMap.get(XYToId(x, y)).setOpenSpace();
				humanMap.get(XYToId(x, y)).setGoalZone();
				robotMap.get(XYToId(x, y)).setOpenSpace();
			}
		}
	}

	protected static void initialiseRobot(int robotPosition) {

		if (idToX(robotPosition) <= MAP_WIDTH / 2 - RobotManager.getRobotWidth()) {
			if (setRobot(robotPosition))
				headNorth();
		}
	}

	protected static boolean setRobot(int robotPosition) {
		int x, y;
		boolean failed = false;

//		for (x = idToX(robotPosition); x < idToX(robotPosition) + RobotManager.getRobotWidth() && !failed; ++x) {
//			for (y = idToY(robotPosition); y < idToY(robotPosition) + RobotManager.getRobotHeight() && !failed; ++y) {
//				if (isOutBoundary(x, y, MAP_WIDTH, MAP_HEIGHT) || humanMap.get(XYToId(x, y)).isObstacle()) {
//					failed = true;
//				}
//			}
//		}
//		if (!failed) {
//			unSetRobot();
			RobotManager.setRobot(idToX(robotPosition), idToY(robotPosition), Robot.ORIENTATION.NORTH);
//		}
		return !failed;
	}

	public static void unSetRobot() {
		int x,y;

		for (x = RobotManager.getRobotPositionX(); x < RobotManager.getRobotPositionX() + RobotManager.getRobotWidth(); ++x) {
			for (y = RobotManager.getRobotPositionY(); y < RobotManager.getRobotPositionY() + RobotManager.getRobotHeight(); ++y) {
				humanMap.get(XYToId(x, y)).unSetIsRobot();
			}
		}
	}

	public static void headWest() {

		robotTurn(RobotManager.getRobotPositionX(), -1);
		RobotManager.setRobotOrientation(Robot.ORIENTATION.WEST);
		pause();
	}

	public static void headEast() {

		robotTurn(RobotManager.getRobotPositionX() + RobotManager.getRobotWidth() - 1, -1);
		RobotManager.setRobotOrientation(Robot.ORIENTATION.EAST);
		pause();
	}

	public static void headNorth() {

		robotTurn(-1, RobotManager.getRobotPositionY());
		RobotManager.setRobotOrientation(Robot.ORIENTATION.NORTH);
		pause();
	}

	public static void headSouth() {

		robotTurn(-1, RobotManager.getRobotPositionY() + RobotManager.getRobotHeight() - 1);
		RobotManager.setRobotOrientation(Robot.ORIENTATION.SOUTH);
		pause();
	}

	protected static void startExploration() {
		RobotManager.startExploration();
	}

	protected static void robotTurn(int robotHeadX, int robotHeadY) {
		int x, y;
		for (x = RobotManager.getRobotPositionX(); x < RobotManager.getRobotPositionX()
				+ RobotManager.getRobotWidth(); ++x) {
			for (y = RobotManager.getRobotPositionY(); y < RobotManager.getRobotPositionY()
					+ RobotManager.getRobotHeight(); ++y) {
				if (y == robotHeadY || x == robotHeadX) {
					humanMap.get(XYToId(x, y)).setRobotHead();
				} else {
					humanMap.get(XYToId(x, y)).setIsRobot();
				}
			}
		}
	}

//	protected static void robotHeadNorth() {
//		robotTurn(-1, RobotManager.getRobotPositionY());
//		
//	}
//
//	protected static void robotHeadSouth() {
//		robotTurn(-1, RobotManager.getRobotPositionY() + RobotManager.getRobotHeight() - 1);
//		
//	}
//
//	protected static void robotHeadWest() {
//		robotTurn(RobotManager.getRobotPositionX(), -1);
//		
//	}
//
//	protected static void robotHeadEast() {
//		robotTurn(RobotManager.getRobotPositionX() + RobotManager.getRobotWidth() - 1, -1);
//		
//	}

//	public static Hashtable<Integer, Movable.GRID_TYPE> robotSensing(int startX,
//			int startY, int xLimit, int yLimit) {
//		int x, y;
//		Hashtable<Integer, Movable.GRID_TYPE> results = new Hashtable<Integer, Movable.GRID_TYPE>();
//		for (x = startX; x < startX + xLimit; ++x) {
//			for (y = startY; y < startY + yLimit; ++y) {
//				if (isOutBoundary(x, y, MAP_WIDTH, MAP_HEIGHT)) {
////					results.put(-1, Movable.GRID_TYPE.WALL);
//				} else if (humanMap.get(XYToId(x, y)).isObstacle()) {
////					addExploredSpace(XYToId(x, y));
//					results.put(XYToId(x, y), Movable.GRID_TYPE.OBSTACLE);
//					robotMap.get(XYToId(x, y)).setObstacle();
//					if (!humanMap.get(XYToId(x, y)).isExplored()) {
//						++obstaclesExplored;
//						humanMap.get(XYToId(x, y)).setIsExplored(true);
////						RobotManager.getExplorationPercentage(1.0
////								* obstaclesExplored / numOfObstacles);
//					}
////				} else if (humanMap.get(XYToId(x, y)).isGoalZone()) {
////					results.put(XYToId(x, y), MAP_GOALZONE);
////					robotMap.get(XYToId(x, y)).setNotAnObstacleColour();
////					addExploredSpace(XYToId(x, y));
////				} else if (humanMap.get(XYToId(x, y)).isStartZone()) {
////					results.put(XYToId(x, y), MAP_STARTZONE);
////					robotMap.get(XYToId(x, y)).setNotAnObstacleColour();
////					addExploredSpace(XYToId(x, y));
//				} else {
//					results.put(XYToId(x, y), Movable.GRID_TYPE.OPEN_SPACE);
//					robotMap.get(XYToId(x, y)).setOpenSpace();
////					robotMap.get(XYToId(x, y)).setNotAnObstacleColour();
//					robotMap.get(XYToId(x, y)).setIsExplored(true);
////					addExploredSpace(XYToId(x, y));
//				}
//			}
//		}
//		return results;
//	}
	
//	public static void addExploredSpace(int id){
//		if(exploredSpaces.contains(id) == false){
//			exploredSpaces.add(id);
//		}
//	}

	protected static void resetMap() {
		for (MapComponent mapComponent : humanMap) {
			mapComponent.setOpenSpace();
		}
		for (MapComponent mapComponent : robotMap) {
			mapComponent.setOpenSpace();
		}
		drawStartZone();
		drawGoalZone();
		numOfObstacles = 0;
		obstaclesExplored = 0;
		RobotManager.setRobot(0, 0, Robot.ORIENTATION.NORTH);
//		exploredSpaces.clear();
//		RobotManager.getExplorationPercentage(0.0);
	}

	protected static void setObstacle(int id) {
		humanMap.get(id).setOpenSpace();
		humanMap.get(id).setObstacle();
		++numOfObstacles;
//		RobotManager.getExplorationPercentage(1.0 * obstaclesExplored / numOfObstacles);
	}

	protected static void unsetObstacle(int id) {
		if (humanMap.get(id).isExplored())
			--obstaclesExplored;
		humanMap.get(id).setOpenSpace();
		--numOfObstacles;
//		try {
//			RobotManager.getExplorationPercentage(1.0 * obstaclesExplored / numOfObstacles);
//		} catch (ArithmeticException ex) {
//			RobotManager.getExplorationPercentage(0.0);
//		}
	}
	
	public static boolean isObstacle(int x, int y){
		return humanMap.get(XYToId(x, y)).isObstacle();
	}
	
	public static void setRobotMapObstacle(int x, int y){
		robotMap.get(XYToId(x, y)).setObstacle();
	}
	
	public static void setRobotMapOpenSpace(int x, int y){
		robotMap.get(XYToId(x, y)).setOpenSpace();
	}
	
	public static void setRobotMapExplored(int x, int y){
		robotMap.get(XYToId(x, y)).setIsExplored();
	}
	
	private static void pause() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	protected static void generateMap() {
		resetMap();
		Random rand = new Random();
		int numOfObstacles = rand.nextInt(MAP_WIDTH * MAP_HEIGHT / 12) + MAP_WIDTH * MAP_HEIGHT / 12;
		while (numOfObstacles > 0) {
			setObstacle(rand.nextInt(MAP_WIDTH * MAP_HEIGHT));
			--numOfObstacles;
		}
		drawStartZone();
		drawGoalZone();
	}

	protected static int idToX(int id) {
		return id % MAP_WIDTH;
	}

	protected static int idToY(int id) {
		return id / MAP_WIDTH;
	}

	protected static int XYToId(int x, int y) {
		return y * MAP_WIDTH + x;
	}

//	private static boolean isOutBoundary(int x, int y, final int WIDTH, final int HEIGHT) {
//		return (x >= WIDTH) || (x < 0) || (y >= HEIGHT) || (y < 0);
//	}

}
