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

	protected static void setRobot(int robotId) {
		int x, y;
		boolean failed = false;
		ArrayList<MapComponent> robotComponents = new ArrayList<MapComponent>();

		for (x = idToX(robotId); x < idToX(robotId) + ROBOT_WIDTH && !failed; ++x) {
			for (y = idToY(robotId); y < idToY(robotId) + ROBOT_HEIGHT && !failed; ++y) {
				if (isOutBoundary(x, y)) {
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
			for (MapComponent robotComponent : robotComponents) {
				robotComponent.setIsRobot();
			}
			robotUpLeft = robotId;
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
		setRobot(XYToId(idToX(robotUpLeft) - 1, idToY(robotUpLeft)));
	}

	protected static void moveRight() {
		setRobot(XYToId(idToX(robotUpLeft) + 1, idToY(robotUpLeft)));
	}

	protected static void moveUp() {
		setRobot(XYToId(idToX(robotUpLeft), idToY(robotUpLeft) - 1));
	}

	protected static void moveDown() {
		setRobot(XYToId(idToX(robotUpLeft), idToY(robotUpLeft) + 1));
	}

	protected static void move() {
		moveStrategy = new SimpleMove();
		int nextMove = moveStrategy.nextMove();

		while (nextMove != moveStrategy.STOP) {
			if (nextMove == moveStrategy.LEFT) {
				moveLeft();
			} else if (nextMove == moveStrategy.RIGHT) {
				moveRight();
			} else if (nextMove == moveStrategy.UP) {
				moveUp();
			} else if (nextMove == moveStrategy.DOWN) {
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

	private static int idToX(int id) {
		return id % MAP_WIDTH;
	}

	private static int idToY(int id) {
		return id / MAP_WIDTH;
	}

	private static int XYToId(int x, int y) {
		return y * MAP_WIDTH + x;
	}

	private static boolean isOutBoundary(int x, int y) {
		return (x >= MAP_WIDTH) || (x < 0) || (y >= MAP_HEIGHT) || (y < 0);
	}
}
