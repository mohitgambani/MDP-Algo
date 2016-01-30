package ui;

import java.util.ArrayList;

public class MapManager {
	protected static final int MAP_WIDTH = 20;
	protected static final int MAP_HEIGHT = 15;
	
	private static final int START_ZONE_WIDTH = 3;
	private static final int START_ZONE_HEIGHT = 3;
	private static final int GOAL_ZONE_WIDTH = 3;
	private static final int GOAL_ZONE_HEIGHT = 3;
	
	protected static ArrayList<MapComponent> humanMapComponents = new ArrayList<MapComponent>();
	protected static ArrayList<MapComponent> robotMapComponents = new ArrayList<MapComponent>();
	
	protected static void drawStartZone(){
		final int START_X = 0;
		final int START_Y = 0;
		
		int x, y;
		for(x = START_X; x < START_ZONE_WIDTH; ++x){
			for(y = START_Y; y < START_ZONE_HEIGHT; ++y){
				humanMapComponents.get(XYToIndex(x, y)).setStartZone();
			}
		}
	}
	
	protected static void drawGoalZone(){
		final int GOAL_X = MAP_WIDTH - 1;
		final int GOAL_Y = MAP_HEIGHT - 1;
		
		int x, y;
		for(x = GOAL_X; x >= MAP_WIDTH - GOAL_ZONE_WIDTH; --x){
			for(y = GOAL_Y; y >= MAP_HEIGHT - GOAL_ZONE_HEIGHT; --y){
				humanMapComponents.get(XYToIndex(x, y)).setGoalZone();
			}
		}
	}
	
	private static int indexToX(int index){
		return index % MAP_WIDTH;
	}
	
	private static int indexToY(int index){
		return index / MAP_WIDTH;
	}
	
	private static int XYToIndex(int x, int y){
		return y * MAP_WIDTH + x;
	}
}
