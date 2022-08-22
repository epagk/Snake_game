package myPackage;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>{

	int x;
	int y;
	int g,h=0;
	private Node parent=null;
	
	
	public Node(int x, int y) {
		this.x=x;
		this.y=y;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}
	
	
	
	public int getG() {
		return g;
	}


	public void setG(int g) {
		this.g = g;
	}


	public int getH() {
		return h;
	}


	public void setH(int h) {
		this.h = h;
	}

	public int getF() {
		return g+h;
	}
	

	public Node getParent() {
		return parent;
	}


	public void setParent(Node parent) {
		this.parent = parent;
	}

	public List<Node> getNeighbors(){
		
		List<Node> neighbors = new ArrayList<>();

		Node up = new Node(getX()-Parameters.NODE_HEIGHT,getY());
		Node down = new Node(getX()+Parameters.NODE_HEIGHT, getY());
		Node left = new Node(getX(), getY()-Parameters.NODE_HEIGHT);
		Node right = new Node(getX(), getY()+Parameters.NODE_HEIGHT);

		neighbors.add(up);
		neighbors.add(down);
		neighbors.add(left);
		neighbors.add(right);

		return neighbors;

	}

	public Node locateApple(Snake snake){
		
		while(true){
			int x = 0,y;
			 x = Parameters.NODE_HEIGHT*(int) (Math.random() * Parameters.TOTAL_WIDTH/Parameters.NODE_HEIGHT);
			 y = Parameters.NODE_HEIGHT*(int) (Math.random() * Parameters.TOTAL_HEIGHT/Parameters.NODE_HEIGHT);
			 Node n=new Node(x,y);
			
			if(!snake.getSnakeBody().contains(n)){
				System.out.println("x="+x);
				System.out.println("y="+y);

				return n;
			}
		}
	}

	
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj instanceof Node){
			Node secondNode = (Node) obj;
			if(this.x == secondNode.x && this.y == secondNode.y){
				return true;
			}
		}
		return false;
	}


	/**
	 * Used for sorting the queue
	 * We add the node at start of queue which has lowest F
	 * Because we will poll the node which has lowest F first
	 */
	@Override
	public int compareTo(Node o) {
		int thisVal = this.getF();
		int otherVal = o.getF();
		
		int value = thisVal - otherVal;
		
		return (value>0)?1:(value<0)? -1: 0;
	}
	
	
}
