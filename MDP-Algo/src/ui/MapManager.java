package ui;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class MapManager {
	protected static final int MAP_WIDTH = 20;
	protected static final int MAP_HEIGHT = 15;

	private static final int START_ZONE_WIDTH = 3;
	private static final int START_ZONE_HEIGHT = 3;
	private static final int GOAL_ZONE_WIDTH = 3;
	private static final int GOAL_ZONE_HEIGHT = 3;

	public static final int MAP_WALL = 1;
	public static final int MAP_OBSTACLE = 2;
	public static final int MAP_OPENSPACE = 3;
	public static final int MAP_STARTZONE = 4;
	public static final int MAP_GOALZONE = 5;

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
				robotMap.get(XYToId(x, y)).setStartZone();
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
				robotMap.get(XYToId(x, y)).setGoalZone();
			}
		}
	}

	protected static void initialiseRobot(int robotUpLeft) {

		if (idToX(robotUpLeft) <= MAP_WIDTH / 2 - RobotManager.getRobotWidth()) {
			if (setRobot(robotUpLeft))
				robotHeadUp();
		}
	}

	protected static boolean setRobot(int robotUpLeft) {
		int x, y;
		boolean failed = false;

		for (x = idToX(robotUpLeft); x < idToX(robotUpLeft) + RobotManager.getRobotWidth() && !failed; ++x) {
			for (y = idToY(robotUpLeft); y < idToY(robotUpLeft) + RobotManager.getRobotHeight() && !failed; ++y) {
				if (isOutBoundary(x, y, MAP_WIDTH, MAP_HEIGHT) || humanMap.get(XYToId(x, y)).isObstacle()) {
					failed = true;
				}
			}
		}
		if (!failed) {
			unSetRobot();
			RobotManager.setRobotUpLeft(robotUpLeft);
		}
		return !failed;
	}

	protected static void unSetRobot() {
		int x, y;
		int robotUpLeft = RobotManager.getRobotUpLeft();

		for (x = idToX(robotUpLeft); x < idToX(robotUpLeft) + RobotManager.getRobotWidth(); ++x) {
			for (y = idToY(robotUpLeft); y < idToY(robotUpLeft) + RobotManager.getRobotHeight(); ++y) {
				humanMap.get(XYToId(x, y)).unSetIsRobot();
			}
		}

	}

	protected static void moveLeft() {
		int robotUpLeft = RobotManager.getRobotUpLeft();

		setRobot(XYToId(idToX(robotUpLeft) - 1, idToY(robotUpLeft)));
		robotHeadLeft();
	}

	protected static void moveRight() {
		int robotUpLeft = RobotManager.getRobotUpLeft();

		setRobot(XYToId(idToX(robotUpLeft) + 1, idToY(robotUpLeft)));
		robotHeadRight();
	}

	protected static void moveUp() {
		int robotUpLeft = RobotManager.getRobotUpLeft();

		setRobot(XYToId(idToX(robotUpLeft), idToY(robotUpLeft) - 1));
		robotHeadUp();
	}

	protected static void moveDown() {
		int robotUpLeft = RobotManager.getRobotUpLeft();
		setRobot(XYToId(idToX(robotUpLeft), idToY(robotUpLeft) + 1));
		robotHeadDown();
	}

	protected static void startExploration() {
		RobotManager.startExploration();
	}

	protected static void robotTurn(int robotHeadX, int robotHeadY) {
		int robotUpLeft = RobotManager.getRobotUpLeft();
		int x, y;
		for (x = idToX(robotUpLeft); x < idToX(robotUpLeft) + RobotManager.getRobotWidth(); ++x) {
			for (y = idToY(robotUpLeft); y < idToY(robotUpLeft) + RobotManager.getRobotHeight(); ++y) {
				if (y == robotHeadY || x == robotHeadX) {
					humanMap.get(XYToId(x, y)).setRobotHead();
				} else {
					humanMap.get(XYToId(x, y)).setIsRobot();
				}
			}
		}
	}

	protected static void robotHeadUp() {
		robotTurn(-1, idToY(RobotManager.getRobotUpLeft()));
		RobotManager.setRobotOrientation(RobotManager.HEAD_UP);
	}

	protected static void robotHeadDown() {
		robotTurn(-1, idToY(RobotManager.getRobotUpLeft()) + RobotManager.getRobotHeight() - 1);
		RobotManager.setRobotOrientation(RobotManager.HEAD_DOWN);
	}

	protected static void robotHeadLeft() {
		robotTurn(idToX(RobotManager.getRobotUpLeft()), -1);
		RobotManager.setRobotOrientation(RobotManager.HEAD_LEFT);
	}

	protected static void robotHeadRight() {
		robotTurn(idToX(RobotManager.getRobotUpLeft()) + RobotManager.getRobotWidth() - 1, -1);
		RobotManager.setRobotOrientation(RobotManager.HEAD_RIGHT);
	}

	protected static Hashtable<Integer, Integer> robotSensing(int startX, int startY, int xLimit, int yLimit) {
		int x, y;
		Hashtable<Integer, Integer> results = new Hashtable<Integer, Integer>();
		for (x = startX; x < startX + xLimit; ++x) {
			for (y = startY; y < startY + yLimit; ++y) {
				if (isOutBoundary(x, y, MAP_WIDTH, MAP_HEIGHT)) {
					results.put(-1, MAP_WALL);
				} else if (humanMap.get(XYToId(x, y)).isObstacle()) {
					results.put(XYToId(x, y), MAP_OBSTACLE);
					robotMap.get(XYToId(x, y)).setObstacle();
					if (!humanMap.get(XYToId(x, y)).isExplored()) {
						++obstaclesExplored;
						humanMap.get(XYToId(x, y)).setIsExplored(true);
						RobotManager.getExplorationPercentage(1.0 * obstaclesExplored / numOfObstacles);
					}
				} else if (humanMap.get(XYToId(x, y)).isGoalZone()) {
					results.put(XYToId(x, y), MAP_GOALZONE);
				} else if (humanMap.get(XYToId(x, y)).isStartZone()) {
					results.put(XYToId(x, y), MAP_STARTZONE);
				} else {
					results.put(XYToId(x, y), MAP_OPENSPACE);
					robotMap.get(XYToId(x, y)).setOpenSpace();
				}
			}
		}
		return results;
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
		RobotManager.setRobot(false);
		numOfObstacles = 0;
		obstaclesExplored = 0;
		RobotManager.getExplorationPercentage(0.0);
	}

	protected static void setObstacle(int id) {
		humanMap.get(id).setOpenSpace();
		humanMap.get(id).setObstacle();
		++numOfObstacles;
		RobotManager.getExplorationPercentage(1.0 * obstaclesExplored / numOfObstacles);
	}

	protected static void unsetObstacle(int id) {
		if (humanMap.get(id).isExplored())
			--obstaclesExplored;
		humanMap.get(id).setOpenSpace();
		--numOfObstacles;
		try {
			RobotManager.getExplorationPercentage(1.0 * obstaclesExplored / numOfObstacles);
		} catch (ArithmeticException ex) {
			RobotManager.getExplorationPercentage(0.0);
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

	private static boolean isOutBoundary(int x, int y, final int WIDTH, final int HEIGHT) {
		return (x >= WIDTH) || (x < 0) || (y >= HEIGHT) || (y < 0);
	}

}
