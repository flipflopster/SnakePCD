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
		super(id,board);

	}

	@Override
	public void run() {
		try {
			doInitialPositioning();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.err.println("initial size:"+cells.size());
		while(true) {
		try {
			move(this.getBoard().getCell(getNextMoveDumb()));
			if(cells.size() == size) {
				cells.get(0).release();
				cells.remove(0);
			}
			//Thread.sleep(getBoard().PLAYER_PLAY_INTERVAL);
			Thread.sleep(getBoard().PLAYER_PLAY_INTERVAL);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	
	public BoardPosition getNextMoveDumb(){
		List<BoardPosition> adjacentMoves = getBoard().getNeighboringPositions(cells.getLast());
		List<BoardPosition> possibleMoves = new ArrayList<BoardPosition>();
		List<Cell> snakeBody = getCells();
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
		
		for (BoardPosition bp : new HashSet<BoardPosition>(adjacentMoves))
			if(bp.distanceTo(goal) < move.distanceTo(goal))
				for(Cell c : snakeBody)
					if(!bp.equals(c.getPosition()))
						move = bp;
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
