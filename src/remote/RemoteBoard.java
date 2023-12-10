package remote;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.Executors;

import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import game.Goal;
import game.Obstacle;
import game.ObstacleMover;
import game.Snake;

/** Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Server.
 * Only for part II of the project.
 * @author luismota
 *
 */
public class RemoteBoard extends Board implements Serializable {
	
	private static final int NUM_OBSTACLES = 25;
	private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;
	
	private int key = KeyEvent.VK_RIGHT;
	
	public RemoteBoard() {
		addObstacles(NUM_OBSTACLES);
		for (int i = 0; i < NUM_SIMULTANEOUS_MOVING_OBSTACLES; i++) {
			for(int j = 0; j < NUM_OBSTACLES; j++) {
				ObstacleMover mover = new ObstacleMover(getObstacles().get(j), this);
				movers.add(mover);
			}
		}
		Goal goal = addGoal();
	}
	
	@Override
	public void handleKeyPress(int keyCode) {
		key = keyCode;
	}

	@Override
	public void handleKeyRelease() {
	}

	@Override
	public void init() {
		// TODO 
		pool = Executors.newFixedThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES);
		for(ObstacleMover s : movers)
			pool.execute(s);
	}

	public int getKey() { return key; }
	

}
