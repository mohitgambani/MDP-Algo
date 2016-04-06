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

	private static SensorDecoder sensorDecoder = null;
	
	private static String fastestRunMoveSequence = "";
	private static int currentMovePos = 0;

	private static Timer timer;
	private static final int TIMER_DELAY = 1;
	private static long startTime;
	private static long timeElapsed;

	private static long timeLimit = 0;
	private static double percentageLimit = 100.0;

	private static int moveCounter = 0;

	private static boolean isDelay = true;

	public static void setRobot(int posX, int posY, ORIENTATION ori) {
		unsetRobot();
		positionX = posX;
		positionY = posY;
		orientation = ori;
		switch (ori) {
		case NORTH:
			headNorth(isDelay);
			break;
		case SOUTH:
			headSouth(isDelay);
			break;
		case EAST:
			headEast(isDelay);
			break;
		case WEST:
			headWest(isDelay);
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
//		explorationStrategy = new DFSEastFirstMove();
		explorationStrategy = new DFSSouthFirstMove();
		addInitialRobotToMapExplored();
		timer.start();
		moveCounter = 0;
		MOVE nextMove = MOVE.STOP;
		sensorDecoder = new SensorDecoder();
		do {
			String sensingInfo = SimulatedSensor.getSensoryInfo();
			sensorDecoder.getReadingsFromExt(sensingInfo);
			
//			TCPClientManager.sendMessage("B" + sensingInfo);
			
			getNewSensoryInfo();
			nextMove = explorationStrategy.nextMove();
			decodeMove(nextMove);
			++moveCounter;
			
//			TCPClientManager.sendMessage("B" + nextMoveStr);
			
			displayExplorationPercentage();
			displayMoves(nextMove, moveCounter);
//			System.out.println(nextMove);
		} while (nextMove != Movable.MOVE.STOP);
		timer.stop();
		writeMap();
	}

	public static void startExploration(String sensorData) {
		sensorDecoder.getReadingsFromExt(sensorData);
		getNewSensoryInfo();
		MOVE nextMove = explorationStrategy.nextMove();
		String nextMoveStr = decodeMove(nextMove);
		TCPClientManager.sendMessage("A" + nextMoveStr);
		++moveCounter;
		displayExplorationPercentage();
		displayMoves(nextMove, moveCounter);
		if (nextMove == MOVE.STOP) {
			timer.stop();
			writeMap();
		}
	}

	public static void initialiseRealExploration() {
		sensorDecoder = new SensorDecoder();
//		explorationStrategy = new DFSEastFirstMove();
		explorationStrategy = new DFSSouthFirstMove();
		moveCounter = 0;
		MainControl.mainWindow.setFreeOutput("---Exploration Started---\n");
		MainControl.mainWindow.setFreeOutput("---Waiting for Sensors---\n");
		addInitialRobotToMapExplored();
		initialiseTimer();
		timer.start();
	}

	private static String decodeMove(MOVE move) {
		String result = "";
		switch (move) {
		case EAST_R:
			moveEast(orientation);
			result = "B";
			break;
		case EAST:
			if (orientation == ORIENTATION.NORTH) {
				result = "RF"; // right forward
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "LF"; // left forward
			} else if (orientation == ORIENTATION.WEST) {
				result = "RRF"; // right right forward
			} else {
				result = "F"; // forward
			}
			moveEast();
			break;
		case TURN_EAST_B:
		case TURN_EAST_M:
		case TURN_EAST:
			if (orientation == ORIENTATION.NORTH) {
				result = "R"; // right
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "L"; // left
			} else if (orientation == ORIENTATION.WEST) {
				result = "RR"; // right right
			}
			headEast(isDelay);
			break;
		case SOUTH_R:
			result = "B";
			moveSouth(orientation);
			break;
		case SOUTH:
			if (orientation == ORIENTATION.EAST) {
				result = "RF";
			} else if (orientation == ORIENTATION.NORTH) {
				result = "RRF";
			} else if (orientation == ORIENTATION.WEST) {
				result = "LF";
			} else {
				result = "F";
			}
			moveSouth();
			break;
		case TURN_SOUTH_B:
		case TURN_SOUTH_M:
		case TURN_SOUTH:
			if (orientation == ORIENTATION.EAST) {
				result = "R";
			} else if (orientation == ORIENTATION.WEST) {
				result = "L";
			} else if (orientation == ORIENTATION.NORTH) {
				result = "RR";
			}
			headSouth(isDelay);
			break;
		case NORTH_R:
			result = "B";
			moveNorth(orientation);
			break;
		case NORTH:
			if (orientation == ORIENTATION.EAST) {
				result = "LF";
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "RRF";
			} else if (orientation == ORIENTATION.WEST) {
				result = "RF";
			} else {
				result = "F";
			}

			moveNorth();
			break;
		case TURN_NORTH_B:
		case TURN_NORTH_M:
		case TURN_NORTH:
			if (orientation == ORIENTATION.EAST) {
				result = "L";
			} else if (orientation == ORIENTATION.WEST) {
				result = "R";
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "RR";
			}
			headNorth(isDelay);
			break;
		case WEST_R:
			result = "B";
			moveWest(orientation);
			break;
		case WEST:
			if (orientation == ORIENTATION.EAST) {
				result = "RRF";
			} else if (orientation == ORIENTATION.NORTH) {
				result = "LF";
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "RF";
			} else {
				result = "F";
			}
			moveWest();
			break;
		case TURN_WEST_B:
		case TURN_WEST_M:
		case TURN_WEST:
			if (orientation == ORIENTATION.NORTH) {
				result = "L";
			} else if (orientation == ORIENTATION.SOUTH) {
				result = "R";
			} else if (orientation == ORIENTATION.EAST) {
				result = "RR";
			}
			headWest(isDelay);
			break;
		case STOP:
			result = "STOP";
		default:
			break;
		}
		return result;
	}

	public static void startFastestRun() {
		fastestRunStrategy = new ShortestPath1(explorationStrategy.getMapExplored());
		MainControl.mainWindow.setFreeOutput("---Fastest Run Started---\n");
		initialiseTimer();
		timer.start();
		moveCounter = 0;
		MOVE nextMove = MOVE.STOP;
		do {
			nextMove = fastestRunStrategy.nextMove();
			String nextMoveStr = decodeMove(nextMove);
			TCPClientManager.sendMessage("A" + nextMoveStr);
			++moveCounter;
			displayMoves(nextMove, moveCounter);
		} while (nextMove != Movable.MOVE.STOP);
		timer.stop();
	}

	public static void startRealFastestRun(){
		String nextMoveStr = "";
		do{
			nextMoveStr += fastestRunMoveSequence.substring(currentMovePos, currentMovePos + 1);
			System.out.println(nextMoveStr + "," + nextMoveStr.contains("F"));
			++currentMovePos;
			System.out.println(fastestRunMoveSequence.substring(currentMovePos, currentMovePos + 1).equals("F"));
		}while(nextMoveStr.contains("F") && fastestRunMoveSequence.substring(currentMovePos, currentMovePos + 1).equals("F"));
		
		if(nextMoveStr.length() == 1){
			TCPClientManager.sendMessage("A" + nextMoveStr);
		}else{
			TCPClientManager.sendMessage("A" + mapStrtoSymbol(nextMoveStr));
		}
	}
	
	private static String mapStrtoSymbol(String nextMoveStr){
		if(nextMoveStr.length() < 10){
			return "" + nextMoveStr.length();
		}else{
			switch (nextMoveStr.length()) {
			case 10:
				return ")";
			case 11:
				return "!";
			case 12:
				return "@";
			case 13:
				return "#";
			case 14:
				return "$";
			case 15:
				return "%";
			case 16:
				return "^";
			case 17:
				return "&";
			case 18:
				return "*";
			case 19:
				return "(";
			default:
				break;
			}
		}
		return "";
	}

	public static void initialiseFastestRun() {
		fastestRunStrategy = new ShortestPath1(explorationStrategy.getMapExplored());
		MainControl.mainWindow.setFreeOutput("---Fastest Run Started---\n");
		fastestRunMoveSequence = "";
		MOVE nextMove;
		do{
			nextMove = fastestRunStrategy.nextMove();
			fastestRunMoveSequence += decodeMove(nextMove);
		}while(nextMove != MOVE.STOP);
		System.out.println(fastestRunMoveSequence);
//		initialiseTimer();
//		timer.start();
//		moveCounter = 0;
	}

	public static void getNewSensoryInfo() {
		Hashtable<Integer, Movable.GRID_TYPE> results = sensorDecoder.getSensoryInfo();
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

	private static void headWest(boolean delay) {
		orientation = ORIENTATION.WEST;
		MapManager.headWest(delay);
	}

	private static void headEast(boolean delay) {
		orientation = ORIENTATION.EAST;
		MapManager.headEast(delay);
	}

	private static void headNorth(boolean delay) {
		orientation = ORIENTATION.NORTH;
		MapManager.headNorth(delay);
	}

	private static void headSouth(boolean delay) {
		orientation = ORIENTATION.SOUTH;
		MapManager.headSouth(delay);
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

//	private static String convertMove(MOVE move) {
//		String result = "STOP";
//		if (move == Movable.MOVE.EAST) {
//			if (orientation == ORIENTATION.NORTH) {
//				result = "RF"; // right forward
//			} else if (orientation == ORIENTATION.SOUTH) {
//				result = "LF"; // left forward
//			} else if (orientation == ORIENTATION.WEST) {
//				result = "RRF"; // right right forward
//			} else {
//				result = "F"; // forward
//			}
//		} else if (move == Movable.MOVE.EAST_R) {
//			result = "B"; // back
//		} else if (move == Movable.MOVE.TURN_EAST) {
//			if (orientation == ORIENTATION.NORTH) {
//				result = "R"; // right
//			} else if (orientation == ORIENTATION.SOUTH) {
//				result = "L"; // left
//			} else if (orientation == ORIENTATION.WEST) {
//				result = "RR"; // right right
//			}
//		} else if (move == Movable.MOVE.NORTH) {
//			if (orientation == ORIENTATION.EAST) {
//				result = "LF";
//			} else if (orientation == ORIENTATION.SOUTH) {
//				result = "RRF";
//			} else if (orientation == ORIENTATION.WEST) {
//				result = "RF";
//			} else {
//				result = "F";
//			}
//		} else if (move == Movable.MOVE.NORTH_R) {
//			result = "B";
//		} else if (move == Movable.MOVE.TURN_NORTH) {
//			if (orientation == ORIENTATION.EAST) {
//				result = "L";
//			} else if (orientation == ORIENTATION.WEST) {
//				result = "R";
//			} else if (orientation == ORIENTATION.SOUTH) {
//				result = "RR";
//			}
//		} else if (move == Movable.MOVE.SOUTH) {
//			if (orientation == ORIENTATION.EAST) {
//				result = "RF";
//			} else if (orientation == ORIENTATION.NORTH) {
//				result = "RRF";
//			} else if (orientation == ORIENTATION.WEST) {
//				result = "LF";
//			} else {
//				result = "F";
//			}
//		} else if (move == Movable.MOVE.SOUTH_R) {
//			result = "B";
//		} else if (move == Movable.MOVE.TURN_SOUTH) {
//			if (orientation == ORIENTATION.EAST) {
//				result = "R";
//			} else if (orientation == ORIENTATION.WEST) {
//				result = "L";
//			} else if (orientation == ORIENTATION.NORTH) {
//				result = "RR";
//			}
//		} else if (move == Movable.MOVE.WEST) {
//			if (orientation == ORIENTATION.EAST) {
//				result = "RRF";
//			} else if (orientation == ORIENTATION.NORTH) {
//				result = "LF";
//			} else if (orientation == ORIENTATION.SOUTH) {
//				result = "RF";
//			} else {
//				result = "F";
//			}
//		} else if (move == Movable.MOVE.WEST_R) {
//			result = "B";
//		} else if (move == Movable.MOVE.TURN_WEST) {
//			if (orientation == ORIENTATION.NORTH) {
//				result = "L";
//			} else if (orientation == ORIENTATION.SOUTH) {
//				result = "R";
//			} else if (orientation == ORIENTATION.EAST) {
//				result = "RR";
//			}
//		}
//		return result;
//	}

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
		TCPClientManager.sendMessage("B" + fullMapToWrite + "\n\n" + exploredMapToWrite);
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
		return result.toUpperCase();
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

	public static int XYToId(int x, int y) {
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

	public static int idToX(int id) {
		return id % MAP_WIDTH;
	}

	public static int idToY(int id) {
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

	public static void setIsDelay(boolean delay) {
		isDelay = delay;
	}
}