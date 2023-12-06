package game;

import java.awt.event.KeyEvent;
import java.util.List;

import environment.Board;
import environment.BoardPosition;

 /** Class for a remote snake, controlled by a human 
  * 
  * @author luismota
  *
  */

public abstract class HumanSnake extends Snake {
	
	private int lastKeyCode;
	
	public HumanSnake(int id, Board board, String name) {
		super(id, board, name);
		lastKeyCode = KeyEvent.KEY_LOCATION_RIGHT;
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
		BoardPosition nextMove = null;
		BoardPosition pos = cells.getLast().getPosition();
		
		switch(lastKeyCode) {
			case(KeyEvent.KEY_LOCATION_LEFT): 
				if(pos.x > 0) nextMove = pos.getCellLeft();
				break;
			case(KeyEvent.VK_KP_UP):
				if(pos.y > 0) nextMove = pos.getCellAbove();
				break;
			case(KeyEvent.VK_DOWN):
				if(pos.y < Board.NUM_COLUMNS - 1) nextMove = pos.getCellBelow();
				break;
			default: if(pos.x < Board.NUM_COLUMNS - 1) nextMove = pos.getCellRight();
		}
		
		return nextMove;
	}
	
	public void changeDirection(int key) { this.lastKeyCode = key; }
	
}
