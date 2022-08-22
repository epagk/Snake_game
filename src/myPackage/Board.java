package myPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JPanel;

public class Board extends JPanel{
	
	public Snake snake;
	private final RunGame run=new RunGame(this);
	public final Apple apple = new Apple();
	
	private final PathFinding path = new PathFinding(this);
	
	public Board(){
        setPreferredSize(new Dimension(Parameters.TOTAL_WIDTH, Parameters.TOTAL_HEIGHT));
        setBackground(Color.white);
        setFocusable(true);

        Node head=new Node(Parameters.TOTAL_WIDTH/2, Parameters.TOTAL_HEIGHT/2);
        Node tail=new Node(Parameters.TOTAL_WIDTH/2,( Parameters.TOTAL_HEIGHT/2)+Parameters.NODE_HEIGHT);

        snake=new Snake(head,tail);
        snake.getSnakeBody().add(head);
        snake.getSnakeBody().add(tail);

        setFood();
        new Thread(run).start();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		drawSnake(g);
		drawFood(g);
		
		if(apple.getPathToApple()==null) {
			LinkedList<Node> list = path.performSearch(snake.getHead(), snake.getApple());
			apple.setPathToApple(list);
			
			if(list==null) {
				//no path found by search Algo take a random move
				moveRandom();
			}
		}
		if(apple.getPathToApple()!=null) {
			//printPath();
			Node node=apple.getPathToApple().remove();
			snake.moveSnake(node);
		}
	}

	private void moveRandom() {
		//here we should make a random move so that snake tail move and maybe we find a 
		//path in next call
		
		Node neighbor = path.getRandomMove();
		if(neighbor!=null) {
			snake.moveSnake(neighbor);
		}
		else{
			System.out.println("Score: "+snake.getSnakeBody().size());
			Parameters.GAME_OVER = true;
		}
	}

	private void setFood() {
		//SnakeNode food=foodProvider.getFood(mySnake);
		Node food = locateApple(snake);
		snake.setApple(food);
		apple.setApple(food);
		apple.setPathToApple(null);
	}
	
	public Node locateApple(Snake snake){
		while(true){
			int x = 0,y;
			 x = Parameters.NODE_HEIGHT*(int) (Math.random() * Parameters.TOTAL_WIDTH/Parameters.NODE_HEIGHT);
			 y = Parameters.NODE_HEIGHT*(int) (Math.random() * Parameters.TOTAL_HEIGHT/Parameters.NODE_HEIGHT);
			 Node n=new Node(x,y);
			
			if(!snake.getSnakeBody().contains(n)){
				return n;
			}
		}
	}
	
	private void drawFood(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(snake.getApple().getX(), snake.getApple().getY(), Parameters.NODE_HEIGHT, Parameters.NODE_HEIGHT);

	}
	
	private void drawSnake(Graphics g) {

		//getting complete snake to draw
		for(int a=0;a<snake.getSnakeBody().size(); a++) {
			Node node=snake.getSnakeBody().get(a);
			
			if(node.equals(snake.getHead())) {
				g.setColor(Color.GREEN); 
			}else {
				g.setColor(Color.BLACK);  
			}
			
			if(node.equals(snake.getApple())) {
				setFood();
			}
			g.fillRect(node.getX(), node.getY(), Parameters.NODE_HEIGHT, Parameters.NODE_HEIGHT);
			
		}
	}


}
