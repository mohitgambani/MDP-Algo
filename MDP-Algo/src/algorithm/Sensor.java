package algorithm;

import java.util.Hashtable;

public abstract class Sensor {
	
	private static final int FORNT_RANGE = 3;
	private static final int SIDE_RANGE = 3;
	
	private int frontRange = FORNT_RANGE;
	private int sideRange = SIDE_RANGE;
	
	public abstract Hashtable<Integer, Movable.GRID_TYPE> getSensoryInfo() ;

	public int getFrontRange() {
		return frontRange;
	}

	public void setFrontRange(int frontRange) {
		this.frontRange = frontRange;
	}
	
	public void setFrontRange() {
		setFrontRange(FORNT_RANGE);
	}

	public int getSideRange() {
		return sideRange;
	}

	public void setSideRange(int sideRange) {
		this.sideRange = sideRange;
	}
	public void setSideRange() {
		setSideRange(SIDE_RANGE);
	}
	
	public abstract void getReadingsFromExt(String readingStr);
	
}
