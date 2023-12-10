package game;

import java.io.Serializable;
import java.util.LinkedList;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;

public class BoardData implements Serializable{

	private Cell[][] cells;
	
	private LinkedList<Snake> snakes;
	private boolean isFinished;

	public BoardData(Board b) {
		this.cells = b.cells;
		this.snakes = b.getSnakes();
		this.isFinished = b.isFinished();
	}
	
	public Cell[][] getCells() { return cells; }
	
	public LinkedList<Snake> getSnakes() { return snakes; }

	public boolean isFinished() { return isFinished; }
	
}
