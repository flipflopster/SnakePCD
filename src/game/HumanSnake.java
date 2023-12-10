package game;

import java.awt.event.KeyEvent;
import java.util.List;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;

 /** Class for a remote snake, controlled by a human 
  * 
  * @author luismota
  *
  */

public class HumanSnake extends Snake {
	
	private int lastKeyCode;
	
	public HumanSnake(int id, Board board) {
		super(id, board, "Client " + id);
		lastKeyCode = KeyEvent.VK_RIGHT;
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
				if(nextMove != null) {
					System.out.println(nextMove);
					move(getBoard().getCell(nextMove));
				}
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
			case(KeyEvent.VK_LEFT): 
				if(pos.x > 0)
					if(!getBoard().getCell(pos.getCellLeft()).isOcupied())
						nextMove = pos.getCellLeft();
				break;
			case(KeyEvent.VK_RIGHT):
				if(pos.x < Board.NUM_ROWS - 1)
					if(!getBoard().getCell(pos.getCellRight()).isOcupied())
						nextMove = pos.getCellRight();
				break;
			case(KeyEvent.VK_UP):
				if(pos.y > 0)
					if(!getBoard().getCell(pos.getCellAbove()).isOcupied())
						nextMove = pos.getCellAbove();
				break;
			case(KeyEvent.VK_DOWN):
				if(pos.y < Board.NUM_COLUMNS - 1)
					if(!getBoard().getCell(pos.getCellBelow()).isOcupied())
						nextMove = pos.getCellBelow();
				break;
			default: { nextMove = null; System.out.println("null");}
		}
		
		return nextMove;
	}
	
	public void changeDirection(int key) {
		if(cells.size() <= 1) { this.lastKeyCode = key; return; }
		
		if(key == KeyEvent.VK_LEFT) 
			if(cells.get(cells.size() - 2).getPosition().equals(cells.getLast().getPosition().getCellLeft()))
				return;
		if(key == KeyEvent.VK_UP)
			if(cells.get(cells.size() - 2).getPosition().equals(cells.getLast().getPosition().getCellAbove()))
				return;
		if(key == KeyEvent.VK_DOWN)
			if(cells.get(cells.size() - 2).getPosition().equals(cells.getLast().getPosition().getCellBelow()))
				return;
		if(key == KeyEvent.VK_RIGHT)
			if(cells.get(cells.size() - 2).getPosition().equals(cells.getLast().getPosition().getCellRight()))
				return;
		this.lastKeyCode = key;
		
//		switch(key) {
//        case(KeyEvent.VK_LEFT): 
//            if(lastKeyCode==KeyEvent.VK_RIGHT) break;
//        case(KeyEvent.VK_RIGHT): 
//            if(lastKeyCode==KeyEvent.VK_LEFT) break;
//        case(KeyEvent.VK_UP): 
//            if(lastKeyCode==KeyEvent.VK_DOWN) break;
//        case(KeyEvent.VK_DOWN): 
//            if(lastKeyCode==KeyEvent.VK_UP) break;
//        default: lastKeyCode = key;
		
	}
	
}
