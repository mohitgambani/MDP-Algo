package ui;

import java.util.ArrayList;
import java.util.Random;
import algorithm.RobotManager;

public class MapManager {
	public static final int MAP_WIDTH = 20;
	public static final int MAP_HEIGHT = 15;

	private static final int START_ZONE_WIDTH = 3;
	private static final int START_ZONE_HEIGHT = 3;
	private static final int GOAL_ZONE_WIDTH = 3;
	private static final int GOAL_ZONE_HEIGHT = 3;

	protected static ArrayList<MapComponent> humanMap = new ArrayList<MapComponent>();
	protected static ArrayList<MapComponent> robotMap = new ArrayList<MapComponent>();

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
			setRobot(robotPosition);
			headEast();
		}
	}

	protected static void setRobot(int robotPosition) {
		RobotManager.setRobot(idToX(robotPosition), idToY(robotPosition), RobotManager.ORIENTATION.EAST);
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
		pause();
	}

	public static void headEast() {
		robotTurn(RobotManager.getRobotPositionX() + RobotManager.getRobotWidth() - 1, -1);
		pause();
	}

	public static void headNorth() {
		robotTurn(-1, RobotManager.getRobotPositionY());
		pause();
	}

	public static void headSouth() {
		robotTurn(-1, RobotManager.getRobotPositionY() + RobotManager.getRobotHeight() - 1);
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

	protected static void resetMap() {
		for (MapComponent mapComponent : humanMap) {
			mapComponent.setOpenSpace();
		}
		for (MapComponent mapComponent : robotMap) {
			mapComponent.setOpenSpace();
		}
		drawStartZone();
		drawGoalZone();
		RobotManager.reset();
	}

	protected static void setObstacle(int id) {
		humanMap.get(id).setOpenSpace();
		humanMap.get(id).setObstacle();
	}

	protected static void unsetObstacle(int id) {
		humanMap.get(id).setOpenSpace();
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

}
