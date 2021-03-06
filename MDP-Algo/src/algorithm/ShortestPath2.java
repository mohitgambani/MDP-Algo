package algorithm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;


public class ShortestPath2 extends Movable {
	
	private Node start;
	private Node goal;
	
	private ArrayList<Node> open = new ArrayList<Node>();
	private ArrayList<Node> closed = new ArrayList<Node>();
	private Hashtable<Integer, GRID_TYPE> mapExplored;
	private Stack<MOVE> listOfMoves = new Stack<MOVE>();
	
	public ShortestPath2(Hashtable<Integer, GRID_TYPE> mapExplored){
		super();
		this.mapExplored = mapExplored;
		start = new Node(XYToId(RobotManager.getRobotPositionX(), 
				RobotManager.getRobotPositionY()));
		goal = new Node(XYToId(MAP_WIDTH - ROBOT_WIDTH, MAP_HEIGHT - ROBOT_HEIGHT));
		start.setGCost(0);
		start.setHCost(distanceToGoal(start.getId()));
		start.setParent(null);
		
		coreAlgorithm();
	}
	
	public ShortestPath2(int startPos, int goalPos,
			Hashtable<Integer, GRID_TYPE> mapExplored){
		super();
		this.mapExplored = mapExplored;
		start = new Node(startPos);
		goal = new Node(goalPos);
		start.setGCost(0);
		start.setHCost(distanceToGoal(start.getId()));
		start.setParent(null);
		
		coreAlgorithm();
	}
	
	public void coreAlgorithm(){
		
		if(start.getId() == goal.getId()){
			listOfMoves.push(MOVE.STOP);
			listOfMoves.push(MOVE.NO_MOVE);
			return;
		}
		
		MOVE old_move = Movable.MOVE.NO_MOVE;
		open.add(start);
		ArrayList<Node> neighbours;
		while(!open.isEmpty()){
			ArrayList<Node> required = getNodesWithLowestFCost(open);
			Node current = getNodeWithLowestFCost(required, old_move);
			
			if(current.getMove() == MOVE.EAST || current.getMove() == MOVE.WEST ||
					current.getMove() == MOVE.NORTH || current.getMove() == MOVE.SOUTH)
				old_move = current.getMove();
			
			if(current.getId() == goal.getId()){
				getAllMovesFromLastNode(current);
				return;
			}
			
			
			
			neighbours = getNeighbouringNodes(current);
			
			for(Node neighbour: neighbours){
				Node openNode = findNodeWithSameIdInOpen(neighbour);
				
				if(openNode != null && openNode.getFCost() <= neighbour.getFCost())
					continue;
				else if(openNode != null)
					open.remove(openNode);
				
				Node closedNode = findNodeWithSameIdInClosed(neighbour);
				
				if(closedNode != null && closedNode.getFCost() <= neighbour.getFCost())
					continue;
				else if(closedNode != null)
					closed.remove(closedNode);
				
				open.add(neighbour);
				
			}
			
			open.remove(current);
			closed.add(current);
			
		}
	}
	
	private ArrayList<Node> getNodesWithLowestFCost(ArrayList<Node> listOfNodes){
		
		Node requiredNode = listOfNodes.get(0);
		ArrayList<Node> requiredNodes = new ArrayList<Node>();
		
		for(Node node: listOfNodes){
			if(node.getFCost() < requiredNode.getFCost())
				requiredNode = node;
		}
		
		for(Node node: listOfNodes){
			if(node.getFCost() == requiredNode.getFCost())
				requiredNodes.add(node);
		}
		
		return requiredNodes;
		
	}
	
	private Node getNodeWithLowestFCost(ArrayList<Node> listOfNodes, MOVE oldMove){
		Node requiredNode = listOfNodes.get(0);
		
		for(Node node:listOfNodes){
			if(node.getMove() == oldMove){
				requiredNode = node;
				break;
			}
		}
		
		return requiredNode;
	}
	
	private ArrayList<Node> getNeighbouringNodes(Node currentNode){
		ArrayList<Node> neighbours = new ArrayList<Node>();
		boolean traversable = true;
		
		int topId = XYToId( idToX(currentNode.getId()),
				idToY(currentNode.getId()) - 1 );
		for(int x = idToX(currentNode.getId()); 
				x < idToX(currentNode.getId()) + ROBOT_WIDTH; x++){
			for(int y = idToY(currentNode.getId()) - 1; 
					y < idToY(currentNode.getId()) - 1 + ROBOT_HEIGHT; y++){
				if(isOutBoundary(x,y) || 
						mapExplored.get(XYToId(x,y)) != Movable.GRID_TYPE.OPEN_SPACE)
					traversable = false;
			}
		}
		if(traversable){
			Node top = new Node(topId);
			top.setGCost(currentNode.getGCost() + 1);
			top.setHCost(distanceToGoal(top.getId()));
			top.setParent(currentNode);
			top.setMove(MOVE.NORTH);
			neighbours.add(top);
		}
		
		traversable = true;
		int bottomId = XYToId( idToX(currentNode.getId()),
				idToY(currentNode.getId()) + 1 );
		for(int x = idToX(currentNode.getId()); 
				x < idToX(currentNode.getId()) + ROBOT_WIDTH; x++){
			for(int y = idToY(currentNode.getId()) + 1; 
					y < idToY(currentNode.getId()) + 1 + ROBOT_HEIGHT; y++){
				if(isOutBoundary(x,y) || 
						mapExplored.get(XYToId(x,y)) != Movable.GRID_TYPE.OPEN_SPACE)
					traversable = false;
			}
		}
		if(traversable){
			Node bottom = new Node(bottomId);
			bottom.setGCost(currentNode.getGCost() + 1);
			bottom.setHCost(distanceToGoal(bottom.getId()));
			bottom.setParent(currentNode);
			bottom.setMove(MOVE.SOUTH);
			neighbours.add(bottom);
		}
		
		traversable = true;
		int leftId = XYToId( idToX(currentNode.getId()) - 1,
				idToY(currentNode.getId()) );
		for(int x = idToX(currentNode.getId()) - 1;
				x < idToX(currentNode.getId()) - 1 + ROBOT_WIDTH; x++){
			for(int y = idToY(currentNode.getId()); 
					y < idToY(currentNode.getId()) + ROBOT_HEIGHT; y++){
				if(isOutBoundary(x,y) || 
						mapExplored.get(XYToId(x,y)) != Movable.GRID_TYPE.OPEN_SPACE)
					traversable = false;
			}
		}
		if(traversable){
			Node left = new Node(leftId);
			left.setGCost(currentNode.getGCost() + 1);
			left.setHCost(distanceToGoal(left.getId()));
			left.setParent(currentNode);
			left.setMove(MOVE.WEST);
			neighbours.add(left);
		}
		
		traversable = true;
		int rightId = XYToId( idToX(currentNode.getId()) + 1,
				idToY(currentNode.getId()) );
		for(int x = idToX(currentNode.getId()) + 1;
				x < idToX(currentNode.getId()) + 1 + ROBOT_WIDTH; x++){
			for(int y = idToY(currentNode.getId());	
					y < idToY(currentNode.getId()) + ROBOT_HEIGHT; y++){
				if(isOutBoundary(x,y) || 
						mapExplored.get(XYToId(x,y)) != Movable.GRID_TYPE.OPEN_SPACE)
					traversable = false;
			}
		}
		if(traversable){
			Node right = new Node(rightId);
			right.setGCost(currentNode.getGCost() + 1);
			right.setHCost(distanceToGoal(right.getId()));
			right.setParent(currentNode);
			right.setMove(MOVE.EAST);
			neighbours.add(right);
		}
		
		return neighbours;
	}
	
	public Node findNodeWithSameIdInOpen(Node node){
		for(Node eachNode: open){
			if(eachNode.getId() == node.getId())
				return eachNode;
		}
		return null;
	}
	
	public Node findNodeWithSameIdInClosed(Node node){
		for(Node eachNode: closed){
			if(eachNode.getId() == node.getId())
				return eachNode;
		}
		return null;
	}
	
	public int distanceToGoal(int id){
		int x = idToX(id);
		int y = idToY(id);
		
		int goalX = idToX(goal.getId());
		int goalY = idToY(goal.getId());
		return Math.abs(x - goalX) + Math.abs(y - goalY);
	}
	
	public void getAllMovesFromLastNode(Node node){
		listOfMoves.push(MOVE.STOP);
		while(node.getMove() != MOVE.NO_MOVE){
			System.out.println(node.getMove());
			listOfMoves.push(node.getMove());
			node = node.getParent();
		}
	}
	
	public MOVE nextMove(){
		return listOfMoves.pop();
	}
	
	@Override
	public int movesToStartZone() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Node getStart() {
		return start;
	}

	public void setStart(int id) {
		this.start.setId(id);
	}

	public Node getGoal() {
		return goal;
	}

	public void setGoal(int id) {
		this.goal.setId(id);
	}

	public Stack<MOVE> getListOfMoves() {
		return listOfMoves;
	}

}