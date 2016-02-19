package algorithm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class ShortestPath extends Movable {
	
	private Node start;
	private Node goal;
	
	private ArrayList<Node> open = new ArrayList<Node>();
	private ArrayList<Node> closed = new ArrayList<Node>();
	private Hashtable<Integer, Enum<Movable.GRID_TYPE>> mapExplored;
	private Stack<Enum<MOVE>> listOfMoves = new Stack<Enum<MOVE>>();
	
	public ShortestPath(){
		super();
		mapExplored = getMapExplored();
		for(int i = 0; i <= 299; i++){
			if(i >= 40 && i <= 50)
				mapExplored.put(i, GRID_TYPE.OBSTACLE);
			else if(i >= 150 && i <= 160)
				mapExplored.put(i, GRID_TYPE.OBSTACLE);
			else
				mapExplored.put(i, GRID_TYPE.OPEN_SPACE);
		}
		start = new Node(0);
		goal = new Node(278);
		start.setGCost(0);
		start.setHCost(distanceToGoal(start.getId()));
		start.setParent(null);
		
		coreAlgorithm();
	}
	
	public void coreAlgorithm(){
		open.add(start);
		ArrayList<Node> neighbours;
		while(!open.isEmpty()){
			Node current = getNodeWithLowestFCost(open);
			
			if(current.getId() == goal.getId()){
				current.setMove(MOVE.STOP);
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
	
	private Node getNodeWithLowestFCost(ArrayList<Node> listOfNodes){
		
		Node requiredNode = listOfNodes.get(0);
		
		for(Node node: listOfNodes){
			if(node.getFCost() < requiredNode.getFCost())
				requiredNode = node;
		}
		
		return requiredNode;
		
	}
	
	private ArrayList<Node> getNeighbouringNodes(Node currentNode){
		ArrayList<Node> neighbours = new ArrayList<Node>();
		boolean traversable = true;
		
		int topId = XYToId( idToX(currentNode.getId()),
				idToY(currentNode.getId()) - 1 );
		for(int x = idToX(currentNode.getId()); x < idToX(currentNode.getId()) + 2; x++){
			for(int y = idToY(currentNode.getId()) - 1; 
					y < idToY(currentNode.getId()) - 1 + 2; y++){
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
		for(int x = idToX(currentNode.getId()); x < idToX(currentNode.getId()) + 2; x++){
			for(int y = idToY(currentNode.getId()) + 1; 
					y < idToY(currentNode.getId()) + 1 + 2; y++){
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
				x < idToX(currentNode.getId()) - 1 + 2; x++){
			for(int y = idToY(currentNode.getId()); y < idToY(currentNode.getId()) + 2; y++){
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
				x < idToX(currentNode.getId()) + 1 + 2; x++){
			for(int y = idToY(currentNode.getId());	y < idToY(currentNode.getId()) + 2; y++){
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
		while(node != null){
			listOfMoves.push(node.getMove());
			node = node.getParent();
		}
	}
	
	public Enum<MOVE> nextMove(){
		return listOfMoves.pop();
	}

	@Override
	public int movesToStartZone() {
		// TODO Auto-generated method stub
		return 0;
	}

}
