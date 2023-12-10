package remote;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;

import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import game.Goal;
import game.Obstacle;
import game.Snake;

/** Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Server.
 * Only for part II of the project.
 * @author luismota
 *
 */
public class RemoteBoard extends Board implements Serializable {
	
	private int key = KeyEvent.VK_RIGHT;
	
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
		for(Snake s : snakes)
			s.start();
	}

	public int getKey() { return key; }
	

}
