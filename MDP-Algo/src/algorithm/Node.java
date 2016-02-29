package algorithm;

import algorithm.Movable.MOVE;

public class Node {
	
	private int id;
	private int g_cost;
	private int h_cost;
	private int f_cost;
	Node parent;
	MOVE move;

	public Node(int id){
		this.id = id;
		g_cost = h_cost = f_cost = -1;
		move = MOVE.NO_MOVE;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getGCost() {
		return g_cost;
	}
	
	public void setGCost(int g_cost) {
		this.g_cost = g_cost;
		this.f_cost = this.g_cost + this.h_cost;
	}
	
	public int getHCost() {
		return h_cost;
	}
	
	public void setHCost(int h_cost) {
		this.h_cost = h_cost;
		this.f_cost = this.g_cost + this.h_cost;
	}
	
	public int getFCost() {
		return f_cost;
	}
	
	public Node getParent() {
		return this.parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public MOVE getMove() {
		return move;
	}

	public void setMove(MOVE move) {
		this.move = move;
	}
}
