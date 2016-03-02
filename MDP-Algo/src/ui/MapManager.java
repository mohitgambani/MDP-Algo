package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import algorithm.RobotManager;
import algorithm.SimulatedSensor;
import io.FileIOManager;

public class MapManager {
	public static final int MAP_WIDTH = 20;
	public static final int MAP_HEIGHT = 15;

	private static final int START_ZONE_WIDTH = 3;
	private static final int START_ZONE_HEIGHT = 3;
	private static final int GOAL_ZONE_WIDTH = 3;
	private static final int GOAL_ZONE_HEIGHT = 3;
	private static final int START_X = 0;
	private static final int START_Y = 0;
	private static final int GOAL_X = MAP_WIDTH - 1;
	private static final int GOAL_Y = MAP_HEIGHT - 1;

	protected static ArrayList<MapComponent> humanMap = new ArrayList<MapComponent>();
	protected static ArrayList<MapComponent> robotMap = new ArrayList<MapComponent>();

	protected static void drawStartZone() {
		int x, y;
		for (x = START_X; x < START_ZONE_WIDTH; ++x) {
			for (y = START_Y; y < START_ZONE_HEIGHT; ++y) {
				humanMap.get(XYToId(x, y)).setStartZone();
			}
		}
	}

	protected static void drawGoalZone() {
		int x, y;
		for (x = GOAL_X; x >= MAP_WIDTH - GOAL_ZONE_WIDTH; --x) {
			for (y = GOAL_Y; y >= MAP_HEIGHT - GOAL_ZONE_HEIGHT; --y) {
				humanMap.get(XYToId(x, y)).setGoalZone();
			}
		}
	}

	public static void initialiseRobot(int id) {
		int posX = idToX(id), posY = idToY(id);
		int x, y;
		boolean failed = false;
		
		for (x = posX; x < posX + RobotManager.ROBOT_WIDTH && !failed; ++x) {
			for (y = posY; y < posY + RobotManager.ROBOT_HEIGHT && !failed; ++y) {
				if (x >= MAP_WIDTH / 2 || MapManager.isObstacle(x, y)) {
					failed = true;
				}
			}
		}
		if (!failed) {
			RobotManager.initialiseRobot(posX, posY);
		}
	}

	public static void unsetRobot() {
		int x, y;

		for (x = RobotManager.getRobotPositionX(); x < RobotManager.getRobotPositionX()
				+ RobotManager.ROBOT_WIDTH; ++x) {
			for (y = RobotManager.getRobotPositionY(); y < RobotManager.getRobotPositionY()
					+ RobotManager.ROBOT_HEIGHT; ++y) {
				humanMap.get(XYToId(x, y)).unsetRobot();
			}
		}
	}

	public static void headWest() {
		robotTurn(RobotManager.getRobotPositionX(), -1);
		pause();
	}

	public static void headEast() {
		robotTurn(RobotManager.getRobotPositionX() + RobotManager.ROBOT_WIDTH - 1, -1);
		pause();
	}

	public static void headNorth() {
		robotTurn(-1, RobotManager.getRobotPositionY());
		pause();
	}

	public static void headSouth() {
		robotTurn(-1, RobotManager.getRobotPositionY() + RobotManager.ROBOT_HEIGHT - 1);
		pause();
	}

	protected static void startExploration() {
		Thread thread = new Thread(){
			@Override
			public void run(){
				RobotManager.setSensor(new SimulatedSensor());
				RobotManager.startExploration();
			}
		};
		thread.start();
	}
	
	protected static void startFastestRun() {
		Thread thread = new Thread(){
			@Override
			public void run(){
				RobotManager.startFastestRun();
			}
		};
		thread.start();
	}

	/**
	 * The real function that draws the robot
	 * @param robotHeadX
	 * @param robotHeadY
	 */
	private static void robotTurn(int robotHeadX, int robotHeadY) {
		int x, y;
		for (x = RobotManager.getRobotPositionX(); x < RobotManager.getRobotPositionX()
				+ RobotManager.ROBOT_WIDTH; ++x) {
			for (y = RobotManager.getRobotPositionY(); y < RobotManager.getRobotPositionY()
					+ RobotManager.ROBOT_HEIGHT; ++y) {
				if (y == robotHeadY || x == robotHeadX) {
					humanMap.get(XYToId(x, y)).setRobotHead();
				} else {
					humanMap.get(XYToId(x, y)).setRobot();
				}
			}
		}
	}

	protected static void resetMap() {
		for (MapComponent mapComponent : humanMap) {
			mapComponent.reset();
		}
		for (MapComponent mapComponent : robotMap) {
			mapComponent.reset();
		}
		drawStartZone();
		drawGoalZone();
		RobotManager.reset();
	}

	protected static void setObstacle(int id) {
		humanMap.get(id).setObstacle();
	}
	
	protected static void setOpenSpace(int id){
		humanMap.get(id).reset();
	}

	protected static void unsetObstacle(int id) {
		humanMap.get(id).unsetObstacle();
	}

	public static boolean isObstacle(int x, int y) {
		return humanMap.get(XYToId(x, y)).isObstacle();
	}

	public static void setObstacle(int x, int y) {
		robotMap.get(XYToId(x, y)).setObstacle();
	}

	public static void setOpenSpace(int x, int y) {
		robotMap.get(XYToId(x, y)).reset();
	}

	public static void setMapExplored(int x, int y) {
		robotMap.get(XYToId(x, y)).setExplored();
	}

	public static void readMap() {
		String map = "";
		try {
			map = FileIOManager.readFile();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		int index;
		for (index = 0; index < MAP_HEIGHT * MAP_WIDTH; ++index) {
			if (index < map.length()) {
				if (map.charAt(index) == '1') {
					setObstacle(index);
				} else {
					setOpenSpace(index);;
				}
			} else {
				setOpenSpace(index);
			}
		}
		drawStartZone();
		drawGoalZone();
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

	private static void pause() {
		try {
			Thread.sleep(1000 / RobotManager.getMovePerSecond());
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
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