package myPackage;

import java.util.ArrayList;
import java.util.List;

public class Snake {
	
	private Node head;
	private Node tail;
	private Node apple;

	
	private List<Node> snakeBody = new ArrayList<>();
	
	public Snake(Node head, Node tail) {
		this.head=head;
		this.tail=tail;
	}

	public List<Node> getSnakeBody() {
		return snakeBody;
	}
	

	public void setSnakeBody(List<Node> snakeBody) {
		this.snakeBody = snakeBody;
	}

	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public Node getTail() {
		return tail;
	}

	public void setTail(Node tail) {
		this.tail = tail;
	}

	public Node getApple() {
		return apple;
	}

	public void setApple(Node apple) {
		this.apple = apple;
	}

	
	public void moveSnake(Node moveTo) {
		snakeBody.add(0,moveTo);

		setHead(snakeBody.get(0));
		
		//System.out.println("moveTo: "+moveTo.getX()+", "+moveTo.getY());
		//System.out.println("apple : "+apple.getX()+", "+apple.getY());
		if(moveTo == null){ System.out.println("moveTo is null"); }
		if(moveTo.equals(apple)) {
		}else {
			snakeBody.remove(getTail());
			setTail(snakeBody.get(snakeBody.size()-1));
		}
	}
		
	public boolean belongInBody(int x, int y){
		
		for(int i=0; i<snakeBody.size(); i++){
			Node n = snakeBody.get(i);
			if(n.getX()==x && n.getY()==y)
				return true;
		}
			
		return false;
	}
}