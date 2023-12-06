package gui;

import java.io.Console;
import java.io.IOException;

import javax.net.ssl.StandardConstants;

import environment.LocalBoard;
import game.Server;

public class Main {
	
	// Variavel para quando uma snake coletar um goal, avisar todas as outras snakes que o goal trocou de lugar.
	private static final boolean INTERRUPT_SNAKES = true;
	
	public static void main(String[] args) {
		LocalBoard board = new LocalBoard(INTERRUPT_SNAKES);
		SnakeGui game = new SnakeGui(board, 600, 0); 
		game.init();
		// Launch server
		// TODO
		
	}
}
