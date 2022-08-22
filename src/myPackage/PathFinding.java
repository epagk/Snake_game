package myPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class PathFinding {
	
	Board board;
	
	int states = (Parameters.TOTAL_HEIGHT / Parameters.NODE_HEIGHT);
	
	private final double alpha = 0.1; // Learning rate
    private final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future
	
    private char[][] maze;  // Maze based on board
	private int[][] R;       // Reward lookup
	private double[][] Q;    // Q learning
	private Node[][] nodeTable;    // Board of the game
	
	private final int reward = 100;
    private final int penalty = -100;
    
    private final int mazeWidth = (Parameters.TOTAL_WIDTH / Parameters.NODE_HEIGHT);
    private final int mazeHeight = (Parameters.TOTAL_HEIGHT / Parameters.NODE_HEIGHT);
    private final int statesCount = mazeHeight * mazeWidth;
	
	public PathFinding(Board board) {
		this.board = board;
	}

	public LinkedList<Node> performSearch(Node startNode, Node destinationNode){
		if(Parameters.ALGO_CHOICE == false)
		 return AstarSearch(startNode, destinationNode);
		return QLearning(startNode, destinationNode);
	}
	
	/**
	 * Calculate Manhattan distance
	 */
	public int manhattanDis(Node source,Node destination){
		int xAbs=Math.abs(source.getX()-destination.getX());
		int yAbs=Math.abs(source.getY()-destination.getY());
		return xAbs+yAbs;
	}
	
	
	public LinkedList<Node> AstarSearch(Node source, Node goal){

            LinkedList<Node> explored = new LinkedList<Node>();
            PriorityQueue<Node> queue = new PriorityQueue<Node>();

            //cost from start
            source.setG(0);
            source.setH(manhattanDis(source, goal));
            source.setParent(null);

            queue.add(source);

            while(!queue.isEmpty()){

                    //the SnakeNode in having the lowest f_score value
                    Node current = queue.poll();

                    explored.add(current);

                    //goal found
                    if(current.equals(goal)){
                           return makePath(current);
                    }
                    
                    List<Node> neighbourNodes = current.getNeighbors();

                    //check every child of current SnakeNode
                    for(int i = 0; i < neighbourNodes.size(); i++){
                            Node child = neighbourNodes.get(i);
                            int temp_g_scores = current.getG() + 10;		// 10 is the distance of a cell to another
                            int temp_getF = temp_g_scores + manhattanDis(child, goal);


                            /*if child SnakeNode has been evaluated and 
                            the newer f_score is higher, skip*/
                            
                            if((explored.contains(child)) && 
                                    (temp_getF >= child.getF())){
                                    continue;
                            }

                            /*else if child SnakeNode is not in queue or 
                            newer f_score is lower*/
                            
                            else if((!queue.contains(child)) || 
                                    (temp_getF < child.getF())){

                                    child.setParent(current);
                                    child.setG(temp_g_scores);

                                    if(queue.contains(child)){
                                            queue.remove(child);
                                    }

                                    if(shouldProcess(child)){
                						queue.add(child);
                					}
                                    //queue.add(child);

                            }

                    }

            }
            return null;
    }
		
		
	/**
	 * Find a appropriate random Neighbor
	 * @return MySnakeNode
	 */
	public Node getRandomMove() {

		List<Node> list = board.snake.getHead().getNeighbors();

		for(int a=0; a<list.size();a++) {
			Node node=list.get(a);
			
			if(shouldProcess(node)) {
				return node;
			}
			
		}
		//System.out.println("moveRandom failed");
		return null;
	}
	
	
	public LinkedList<Node> makePath(Node node){

		LinkedList<Node> path = new LinkedList<Node>();
		
		while(node.getParent() !=null){
			path.addFirst(node);
			node = node.getParent();
		}
					
		return path;
	}
	
	
	/**
	 * @return should we process this node
	 */
	public boolean shouldProcess(Node n){
		//if node is out of screen MAX
		if(n.getX()>(Parameters.TOTAL_WIDTH-Parameters.NODE_HEIGHT) ||
				n.getY()>(Parameters.TOTAL_HEIGHT-Parameters.NODE_HEIGHT)) {
			return false;
		}
		//if node is out of screen MIN
		if(n.getX()<0 ||
				n.getY()<0) {
			return false;
		}

		boolean  shouldProceed=!board.snake.getSnakeBody().contains(n);
//		System.out.println("shouldProcess: "+shouldProceed);
		return shouldProceed;
		
	}

	public LinkedList<Node> QLearning(Node source, Node goal){
		
		init(source, goal);
		calculateQ();
		
		return findPath(source);
	}
	
	public void init(Node head, Node apple) {

        R = new int[statesCount][statesCount];
        Q = new double[statesCount][statesCount];
        maze = new char[mazeHeight][mazeWidth];

        List<Node> snakeBody = board.snake.getSnakeBody();
        
        for(int i=0; i<snakeBody.size(); i++){
        	Node n = snakeBody.get(i);
        	maze[n.getX()/10][n.getY()/10] = 'X';
        }
        
        maze[head.getX()/10][head.getY()/10] = 'S';
        maze[apple.getX()/10][apple.getY()/10] = 'F';
        
        for(int i=0; i<mazeWidth; i++){
        	for(int j=0; j<mazeHeight; j++){
        		if(maze[i][j]!='X' && maze[i][j]!='F' && maze[i][j]!='S'){
        			maze[i][j] = '0';
        		}
        	}
        }

        // We will navigate through the reward matrix R using k index
        for (int k = 0; k < statesCount; k++) {

        	// We will navigate with i and j through the maze, so we need
        	// to translate k into i and j
        	int i = k / mazeWidth;
        	int j = k - i * mazeWidth;

        	// Fill in the reward matrix with -1
        	for (int s = 0; s < statesCount; s++) {
        		R[k][s] = -1;
        	}

        	// If not in final state or a wall try moving in all directions in the maze
        	if (maze[i][j] != 'F') {

        		// Try to move left in the maze
        		int goLeft = j - 1;
        		if (goLeft >= 0) {
        			int target = i * mazeWidth + goLeft;
        			if (maze[i][goLeft] == '0') {
        				R[k][target] = 0;
        			} else if (maze[i][goLeft] == 'F') {
        				R[k][target] = reward;
        			} else {
        				R[k][target] = penalty;
        			}
        		}

        		// Try to move right in the maze
        		int goRight = j + 1;
        		if (goRight < mazeWidth) {
        			int target = i * mazeWidth + goRight;
        			if (maze[i][goRight] == '0') {
        				R[k][target] = 0;
        			} else if (maze[i][goRight] == 'F') {
        				R[k][target] = reward;
        			} else {
        				R[k][target] = penalty;
        			}
        		}

        		// Try to move up in the maze
        		int goUp = i - 1;
        		if (goUp >= 0) {
        			int target = goUp * mazeWidth + j;
        			if (maze[goUp][j] == '0') {
        				R[k][target] = 0;
        			} else if (maze[goUp][j] == 'F') {
        				R[k][target] = reward;
        			} else {
        				R[k][target] = penalty;
        			}
        		}

        		// Try to move down in the maze
        		int goDown = i + 1;
        		if (goDown < mazeHeight) {
        			int target = goDown * mazeWidth + j;
        			if (maze[goDown][j] == '0') {
        				R[k][target] = 0;
        			} else if (maze[goDown][j] == 'F') {
        				R[k][target] = reward;
        			} else {
        				R[k][target] = penalty;
        			}
        		}
        	}
        }
        initializeQ();
        DFS(board.snake.getHead());
    }

	//Set Q values to R values
    public void initializeQ()
    {
        for (int i = 0; i < statesCount; i++){
            for(int j = 0; j < statesCount; j++){
                Q[i][j] = (double)R[i][j];
            }
        }
    }
	
    public void calculateQ() {
        Random rand = new Random();

        for (int i = 0; i < 100; i++) { // Train cycles
            // Select random initial state
            int crtState = rand.nextInt(statesCount);

            while (!isFinalState(crtState)) {
                int[] actionsFromCurrentState = possibleActionsFromState(crtState);

                // Pick a random action from the ones possible
                int index = rand.nextInt(actionsFromCurrentState.length);
                int nextState = actionsFromCurrentState[index];

                // Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))
                double q = Q[crtState][nextState];
                double maxQ = maxQ(nextState);
                int r = R[crtState][nextState];

                double value = q + alpha * (r + gamma * maxQ - q);
                Q[crtState][nextState] = value;

                crtState = nextState;
            }
        }
    }
    
    
    public boolean isFinalState(int state) {
        int i = state / mazeWidth;
        int j = state - i * mazeWidth;

        return maze[i][j] == 'F';
    }
    
    
    public int[] possibleActionsFromState(int state) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < statesCount; i++) {
            if (R[state][i] != -1) {
                result.add(i);
            }
        }

        return result.stream().mapToInt(i -> i).toArray();
    }

    public double maxQ(int nextState) {
        int[] actionsFromState = possibleActionsFromState(nextState);
        //the learning rate and eagerness will keep the W value above the lowest reward
        double maxValue = -10;
        for (int nextAction : actionsFromState) {
            double value = Q[nextState][nextAction];

            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }


    public int getPolicyFromState(int state) {
        int[] actionsFromState = possibleActionsFromState(state);

        double maxValue = Double.MIN_VALUE;
        int policyGotoState = state;

        // Pick to move to the state that has the maximum Q value
        for (int nextState : actionsFromState) {
            double value = Q[state][nextState];

            if (value > maxValue) {
                maxValue = value;
                policyGotoState = nextState;
            }
        }
        return policyGotoState;
    }
    
    public LinkedList<Node> findPath(Node head){
    	
    	List<Node> snakeBody = board.snake.getSnakeBody();
    	
    	LinkedList<Node> path = new LinkedList<Node>();
    	
    	int curState = (head.getY()/10) + (head.getX()*3);
    	
    	boolean appleFound = false;
    	
    	while(!appleFound){
    		int nextState = getPolicyFromState(curState);

    		int stateX = nextState/mazeWidth;
    		int stateY = nextState - (stateX*mazeWidth);
    		
    		Node n = nodeTable[stateX][stateY];
    		
    		if(snakeBody.contains(n))
    			return null;
		
    		path.add(n);
    		
    		if(isFinalState(nextState)) { appleFound = true; }
    		
    		curState = nextState;
    	}
    	
    	return path;
    	
    }

    public void DFS(Node start){
    	
    	nodeTable = new Node[mazeHeight][mazeWidth];
    	List<Node> visited = new LinkedList<Node>(); 
    	
    	DFSUtil(start, visited);
    }
    
    public void DFSUtil(Node curNode, List<Node> visited){
    	
    	visited.add(curNode);
    	nodeTable[curNode.getX()/10][curNode.getY()/10] = curNode;
    	
    	List<Node> neighbors = curNode.getNeighbors();
    	
    	for(int i=0; i<neighbors.size(); i++){
    		Node n = neighbors.get(i);
    		if(!visited.contains(n) && inBound(n)){
    			DFSUtil(n,visited);
    		}
    	}
    	
    }
    
    public boolean inBound(Node n){
		//if node is out of screen MAX
		if(n.getX()>(Parameters.TOTAL_WIDTH-Parameters.NODE_HEIGHT) ||
				n.getY()>(Parameters.TOTAL_HEIGHT-Parameters.NODE_HEIGHT)) {
			return false;
		}
		//if node is out of screen MIN
		if(n.getX()<0 ||
				n.getY()<0) {
			return false;
		}

		return true;
		
	}

    
}
