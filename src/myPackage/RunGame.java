package myPackage;

public class RunGame implements Runnable{

	private Board board;
	
	public RunGame(Board board) {
		this.board=board;
	}
	
	@Override
	public void run() {
		while(!Parameters.GAME_OVER) {
			try {
				Thread.sleep(Parameters.GAME_DELAY);
				board.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("GAME OVER");
		System.exit(0);		// Game Over
	}

	public Board getBoard() {
		return board;
	}

	public void setPanel(Board board) {
		this.board = board;
	}
	
}
