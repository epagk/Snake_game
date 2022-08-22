package myPackage;

import java.util.LinkedList;

public class Apple {
	
	private Node currentApple;
	
	private LinkedList<Node> pathToCurrentApple;

	public Node getApple() {
		return currentApple;
	}

	public void setApple(Node currentApple) {
		this.currentApple = currentApple;
	}

	public LinkedList<Node> getPathToApple() {
		return pathToCurrentApple;
	}

	public void setPathToApple(LinkedList<Node> pathToCurrentApple) {
		this.pathToCurrentApple = pathToCurrentApple;
	}

}
