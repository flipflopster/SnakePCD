package game;

import environment.Board;
import environment.BoardPosition;

 /** Class for a remote snake, controlled by a human 
  * 
  * @author luismota
  *
  */

public abstract class HumanSnake extends Snake {
	
	public HumanSnake(int id, Board board, String name) {
		super(id, board, name);
	}

	@Override
	public void run() {
		try { doInitialPositioning(); } catch (Exception e1) { e1.printStackTrace(); }
		
		System.err.println("initial size:" + cells.size());
		
//		try {
//			cells.getLast().request(this);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//TODO: automatic movement
		
		while(size <= DELTA_SIZE && !getBoard().isFinished()) {
			BoardPosition nextMove = getNextMove();
			try {
				if(nextMove != null)
					move(getBoard().getCell(nextMove));
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
			} catch (InterruptedException e) {
				System.out.println("Snake " + getIdentification() + " interrupted");
			}
		}
		System.out.println(getName() + " is joever");
	}

	private BoardPosition getNextMove() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
