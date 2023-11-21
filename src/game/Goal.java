package game;

import environment.Board;
import environment.LocalBoard;

public class Goal extends GameElement  {
	
	private int value = 1;
	private Board board;
	public static final int MAX_VALUE = 10;
	
	public Goal(Board board2) {
		this.board = board2;
	}
	
	public int getValue() {
		return value;
	}
	
	public void incrementValue() {
		//TODO
		value++;
	}

	public int captureGoal() {
		//TODO
		incrementValue();
		board.addGameElement(this);
		
		board.interruptSnakes(false);		// Para quando o goal mudar de posição as snakes
											// mudarem de direção.	
		return getValue() - 1;
	}
}
