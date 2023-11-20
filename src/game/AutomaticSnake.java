package game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.Position;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id, board);

	}

	@Override
	public void run() {
		
		try { doInitialPositioning(); } catch (Exception e1) { e1.printStackTrace(); }
		
		System.err.println("initial size:" + cells.size());
		
		while(size <= DELTA_SIZE && !getBoard().isFinished()) {
			try {
				
				move(this.getBoard().getCell(getNextMoveDumb()));
				Thread.sleep(getBoard().PLAYER_PLAY_INTERVAL);
			} catch (InterruptedException e1) {
				if(!getBoard().isFinished()) {
					try {
						BoardPosition move = getNextPossibleMove();
						if( move != null)
							move(this.getBoard().getCell(move));
						Thread.sleep(getBoard().PLAYER_PLAY_INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					e1.printStackTrace();
					System.out.println("joever");
				}
			}
		}
//		try {
//			cells.getLast().request(this);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//TODO: automatic movement
	}
	
	private BoardPosition getNextPossibleMove() {
		List<BoardPosition> adjacentMoves = getBoard().getNeighboringPositions(cells.getLast());

		BoardPosition move = adjacentMoves.get(0);
		
		
		for (BoardPosition bp : new HashSet<BoardPosition>(adjacentMoves))
			if(!getBoard().getCell(bp).isOcupied())
					move = bp;
		
		return move;
	}

	public BoardPosition getNextMoveDumb(){
		List<BoardPosition> adjacentMoves = getBoard().getNeighboringPositions(cells.getLast());
		// List<BoardPosition> possibleMoves = new ArrayList<BoardPosition>();
		List<Cell> snakeBody = getCells();
		
		List<BoardPosition> noGood = new LinkedList<BoardPosition>();
		
		for(BoardPosition bp : adjacentMoves)
			for(Cell c : snakeBody)
				if(bp.equals(c.getPosition())) {
					noGood.add(bp);
					break;
				}
		
		for(BoardPosition bp : noGood)
			adjacentMoves.remove(bp);
		
//		for(int i=0; i<adjacentMoves.size();i++) {
//			
//			if(!this.getBoard().getCell(adjacentMoves.get(i)).isOcupied() );
//				possibleMoves.add(adjacentMoves.get(i));
//		}
		
		BoardPosition goal = this.getBoard().getGoalPosition();
		BoardPosition move = adjacentMoves.get(0);
//		for(int i=0; i<possibleMoves.size();i++) {
//			if(possibleMoves.get(i).distanceTo(goal)<move.distanceTo(goal))
//				move=possibleMoves.get(i);
//		}
		
		for (BoardPosition bp : adjacentMoves)
			move = getBoard().getCell(move).getBest(getBoard().getCell(bp), goal).getPosition();
		return move;
	}
	
	public void moveDumb(){
		try {
			move(this.getBoard().getCell(getNextMoveDumb()));
			Thread.sleep(getBoard().PLAYER_PLAY_INTERVAL);
		}catch(InterruptedException e ) {
			
		}
		
	}
}
