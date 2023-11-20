package environment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.ObstacleMover;
import game.Snake;

public abstract class Board extends Observable {
	public Cell[][] cells;
	private BoardPosition goalPosition;
	public static final long PLAYER_PLAY_INTERVAL = 100;
	public static final long REMOTE_REFRESH_INTERVAL = 200;
	public static final int NUM_COLUMNS = 30;
	public static final int NUM_ROWS = 30;
	
	protected LinkedList<Snake> snakes = new LinkedList<Snake>();
	
	protected ExecutorService pool;
	private LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();
	protected LinkedList<ObstacleMover> movers = new LinkedList<ObstacleMover>();
	protected boolean isFinished;
	

	public Board() {
	    isFinished = false;
		cells = new Cell[NUM_COLUMNS][NUM_ROWS];
		for (int x = 0; x < NUM_COLUMNS; x++) {
			for (int y = 0; y < NUM_ROWS; y++) {
				cells[x][y] = new Cell(new BoardPosition(x, y));
			}
		}

	}

	public Cell getCell(BoardPosition cellCoord) {
		return cells[cellCoord.x][cellCoord.y];
	}

	public BoardPosition getRandomPosition() {
		return new BoardPosition((int) (Math.random() *NUM_ROWS),(int) (Math.random() * NUM_ROWS));
	}

	public BoardPosition getGoalPosition() {
		return goalPosition;
	}

	public void setGoalPosition(BoardPosition goalPosition) {
		this.goalPosition = goalPosition;
	}
	
	public void addGameElement(GameElement gameElement) {
		boolean placed = false;
		while(!placed) {
			BoardPosition pos = getRandomPosition();
			if(!getCell(pos).isOcupied() && !getCell(pos).isOcupiedByGoal()) {
				if(gameElement instanceof Goal) {
					if(((Goal) gameElement).getValue() <= 9) {
						getCell(pos).setGameElement(gameElement);
						setGoalPosition(pos);
					} else endGame();
//					System.out.println("Goal placed at:" + pos);
				} else getCell(pos).setGameElement(gameElement);
				placed = true;
			} 
		}
	}

	private void endGame() {
		isFinished = true;
		getCell(getGoalPosition()).removeGoal();
		for(Snake s : getSnakes())
			s.interrupt();
		for(ObstacleMover o : movers)
			o.interrupt();
		pool.shutdownNow();
		setChanged();
	}

	public List<BoardPosition> getNeighboringPositions(Cell cell) {
		ArrayList<BoardPosition> possibleCells=new ArrayList<BoardPosition>();
		BoardPosition pos=cell.getPosition();
		if(pos.x > 0)
			possibleCells.add(pos.getCellLeft());
		if(pos.x < NUM_COLUMNS-1)
			possibleCells.add(pos.getCellRight());
		if(pos.y > 0)
			possibleCells.add(pos.getCellAbove());
		if(pos.y < NUM_ROWS-1)
			possibleCells.add(pos.getCellBelow());
		return possibleCells;

	}	

	protected Goal addGoal() {
		Goal goal = new Goal(this);
		addGameElement( goal);
		return goal;
	}

	protected void addObstacles(int numberObstacles) {
		// clear obstacle list , necessary when resetting obstacles.
		getObstacles().clear();
		while(numberObstacles > 0) {
			Obstacle obs = new Obstacle(this);
			addGameElement(obs);
			getObstacles().add(obs);
			numberObstacles--;
		}
	}
	
	public LinkedList<Snake> getSnakes() {
		return snakes;
	}

	@Override
	public void setChanged() {
		super.setChanged();
		notifyObservers();
	}

	public LinkedList<Obstacle> getObstacles() {
		return obstacles;
	}

	public abstract void init(); 
	
	public abstract void handleKeyPress(int keyCode);

	public abstract void handleKeyRelease();	

	public void addSnake(Snake snake) {
		snakes.add(snake);
	}
	
	// Metodo para as cobras manejarem as interrupcoes.
	public boolean isFinished() { return isFinished; }
	
	// Metodo auxiliar para a colocacao das snakes.
	public LinkedList<Cell> getEmptyCellsList(int column) {
		Cell[] cellarr = this.cells[column];
		LinkedList<Cell> cellLst = new LinkedList<Cell>();
		
		for (int i = 0; i != cellarr.length; i++) {
			Cell c = cellarr[i];
			if(!c.isOcupied())
				cellLst.add(c);
		}
		
		return cellLst;
	}

}