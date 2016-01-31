package ui;

import java.util.ArrayList;
import java.util.Hashtable;

import algorithm.Movable;
import algorithm.SimpleMove;

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

	protected static ArrayList<MapComponent> humanMapComponents = new ArrayList<MapComponent>();
	protected static ArrayList<MapComponent> robotMapComponents = new ArrayList<MapComponent>();

	protected static void drawStartZone() {
		final int START_X = 0;
		final int START_Y = 0;

		int x, y;
		for (x = START_X; x < START_ZONE_WIDTH; ++x) {
			for (y = START_Y; y < START_ZONE_HEIGHT; ++y) {
				humanMapComponents.get(XYToId(x, y)).setStartZone();
				robotMapComponents.get(XYToId(x, y)).setStartZone();
			}
		}
	}

	protected static void drawGoalZone() {
		final int GOAL_X = MAP_WIDTH - 1;
		final int GOAL_Y = MAP_HEIGHT - 1;

		int x, y;
		for (x = GOAL_X; x >= MAP_WIDTH - GOAL_ZONE_WIDTH; --x) {
			for (y = GOAL_Y; y >= MAP_HEIGHT - GOAL_ZONE_HEIGHT; --y) {
				humanMapComponents.get(XYToId(x, y)).setGoalZone();
				robotMapComponents.get(XYToId(x, y)).setGoalZone();
			}
		}
	}

	protected static void setRobot(int robotId, final int ROBOT_DIRECTION) {
		int x, y;
		boolean failed = false;
		ArrayList<MapComponent> robotComponents = new ArrayList<MapComponent>();

		for (x = idToX(robotId); x < idToX(robotId) + RobotManager.ROBOT_WIDTH && !failed; ++x) {
			for (y = idToY(robotId); y < idToY(robotId) + RobotManager.ROBOT_HEIGHT && !failed; ++y) {
				if (isOutBoundary(x, y, MAP_WIDTH / 2, MAP_HEIGHT)) {
					failed = true;
				} else if (humanMapComponents.get(XYToId(x, y)).isObstacle()) {
					failed = true;
				} else {
					robotComponents.add(humanMapComponents.get(XYToId(x, y)));
				}
			}
		}
		if (!failed) {
			unSetRobot();
			RobotManager.setRobotUpLeft(robotId);
			switch (ROBOT_DIRECTION) {
			case RobotManager.HEAD_UP:
				robotHeadUp();
				break;
			case RobotManager.HEAD_DOWN:
				robotHeadDown();
				break;
			case RobotManager.HEAD_LEFT:
				robotHeadLeft();
				break;
			case RobotManager.HEAD_RIGHT:
				robotHeadRight();
				break;
			}
		}
	}

	protected static void unSetRobot() {
		int x, y;
		int robotUpLeft = RobotManager.getRobotUpLeft();
		for (x = idToX(robotUpLeft); x < idToX(robotUpLeft) + RobotManager.ROBOT_WIDTH; ++x) {
			for (y = idToY(robotUpLeft); y < idToY(robotUpLeft) + RobotManager.ROBOT_HEIGHT; ++y) {
				humanMapComponents.get(XYToId(x, y)).unSetIsRobot();
			}
		}
		RobotManager.setRobotUpLeft(0);
	}

	protected static void moveLeft() {
		int robotUpLeft = RobotManager.getRobotUpLeft();
		robotHeadLeft();
		setRobot(XYToId(idToX(robotUpLeft) - 1, idToY(robotUpLeft)), RobotManager.HEAD_LEFT);
	}

	protected static void moveRight() {
		int robotUpLeft = RobotManager.getRobotUpLeft();
		robotHeadRight();
		setRobot(XYToId(idToX(robotUpLeft) + 1, idToY(robotUpLeft)), RobotManager.HEAD_RIGHT);
	}

	protected static void moveUp() {
		int robotUpLeft = RobotManager.getRobotUpLeft();
		robotHeadUp();
		setRobot(XYToId(idToX(robotUpLeft), idToY(robotUpLeft) - 1), RobotManager.HEAD_UP);
	}

	protected static void moveDown() {
		int robotUpLeft = RobotManager.getRobotUpLeft();
		robotHeadDown();
		setRobot(XYToId(idToX(robotUpLeft), idToY(robotUpLeft) + 1), RobotManager.HEAD_DOWN);
	}

	protected static void move() {
		RobotManager.move();
	}

	protected static void robotHeadUp() {
		int upLeft;
		int robotUpLeft = RobotManager.getRobotUpLeft();
		for (upLeft = idToX(robotUpLeft); upLeft < idToX(robotUpLeft) + RobotManager.ROBOT_WIDTH; ++upLeft) {
			humanMapComponents.get(XYToId(upLeft, idToY(robotUpLeft))).setRobotHead();
			humanMapComponents.get(XYToId(upLeft, idToY(robotUpLeft) + RobotManager.ROBOT_HEIGHT - 1)).setIsRobot();
		}
		RobotManager.setRobotOrientation(RobotManager.HEAD_UP);
	}

	protected static void robotHeadDown() {
		int upLeft;
		int robotUpLeft = RobotManager.getRobotUpLeft();
		for (upLeft = idToX(robotUpLeft); upLeft < idToX(robotUpLeft) + RobotManager.ROBOT_WIDTH; ++upLeft) {
			humanMapComponents.get(XYToId(upLeft, idToY(robotUpLeft) + RobotManager.ROBOT_HEIGHT - 1)).setRobotHead();
			humanMapComponents.get(XYToId(upLeft, idToY(robotUpLeft))).setIsRobot();
		}
		RobotManager.setRobotOrientation(RobotManager.HEAD_DOWN);
	}

	protected static void robotHeadLeft() {
		int upLeft;
		int robotUpLeft = RobotManager.getRobotUpLeft();
		for (upLeft = idToY(robotUpLeft); upLeft < idToY(robotUpLeft) + RobotManager.ROBOT_HEIGHT; ++upLeft) {
			humanMapComponents.get(XYToId(idToX(robotUpLeft), upLeft)).setRobotHead();
			humanMapComponents.get(XYToId(idToX(robotUpLeft) + RobotManager.ROBOT_WIDTH - 1, upLeft)).setIsRobot();
		}
		RobotManager.setRobotOrientation(RobotManager.HEAD_LEFT);
	}

	protected static void robotHeadRight() {
		int upLeft;
		int robotUpLeft = RobotManager.getRobotUpLeft();
		for (upLeft = idToY(robotUpLeft); upLeft < idToY(robotUpLeft) + RobotManager.ROBOT_HEIGHT; ++upLeft) {
			humanMapComponents.get(XYToId(idToX(robotUpLeft) + RobotManager.ROBOT_WIDTH - 1, upLeft)).setRobotHead();
			humanMapComponents.get(XYToId(idToX(robotUpLeft), upLeft)).setIsRobot();
		}
		RobotManager.setRobotOrientation(RobotManager.HEAD_RIGHT);
	}

	protected static Hashtable<Integer, Integer> robotSensing(int startX, int startY, int xLimit, int yLimit) {
		int x, y;
		Hashtable<Integer, Integer> results = new Hashtable<Integer, Integer>();
		for (x = startX; x < startX + xLimit; ++x) {
			for (y = startY; y < startY + yLimit; ++y) {
				if (isOutBoundary(x, y, MAP_WIDTH, MAP_HEIGHT)) {
					results.put(-1, MAP_WALL);
				} else if (humanMapComponents.get(XYToId(x, y)).isObstacle()) {
					results.put(XYToId(x, y), MAP_OBSTACLE);
					robotMapComponents.get(XYToId(x, y)).setObstacle();
				} else if (humanMapComponents.get(XYToId(x, y)).isGoalZone()) {
					results.put(XYToId(x, y), MAP_GOALZONE);
				} else if (humanMapComponents.get(XYToId(x, y)).isStartZone()) {
					results.put(XYToId(x, y), MAP_STARTZONE);
				} else {
					results.put(XYToId(x, y), MAP_OPENSPACE);
					robotMapComponents.get(XYToId(x, y)).unSetObstacle();
				}
			}
		}
		return results;
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
