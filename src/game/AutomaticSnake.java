package game;

import java.io.Serializable;
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
	
	public AutomaticSnake(int id, LocalBoard board, String name) {
		super(id, board, name);

	}

	@Override
	public void run() {
		try { doInitialPositioning(); } catch (Exception e1) { e1.printStackTrace(); }
		
		System.err.println("initial size:" + cells.size());
		
		while(size <= DELTA_SIZE && !getBoard().isFinished()) {
			BoardPosition nextMove;
			
			if(getReset()) nextMove = getNextPossibleMove();
			else nextMove = getNextMoveDumb();
			
			try {
				if(nextMove != null)
					move(getBoard().getCell(nextMove));
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
			} catch (InterruptedException e) {
				System.out.println(getName() + " interrupted");
			}
		}
		System.out.println(getName() + " is joever");
	}
	
	private BoardPosition getNextPossibleMove() {
		List<BoardPosition> adjacentMoves = getBoard().getNeighboringPositions(cells.getLast());

		BoardPosition goal = this.getBoard().getGoalPosition();
		BoardPosition move = adjacentMoves.get(0);
		
		for (BoardPosition bp : adjacentMoves)
			if(!getBoard().getCell(bp).isOcupied())
				move = getBoard().getCell(move).getBest(getBoard().getCell(bp), goal).getPosition();
		
		setReset(false);
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
