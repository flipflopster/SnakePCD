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
		this.notify();
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake != null;
	}
	
	public void setGameElement(GameElement element) {
		// TODO coordination and mutual exclusion
		lock.lock();
		gameElement = element;
		lock.unlock();
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
		gameElement = null;
	}

	public Goal getGoal() {
		return (Goal)gameElement;
	}


	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}
	
	

}
