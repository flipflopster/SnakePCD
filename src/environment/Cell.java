package environment;

import java.io.Serializable;

import javax.sound.midi.SysexMessage;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;
import game.AutomaticSnake;
/** Main class for game representation. 
 * 
 * @author luismota
 *
 */
public class Cell {
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement = null;
	private boolean queued = false;
	
	public GameElement getGameElement() {
		return gameElement;
	}
	
	public String toString() {
		return position.toString();
	}

	public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public BoardPosition getPosition() {
		return position;
	}

	public synchronized void request(Snake snake) throws InterruptedException {
		//TODO coordination and mutual exclusion
		while(isOcupied()) {
			queued = true;
			this.wait();
		}
		ocuppyingSnake = snake;
		queued = false;
	}

	public synchronized void release() throws InterruptedException {
		ocuppyingSnake = null;
		if(queued)
			this.notify();
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake != null;
	}
	
	public void setGameElement(GameElement element) {
		// TODO coordination and mutual exclusion
		gameElement = element;

	}

	public boolean isOcupied() {
		return isOcupiedBySnake() || (gameElement!=null && gameElement instanceof Obstacle);
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public synchronized Goal removeGoal() {
		// TODO
		Goal ge = (Goal)gameElement;
		gameElement = null;
		return ge;
	}
	
	public void removeObstacle() {
	//TODO
	}

	public Goal getGoal() {
		return (Goal)gameElement;
	}


	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}
	
	

}
