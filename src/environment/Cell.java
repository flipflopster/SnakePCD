package environment;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	private Lock lock = new ReentrantLock();
	
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
		while(isOcupied())
			this.wait();
		ocuppyingSnake = snake;
		queued = false;
	}

	public synchronized void release() {
		ocuppyingSnake = null;
		this.notifyAll();
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake != null;
	}
	
	public synchronized void setGameElement(GameElement element) {
		// TODO coordination and mutual exclusion
		gameElement = element;
	}

	public boolean isOcupied() {
		return isOcupiedBySnake() || (gameElement != null && gameElement instanceof Obstacle);
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public synchronized Goal removeGoal() {
		// TODO
		Goal ge = null;
		if(isOcupiedByGoal()) {
			ge = (Goal)gameElement;
			gameElement = null;
			return ge;
		} else 
			return ge;
	}
	
	public synchronized void removeObstacle() {
	//TODO
		gameElement = null;
		this.notifyAll();
	}

	public Goal getGoal() {
		return (Goal)gameElement;
	}


	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}
	
	public Cell getBest(Cell c, BoardPosition goalPos) {
		if(this.getPosition().distanceTo(goalPos) < c.getPosition().distanceTo(goalPos))
			return this;
		else if(this.getPosition().distanceTo(goalPos) > c.getPosition().distanceTo(goalPos))
			return c;
		else if(this.isOcupied())
			return c;
		else if(c.isOcupied())
			return this;
		else return this;
	}

}
