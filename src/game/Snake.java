package game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.naming.directory.InvalidAttributesException;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
/** Base class for representing Snakes.
 * Will be extended by HumanSnake and AutomaticSnake.
 * Common methods will be defined here.
 * @author luismota
 *
 */
public abstract class Snake extends Thread implements Serializable {
	protected static final int DELTA_SIZE = 10;
	protected LinkedList<Cell> cells = new LinkedList<Cell>();
	protected int size = 5;
	protected AtomicInteger sizeA = new AtomicInteger(5);
	private int id;
	private Board board;
	private boolean resetButton;
	
	public Snake(int id, Board board, String name) {
		super(name);
		this.id = id;
		this.board = board;
		this.resetButton = false;
	}

	public int getSize() {
		return size;
	}

	public int getIdentification() {
		return id;
	}

	public int getLength() {
		return cells.size();
	}
	
	public LinkedList<Cell> getCells() {
		return cells;
	}
	
	protected void move(Cell cell) throws InterruptedException {
		Goal g = cell.removeGoal();
		if(g != null) {
			int plus = g.captureGoal();
			if(gui.Main.BIG_GOAL)
				size = size + plus;
			else size++;
		}
			
		cell.request(this);
		cells.add(cell);
		getBoard().setChanged();
		if(cells.size() == size) {
			cells.get(0).release();
			cells.remove(0);
		}
	}
	
	public LinkedList<BoardPosition> getPath() {
		LinkedList<BoardPosition> coordinates = new LinkedList<BoardPosition>();
		for (Cell cell : cells) {
			coordinates.add(cell.getPosition());
		}

		return coordinates;
	}
	
	public void setReset(Boolean resetVal) { resetButton = resetVal; }
	
	protected Boolean getReset() { return resetButton; }
	
	protected void doInitialPositioning() throws InvalidAttributesException {
		// Random position on the first column. 
		// At startup, snake occupies a single cell
		LinkedList<Cell> emptyCells = this.board.getEmptyCellsList(0);
		if(emptyCells.size() == 0)
			throw new InvalidAttributesException("Board column 0 is full.");
		int cellY = (int) (Math.random() * emptyCells.size());
		
		BoardPosition at = emptyCells.get(cellY).getPosition();
		
		try {
			board.getCell(at).request(this);
			getBoard().setChanged();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		cells.add(board.getCell(at));
		System.out.println(getName() + " starting at:" + getCells().getLast());		
	}
	
	public Board getBoard() { return board; }
	
}
