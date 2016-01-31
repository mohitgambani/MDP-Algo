package ui;

import java.util.ArrayList;

import algorithm.Movable;
import algorithm.SimpleMove;

public class MapManager {
	protected static final int MAP_WIDTH = 20;
	protected static final int MAP_HEIGHT = 15;

	private static final int START_ZONE_WIDTH = 3;
	private static final int START_ZONE_HEIGHT = 3;
	private static final int GOAL_ZONE_WIDTH = 3;
	private static final int GOAL_ZONE_HEIGHT = 3;
	private static final int ROBOT_WIDTH = 2;
	private static final int ROBOT_HEIGHT = 2;

	protected static final int HEAD_UP = 0;
	protected static final int HEAD_DOWN = 1;
	protected static final int HEAD_LEFT = 2;
	protected static final int HEAD_RIGHT = 3;

	private static int robotUpLeft = 0;

	protected static ArrayList<MapComponent> humanMapComponents = new ArrayList<MapComponent>();
	protected static ArrayList<MapComponent> robotMapComponents = new ArrayList<MapComponent>();

	private static Movable moveStrategy = new SimpleMove();

	protected static void drawStartZone() {
		final int START_X = 0;
		final int START_Y = 0;

		int x, y;
		for (x = START_X; x < START_ZONE_WIDTH; ++x) {
			for (y = START_Y; y < START_ZONE_HEIGHT; ++y) {
				humanMapComponents.get(XYToId(x, y)).setStartZone();
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
			}
		}
	}

	protected static void setRobot(int robotId, final int ROBOT_DIRECTION) {
		int x, y;
		boolean failed = false;
		ArrayList<MapComponent> robotComponents = new ArrayList<MapComponent>();

		for (x = idToX(robotId); x < idToX(robotId) + ROBOT_WIDTH && !failed; ++x) {
			for (y = idToY(robotId); y < idToY(robotId) + ROBOT_HEIGHT && !failed; ++y) {
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
			robotUpLeft = robotId;
			switch (ROBOT_DIRECTION) {
			case HEAD_UP:
				robotHeadUp();
				break;
			case HEAD_DOWN:
				robotHeadDown();
				break;
			case HEAD_LEFT:
				robotHeadLeft();
				break;
			case HEAD_RIGHT:
				robotHeadRight();
				break;
			}
		}
	}

	protected static void unSetRobot() {
		int x, y;

		for (x = idToX(robotUpLeft); x < idToX(robotUpLeft) + ROBOT_WIDTH; ++x) {
			for (y = idToY(robotUpLeft); y < idToY(robotUpLeft) + ROBOT_HEIGHT; ++y) {
				humanMapComponents.get(XYToId(x, y)).unSetIsRobot();
			}
		}
	}

	protected static void moveLeft() {
		robotHeadLeft();
		setRobot(XYToId(idToX(robotUpLeft) - 1, idToY(robotUpLeft)), HEAD_LEFT);
	}

	protected static void moveRight() {
		robotHeadRight();
		setRobot(XYToId(idToX(robotUpLeft) + 1, idToY(robotUpLeft)), HEAD_RIGHT);
	}

	protected static void moveUp() {
		robotHeadUp();
		setRobot(XYToId(idToX(robotUpLeft), idToY(robotUpLeft) - 1), HEAD_UP);
	}

	protected static void moveDown() {
		robotHeadDown();
		setRobot(XYToId(idToX(robotUpLeft), idToY(robotUpLeft) + 1), HEAD_DOWN);
	}

	protected static void move() {
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

	protected static void robotHeadUp() {
		int upLeft;
		for (upLeft = idToX(robotUpLeft); upLeft < idToX(robotUpLeft) + ROBOT_WIDTH; ++upLeft) {
			humanMapComponents.get(XYToId(upLeft, idToY(robotUpLeft))).setRobotHead();
			humanMapComponents.get(XYToId(upLeft, idToY(robotUpLeft) + ROBOT_HEIGHT - 1)).setIsRobot();
		}
	}

	protected static void robotHeadDown() {
		int upLeft;
		for (upLeft = idToX(robotUpLeft); upLeft < idToX(robotUpLeft) + ROBOT_WIDTH; ++upLeft) {
			humanMapComponents.get(XYToId(upLeft, idToY(robotUpLeft) + ROBOT_HEIGHT - 1)).setRobotHead();
			humanMapComponents.get(XYToId(upLeft, idToY(robotUpLeft))).setIsRobot();
		}
	}

	protected static void robotHeadLeft() {
		int upLeft;
		for (upLeft = idToY(robotUpLeft); upLeft < idToY(robotUpLeft) + ROBOT_HEIGHT; ++upLeft) {
			humanMapComponents.get(XYToId(idToX(robotUpLeft), upLeft)).setRobotHead();
			humanMapComponents.get(XYToId(idToX(robotUpLeft) + ROBOT_WIDTH - 1, upLeft)).setIsRobot();
		}
	}

	protected static void robotHeadRight() {
		int upLeft;
		for (upLeft = idToY(robotUpLeft); upLeft < idToY(robotUpLeft) + ROBOT_HEIGHT; ++upLeft) {
			humanMapComponents.get(XYToId(idToX(robotUpLeft) + ROBOT_WIDTH - 1, upLeft)).setRobotHead();
			humanMapComponents.get(XYToId(idToX(robotUpLeft), upLeft)).setIsRobot();
		}
	}

	private static int idToX(int id) {
		return id % MAP_WIDTH;
	}

	private static int idToY(int id) {
		return id / MAP_WIDTH;
	}

	private static int XYToId(int x, int y) {
		return y * MAP_WIDTH + x;
	}

	private static boolean isOutBoundary(int x, int y, final int WIDTH, final int HEIGHT) {
		return (x >= WIDTH) || (x < 0) || (y >= HEIGHT) || (y < 0);
	}
}
