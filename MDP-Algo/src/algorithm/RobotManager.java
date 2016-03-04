package algorithm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.Timer;

import algorithm.Movable.GRID_TYPE;
import algorithm.Movable.MOVE;
import io.FileIOManager;
import io.TCPClientManager;
import ui.MainControl;
import ui.MapManager;

public class RobotManager {

	public static final int MAP_WIDTH = MapManager.MAP_WIDTH;
	public static final int MAP_HEIGHT = MapManager.MAP_HEIGHT;

	public static final int ROBOT_WIDTH = 3;
	public static final int ROBOT_HEIGHT = 3;

	private static int movesPerSecond = 10;

	public static enum ORIENTATION {
		NORTH, SOUTH, EAST, WEST
	}

	private static int positionX, positionY;
	private static ORIENTATION orientation;

	private static Movable explorationStrategy = null;
	private static Movable fastestRunStrategy = null;

	private static Sensor sensor = null;

	private static Timer timer;
	private static final int TIMER_DELAY = 1;
	private static long startTime;
	private static long timeElapsed;

	private static long timeLimit = 0;
	private static double percentageLimit = 100.0;

	private static int moveCounter = 0;

	public static void setRobot(int posX, int posY, ORIENTATION ori) {
		unsetRobot();
		positionX = posX;
		positionY = posY;
		orientation = ori;
		switch (ori) {
		case NORTH:
			headNorth();
			break;
		case SOUTH:
			headSouth();
			break;
		case EAST:
			headEast();
			break;
		case WEST:
			headWest();
			break;
		}
		MainControl.mainWindow.setRobotPosition(posX + "," + posY);
	}

	private static void unsetRobot() {
		MapManager.unsetRobot();
	}

	public static void startExploration() {
		timeLimit = MainControl.mainWindow.getTimeLimit();
		MainControl.mainWindow.setFreeOutput("---Exploration Started---\n");
		initialiseTimer(timeLimit);
//		sensor = new SimulatedSensor();
		explorationStrategy = new FloodFillMove();
		addInitialRobotToMapExplored();
		timer.start();
		moveCounter = 0;
		MOVE nextMove = MOVE.STOP;
		do {
			getSensoryInfo();
			nextMove = explorationStrategy.nextMove();
			// NetworkIOManager.sendMessage("A" + convertMove(nextMove));
			decodeMove(nextMove);
			++moveCounter;
			displayExplorationPercentage();
			displayMoves(nextMove, moveCounter);
		} while (nextMove != Movable.MOVE.STOP);
		timer.stop();
		writeMap();
	}

	public static void startExploration(String sensorData) {
		sensor.getReadingsFromExt(sensorData);
		getSensoryInfo();
		MOVE nextMove = explorationStrategy.nextMove();
		TCPClientManager.sendMessage("A" + convertMove(nextMove));
		decodeMove(nextMove);
		++moveCounter;
		displayExplorationPercentage();
		displayMoves(nextMove, moveCounter);
		if(nextMove == MOVE.STOP){
			timer.stop();
			writeMap();
		}
	}

	public static void initialiseRealExploration() {
		sensor = new SensorDecoder();
		explorationStrategy = new FloodFillMove();
		moveCounter = 0;
//		movesPerSecond = 10;
		MainControl.mainWindow.setFreeOutput("---Exploration Started---\n");
		MainControl.mainWindow.setFreeOutput("---Waiting for Sensors---\n");
		addInitialRobotToMapExplored();
//		timeLimit = MainControl.mainWindow.getTimeLimit();
		initialiseTimer();
		timer.start();
	}

	private static void decodeMove(MOVE move) {
		switch (move) {
		case EAST_R:
			moveEast(orientation);
			break;
		case EAST:
			moveEast();
			break;
		case TURN_EAST:
			headEast();
			break;
		case SOUTH_R:
			moveSouth(orientation);
			break;
		case SOUTH:
			moveSouth();
			break;
		case TURN_SOUTH:
			headSouth();
			break;
		case NORTH_R:
			moveNorth(orientation);
			break;
		case NORTH:
			moveNorth();
			break;
		case TURN_NORTH:
			headNorth();
			break;
		case WEST_R:
			moveWest(orientation);
			break;
		case WEST:
			moveWest();
			break;
		case TURN_WEST:
			headWest();
		default:
			break;
		}
	}

	public static void startFastestRun() {
		fastestRunStrategy = new ShortestPath(explorationStrategy.getMapExplored());
		MainControl.mainWindow.setFreeOutput("---Fastest Run Started---\n");
		initialiseTimer();
		timer.start();
		moveCounter = 0;
		MOVE nextMove = MOVE.STOP;
		do {
			nextMove = fastestRunStrategy.nextMove();
			TCPClientManager.sendMessage("A" + convertMove(nextMove));
			decodeMove(nextMove);
			++moveCounter;
			displayMoves(nextMove, moveCounter);
		} while (nextMove != Movable.MOVE.STOP);
		timer.stop();
	}

	public static void getSensoryInfo() {
		Hashtable<Integer, Movable.GRID_TYPE> results = sensor.getSensoryInfo();
		Enumeration<Integer> keys = results.keys();
		while (keys.hasMoreElements()) {
			Integer key = keys.nextElement();
			GRID_TYPE type = results.get(key);
			if (type == GRID_TYPE.OPEN_SPACE) {
				MapManager.setMapExplored(idToX(key), idToY(key));
			} else {
				MapManager.setObstacle(idToX(key), idToY(key));
			}
			explorationStrategy.getMapUpdate(key, type);
		}
	}

	private static void headWest() {
		orientation = ORIENTATION.WEST;
		MapManager.headWest();
	}

	private static void headEast() {
		orientation = ORIENTATION.EAST;
		MapManager.headEast();
	}

	private static void headNorth() {
		orientation = ORIENTATION.NORTH;
		MapManager.headNorth();
	}

	private static void headSouth() {
		orientation = ORIENTATION.SOUTH;
		MapManager.headSouth();
	}

	private static void moveWest(ORIENTATION orientation) {
		unsetRobot();
		setRobot(positionX - 1, positionY, orientation);
	}

	private static void moveEast(ORIENTATION orientation) {
		unsetRobot();
		setRobot(positionX + 1, positionY, orientation);
	}

	private static void moveNorth(ORIENTATION orientation) {
		unsetRobot();
		setRobot(positionX, positionY - 1, orientation);
	}

	private static void moveSouth(ORIENTATION orientation) {
		unsetRobot();
		setRobot(positionX, positionY + 1, orientation);
	}

	private static void moveWest() {
		moveWest(ORIENTATION.WEST);
	}

	private static void moveEast() {
		moveEast(ORIENTATION.EAST);
	}

	private static void moveNorth() {
		moveNorth(ORIENTATION.NORTH);
	}

	private static void moveSouth() {
		moveSouth(ORIENTATION.SOUTH);
	}

	private static String convertMove(MOVE move) {
		String result = "STOP";
		if (move == Movable.MOVE.EAST) {
			if (orientation == ORIENTATION.NORTH) {
				result = "RF"; // right forward
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "LF"; // left forward
			} else if (orientation == ORIENTATION.WEST) {
				result = "RRF"; // right right forward
			} else {
				result = "F"; // forward
			}
		} else if (move == Movable.MOVE.EAST_R) {
			result = "B"; // back
		} else if (move == Movable.MOVE.TURN_EAST) {
			if (orientation == ORIENTATION.NORTH) {
				result = "R"; // right
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "L"; // left
			} else if (orientation == ORIENTATION.WEST) {
				result = "RR"; // right right
			}
		} else if (move == Movable.MOVE.NORTH) {
			if (orientation == ORIENTATION.EAST) {
				result = "LF";
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "RRF";
			} else if (orientation == ORIENTATION.WEST) {
				result = "RF";
			} else {
				result = "F";
			}
		} else if (move == Movable.MOVE.NORTH_R) {
			result = "B";
		} else if (move == Movable.MOVE.TURN_NORTH) {
			if (orientation == ORIENTATION.EAST) {
				result = "L";
			} else if (orientation == ORIENTATION.WEST) {
				result = "R";
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "RR";
			}
		} else if (move == Movable.MOVE.SOUTH) {
			if (orientation == ORIENTATION.EAST) {
				result = "RF";
			} else if (orientation == ORIENTATION.NORTH) {
				result = "RRF";
			} else if (orientation == ORIENTATION.WEST) {
				result = "LF";
			} else {
				result = "F";
			}
		} else if (move == Movable.MOVE.SOUTH_R) {
			result = "B";
		} else if (move == Movable.MOVE.TURN_SOUTH) {
			if (orientation == ORIENTATION.EAST) {
				result = "R";
			} else if (orientation == ORIENTATION.WEST) {
				result = "L";
			} else if (orientation == ORIENTATION.NORTH) {
				result = "RR";
			}
		} else if (move == Movable.MOVE.WEST) {
			if (orientation == ORIENTATION.EAST) {
				result = "RRF";
			} else if (orientation == ORIENTATION.NORTH) {
				result = "LF";
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "RF";
			} else {
				result = "F";
			}
		} else if (move == Movable.MOVE.WEST_R) {
			result = "B";
		} else if (move == Movable.MOVE.TURN_WEST) {
			if (orientation == ORIENTATION.NORTH) {
				result = "L";
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "R";
			} else if (orientation == ORIENTATION.EAST) {
				result = "RR";
			}
		}
		return result;
	}

	private static void writeMap() {
		Hashtable<Integer, GRID_TYPE> map = explorationStrategy.getMapExplored();
		int x, y;
		String fullMapToWrite = "11";
		String exploredMapToWrite = "";

		for (x = 0; x < MAP_WIDTH; ++x) {
			for (y = x; y <= x + (MAP_HEIGHT - 1) * MAP_WIDTH; y += MAP_WIDTH) {
				if (map.containsKey(y)) {
					fullMapToWrite += "1";
					if (map.get(y) == Movable.GRID_TYPE.OBSTACLE) {
						exploredMapToWrite += "1";
					} else {
						exploredMapToWrite += "0";
					}
				} else {
					fullMapToWrite += "0";
				}
			}
		}
		fullMapToWrite += "11";
		while (exploredMapToWrite.length() % 8 != 0) {
			exploredMapToWrite += "0";
		}
		fullMapToWrite = binaryToHexa(fullMapToWrite);
		exploredMapToWrite = binaryToHexa(exploredMapToWrite);
		System.out.println(fullMapToWrite);
		System.out.println(exploredMapToWrite);
		try {
			FileIOManager.writeFile(fullMapToWrite, "fullMap");
			FileIOManager.writeFile(exploredMapToWrite, "exploredMap");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static String binaryToHexa(String binary) {
		int index;
		String result = "";
		for (index = 0; index < binary.length(); index += 4) {
			result += Integer.toString(Integer.parseInt(binary.substring(index, index + 4), 2), 16);
		}
		return result;
	}

	public static void reset() {
		MainControl.mainWindow.setRobotPosition("unknown");
		MainControl.mainWindow.setTimerDisplay(String.format("%d min %d s %d ms", 0, 0, 0));
		MainControl.mainWindow.setMapExplored(String.format("Map Explored: %.2f%%", 0.0));
		MainControl.mainWindow.clearFreeOutput();
	}

	private static void displayMoves(Enum<Movable.MOVE> nextMove, int counter) {
		MainControl.mainWindow.setFreeOutput(String.format("%d %s\n", counter, nextMove.toString()));
	}

	protected static void displayExplorationPercentage() {
		MainControl.mainWindow.setMapExplored(String.format("Map Explored: %.2f%%", getPercentageExplored()));
	}

	private static double getPercentageExplored() {
		return 100.0 * explorationStrategy.numOfExploredSpace() / (MAP_WIDTH * MAP_HEIGHT);
	}

	protected static int XYToId(int x, int y) {
		return y * MAP_WIDTH + x;
	}

	public static void setTimeLimit(long tLimit) {
		timeLimit = tLimit;
	}

	public static void setPercentageLimit(double pLimit) {
		percentageLimit = pLimit;
	}

	private static void addInitialRobotToMapExplored() {
		int x, y;
		for (x = positionX; x < positionX + ROBOT_WIDTH; ++x) {
			for (y = positionY; y < positionY + ROBOT_HEIGHT; ++y) {
				explorationStrategy.getMapUpdate(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
				MapManager.setMapExplored(x, y);
			}
		}
	}

	public static int getMovePerSecond() {
		return movesPerSecond;
	}

	public static void setMovePerSecond(int speed) {
		movesPerSecond = speed;
	}

	private static int idToX(int id) {
		return id % MAP_WIDTH;
	}

	private static int idToY(int id) {
		return id / MAP_WIDTH;
	}

	public static void initialiseRobot(int posX, int posY) {
		setRobot(posX, posY, ORIENTATION.EAST);
	}

	public static void initialiseRobot(String content) {
		int robotIndex = Integer.parseInt(content.substring(0, 3));
		int robotOrientation = Integer.parseInt(content.substring(3, 4));
		ORIENTATION ori = ORIENTATION.EAST;
		switch (robotOrientation) {
		case 0:
			ori = ORIENTATION.NORTH;
			break;
		case 1:
			ori = ORIENTATION.EAST;
			break;
		case 2:
			ori = ORIENTATION.SOUTH;
			break;
		case 3:
			ori = ORIENTATION.WEST;
			break;
		}
		setRobot(idToX(robotIndex) - 1, idToY(robotIndex) - 1, ori);
	}

	public static void setSensor(Sensor newSensor) {
		sensor = newSensor;
	}

	public static Sensor getSensor() {
		return sensor;
	}

	public static boolean isOutBoundary(int x, int y) {
		return (x >= MAP_WIDTH) || (x < 0) || (y >= MAP_HEIGHT) || (y < 0);
	}

	private static void initialiseTimer(long timeLimit) {
		timeElapsed = 0;
		MainControl.mainWindow.setTimerDisplay(String.format("%d min %d s %d ms", timeElapsed / 1000 / 60,
				timeElapsed / 1000 % 60, timeElapsed % 1000));
		startTime = System.currentTimeMillis();
		timer = new Timer(TIMER_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeElapsed = System.currentTimeMillis() - startTime;
				if (timeElapsed
						+ Math.ceil(1.0 * explorationStrategy.movesToStartZone() / movesPerSecond) * 1000 >= timeLimit
						|| getPercentageExplored() >= percentageLimit)
					explorationStrategy.setConditionalStop();
				MainControl.mainWindow.setTimerDisplay(String.format("%d min %d s %d ms", timeElapsed / 1000 / 60,
						timeElapsed / 1000 % 60, timeElapsed % 1000));
			}
		});
	}

	private static void initialiseTimer() {
		timeElapsed = 0;
		MainControl.mainWindow.setTimerDisplay(String.format("%d min %d s %d ms", timeElapsed / 1000 / 60,
				timeElapsed / 1000 % 60, timeElapsed % 1000));
		startTime = System.currentTimeMillis();
		timer = new Timer(TIMER_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeElapsed = System.currentTimeMillis() - startTime;
				MainControl.mainWindow.setTimerDisplay(String.format("%d min %d s %d ms", timeElapsed / 1000 / 60,
						timeElapsed / 1000 % 60, timeElapsed % 1000));
			}
		});
	}

	public static int getRobotPositionX() {
		return positionX;
	}

	public static int getRobotPositionY() {
		return positionY;
	}

	public static ORIENTATION getRobotOrientation() {
		return orientation;
	}
}