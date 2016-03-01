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
import io.NetworkIOManager;
import ui.MainControl;
import ui.MapManager;
import ui.SensorManager;

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

	private static Timer timer;
	private static final int TIMER_DELAY = 1;
	private static long startTime;
	private static long timeElapsed;

	private static long timeLimit = 0;
	private static double percentageLimit = 100.0;

	/***
	 * To set the robot's position, without drawing it
	 * 
	 * @param posX
	 * @param posY
	 * @param ori
	 */
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

	public static int getRobotPositionX() {
		return positionX;
	}

	public static int getRobotPositionY() {
		return positionY;
	}

	public static ORIENTATION getRobotOrientation() {
		return orientation;
	}

	public static void startExploration() {
		timeLimit = MainControl.mainWindow.getTimeLimit();
		startTime = System.currentTimeMillis();
		explorationStrategy = new FloodFillMove();
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
		addInitialRobotToMapExplored();
		Thread thread = new Thread() {
			@Override
			public void run() {
				timer.start();
				int counter = 0;
				MOVE nextMove = MOVE.STOP;
				do {
					sense();
					nextMove = explorationStrategy.nextMove();
					NetworkIOManager.sendMessage("A" + convertMove(nextMove));
					switch (nextMove) {
					case EAST_R:
						moveEastR();
						break;
					case EAST:
						moveEast();
						break;
					case TURN_EAST:
						headEast();
						break;
					case SOUTH_R:
						moveSouthR();
						break;
					case SOUTH:
						moveSouth();
						break;
					case TURN_SOUTH:
						headSouth();
						break;
					case NORTH_R:
						moveNorthR();
						break;
					case NORTH:
						moveNorth();
						break;
					case TURN_NORTH:
						headNorth();
						break;
					case WEST_R:
						moveWestR();
						break;
					case WEST:
						moveWest();
						break;
					case TURN_WEST:
						headWest();
					default:
						break;
					}
					++counter;
					displayExplorationPercentage();
					displayMoves(nextMove, counter);
				} while (nextMove != Movable.MOVE.STOP);
				timer.stop();
				writeMap();
			}
		};
		thread.start();
	}

	public static void startFastestRun() {
		fastestRunStrategy = new ShortestPath(explorationStrategy.getMapExplored());
		Thread thread = new Thread() {
			@Override
			public void run() {
				int counter = 0;
				MOVE nextMove = MOVE.STOP;
				do {
					nextMove = fastestRunStrategy.nextMove();
					NetworkIOManager.sendMessage("A" + convertMove(nextMove));
					if (nextMove == Movable.MOVE.EAST) {
						moveEast();
					} else if (nextMove == Movable.MOVE.WEST) {
						moveWest();
					} else if (nextMove == Movable.MOVE.NORTH) {
						moveNorth();
					} else if (nextMove == Movable.MOVE.SOUTH) {
						moveSouth();
					}
					++counter;
					displayMoves(nextMove, counter);
				} while (nextMove != Movable.MOVE.STOP);
			}
		};
		thread.start();
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

	private static void moveWest() {
		unsetRobot();
		setRobot(positionX - 1, positionY, ORIENTATION.WEST);
	}

	private static void moveEast() {
		unsetRobot();
		setRobot(positionX + 1, positionY, ORIENTATION.EAST);
	}

	private static void moveNorth() {
		unsetRobot();
		setRobot(positionX, positionY - 1, ORIENTATION.NORTH);
	}

	private static void moveSouth() {
		unsetRobot();
		setRobot(positionX, positionY + 1, ORIENTATION.SOUTH);
	}

	private static void moveEastR() {
		unsetRobot();
		setRobot(positionX + 1, positionY, orientation);
	}

	private static void moveWestR() {
		unsetRobot();
		setRobot(positionX - 1, positionY, orientation);
	}

	private static void moveNorthR() {
		unsetRobot();
		setRobot(positionX, positionY - 1, orientation);
	}

	private static void moveSouthR() {
		unsetRobot();
		setRobot(positionX, positionY + 1, orientation);
	}

	private static String convertMove(Enum<Movable.MOVE> move) {
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

	public static void sense() {
		Hashtable<Integer, Movable.GRID_TYPE> updates = new Hashtable<Integer, Movable.GRID_TYPE>();
		switch (orientation) {
		case NORTH:
			updates.putAll(SensorManager.senseNorth());
			updates.putAll(SensorManager.senseEast());
			updates.putAll(SensorManager.senseWest());
			break;
		case SOUTH:
			updates.putAll(SensorManager.senseSouth());
			updates.putAll(SensorManager.senseEast());
			updates.putAll(SensorManager.senseWest());
			break;
		case EAST:
			updates.putAll(SensorManager.senseEast());
			updates.putAll(SensorManager.senseNorth());
			updates.putAll(SensorManager.senseSouth());
			break;
		case WEST:
			updates.putAll(SensorManager.senseWest());
			updates.putAll(SensorManager.senseNorth());
			updates.putAll(SensorManager.senseSouth());
			break;
		}
		Enumeration<Integer> keys = updates.keys();
		while (keys.hasMoreElements()) {
			Integer key = keys.nextElement();
			explorationStrategy.getMapUpdate(key, updates.get(key));
		}
	}

	private static void writeMap() {
		Hashtable<Integer, GRID_TYPE> map = explorationStrategy.getMapExplored();
		int index;
		String fullMapToWrite = "11";
		String exploredMapToWrite = "";

		for (index = 0; index < MAP_WIDTH * MAP_HEIGHT; ++index) {
			if (map.containsKey(index)) {
				fullMapToWrite += "1";
				if (map.get(index) == Movable.GRID_TYPE.OBSTACLE) {
					exploredMapToWrite += "1";
				} else {
					exploredMapToWrite += "0";
				}
			} else {
				fullMapToWrite += "0";
			}
		}
		fullMapToWrite += "11";

		while (exploredMapToWrite.length() % 8 != 0) {
			exploredMapToWrite += "0";
		}
		try {
			FileIOManager.writeFile(fullMapToWrite, "fullMap");
			FileIOManager.writeFile(exploredMapToWrite, "exploredMap");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void reset() {
		// setRobot(0, 0, RobotManager.ORIENTATION.EAST);
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
				MapManager.setMapExplored(x, y);
				explorationStrategy.getMapUpdate(XYToId(x, y), GRID_TYPE.OPEN_SPACE);
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
		positionX = idToX(robotIndex) - 1;
		positionY = idToY(robotIndex) - 1;
		MapManager.initialiseRobot(XYToId(positionX, positionY));

		int robotOrientation = Integer.parseInt(content.substring(3, 4));
		switch (robotOrientation) {
		case 0:
			headNorth();
			break;
		case 1:
			headEast();
			break;
		case 2:
			headSouth();
			break;
		case 3:
			headWest();
			break;
		}
	}

	private static boolean isOutBoundary(int x, int y) {
		return (x >= MAP_WIDTH) || (x < 0) || (y >= MAP_HEIGHT) || (y < 0);
	}
}