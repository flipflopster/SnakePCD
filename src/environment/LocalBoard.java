package environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.ObstacleMover;
import game.Server;
import game.Snake;
import game.AutomaticSnake;

/** Class representing the state of a game running locally
 * 
 * @author luismota
 *
 */
public class LocalBoard extends Board {
	
	private static final int NUM_SNAKES = 5;
	private static final int NUM_OBSTACLES = 25;
	private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;
	private static final String[] SNAKE_NAMES = {"Big Boss", "Solid Snake", "Liquid Snake", "Solidus Snake", "Raiden"};

	public LocalBoard(boolean interruptSnakes) {
		
		for (int i = 0; i < NUM_SNAKES; i++) {
			AutomaticSnake snake = new AutomaticSnake(i, this, SNAKE_NAMES[i]);
			snakes.add(snake);
		}

		addObstacles( NUM_OBSTACLES);
		for (int i = 0; i < NUM_SIMULTANEOUS_MOVING_OBSTACLES; i++) {
			for(int j = 0; j < NUM_OBSTACLES; j++) {
				ObstacleMover mover = new ObstacleMover(getObstacles().get(j), this);
				movers.add(mover);
			}
		}
		
		Goal goal = addGoal(interruptSnakes);
//		System.err.println("All elements placed");
	}

	public void init() {
		for(Snake s : snakes)
			s.start();
		// TODO: launch other threads
		pool = Executors.newFixedThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES);
		for(ObstacleMover s : movers)
			pool.execute(s);
	}

	@Override
	public void handleKeyPress(int keyCode) {
		// do nothing... No keys relevant in local game
	}

	@Override
	public void handleKeyRelease() {
		// do nothing... No keys relevant in local game
	}





}
