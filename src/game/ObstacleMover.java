package game;

import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

public class ObstacleMover extends Thread {
	private Obstacle obstacle;
	private LocalBoard board;
	private BoardPosition prePosition;
	private boolean posPositionOcuppied = false;

	public ObstacleMover(Obstacle obstacle, LocalBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}

	@Override
	public void run() {

		if(obstacle.getRemainingMoves() > 0) {
			if(obstacle.getCurrentPosition() != null) {
				prePosition = obstacle.getCurrentPosition();
			}
			else { prePosition = getPosition(); }
				
			while(!posPositionOcuppied) {
				Cell aux = board.getCell(board.getRandomPosition());
				if(!aux.isOcupied() && !aux.isOcupiedByGoal()) {
					aux.setGameElement(obstacle);
					board.getCell(prePosition).removeObstacle();
					posPositionOcuppied = true;
					board.setChanged();
				}
			}
			obstacle.decrementMoves();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("teste");
				e.printStackTrace();
			}
		}



	}
	public BoardPosition getPosition() {
		for (int i = 0; i < board.cells.length; i++) {
			for (int j = 0; j < board.cells[i].length; j++) {
				Cell aux = board.getCell(new BoardPosition(i,j));
				if(!aux.isOcupiedByGoal() && !aux.isOcupiedBySnake() && aux.isOcupied()) {
					if((Obstacle)aux.getGameElement()==obstacle)
						return new BoardPosition(i,j);
				}
			}
		}
		return null;
	}
}

