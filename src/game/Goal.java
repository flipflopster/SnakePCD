package game;

import environment.Board;
import environment.LocalBoard;

public class Goal extends GameElement  {
	
	private int value = 1;
	private Board board;
	public static final int MAX_VALUE = 10;
	private boolean interruptSnakes;
	
	public Goal(Board board, boolean interruptSnakes) {
		this.board = board;
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
		
		if(interruptSnakes)
			board.interruptSnakes(false);	// Para quando o goal mudar de posição as snakes
											// mudarem de direção.	
		return getValue() - 1;
	}
}
