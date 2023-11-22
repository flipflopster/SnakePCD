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
	// private Lock lock = new ReentrantLock();
	
	public GameElement getGameElement() {
		return gameElement;
	}
	
	public String toString() { return position.toString(); }

	public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public BoardPosition getPosition() { return position; }

	public synchronized void request(Snake snake) throws InterruptedException {
		while(isOcupied())
			this.wait();
		ocuppyingSnake = snake;
	}

	public synchronized void release() {
		ocuppyingSnake = null;
		this.notifyAll();
	}

	public boolean isOcupiedBySnake() { return ocuppyingSnake != null; }
	
	public synchronized void setGameElement(GameElement element) {
		gameElement = element;
	}

	public boolean isOcupied() {
		return isOcupiedBySnake() || (gameElement != null && gameElement instanceof Obstacle);
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public synchronized Goal removeGoal() {
		Goal ge = null;
		if(isOcupiedByGoal()) {
			ge = (Goal)gameElement;
			gameElement = null;
		}
		return ge;
	}
	
	public synchronized void removeObstacle() {
		gameElement = null;
		this.notifyAll();
	}

	public Goal getGoal() {
		return (Goal)gameElement;
	}


	public boolean isOcupiedByGoal() {
		return (gameElement != null && gameElement instanceof Goal);
	}
	
	public Cell getBest(Cell c, BoardPosition goalPos) {
		if(this == null) return c;
		if(c == null) return this;
		Double thisDistance = this.getPosition().distanceTo(goalPos);
		Double cDistance = c.getPosition().distanceTo(goalPos);
		if(thisDistance < cDistance) return this;
		if(thisDistance > cDistance) return c;
		if(this.isOcupied()) return c;
		if(c.isOcupied()) return this;
		return this;
	}

}
