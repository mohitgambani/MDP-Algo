package algorithm;

import java.util.Hashtable;

import algorithm.Movable.GRID_TYPE;

public class RealSensor extends Sensor{
	
	private static final int numOfSensors = 5;
	private int[] readings;
	
	private boolean readingsReady;

	public RealSensor() {
		super();
		readings = new int[numOfSensors];
		initialiseReadings();
	}


	@Override
	public Hashtable<Integer, GRID_TYPE> getSensoryInfo() {
		while(!readingsReady){}
		Hashtable<Integer, GRID_TYPE> results = new Hashtable<Integer, GRID_TYPE>();
		initialiseReadings();
		return results;
	}
	
	private void initialiseReadings(){
		for(int reading : readings){
			reading = -1;
		}
		readingsReady = false;
	}

	@Override
	public void getReadingsFromExt(String readingStr) {
		readingStr = readingStr.substring(0, readingStr.length() - 1);
		int index = 0;
		
		for(String str : readingStr.split(",")){
			readings[index] = Integer.parseInt(str);
			++index;
		}
		readingsReady = true;
	}


}
