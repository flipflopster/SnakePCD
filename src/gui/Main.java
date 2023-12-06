package gui;

import java.io.Console;
import java.io.IOException;

import javax.net.ssl.StandardConstants;

import environment.LocalBoard;
import game.Server;

public class Main {
	
	// Variavel para quando uma snake coletar um goal, avisar todas as outras snakes que o goal trocou de lugar.
	public static final boolean INTERRUPT_SNAKES = true;
	
	// Se true, entao quando uma cobra coleta um goal cresce esse numero de celulas
	public static final boolean BIG_GOAL = false;
	
	public static void main(String[] args) {
		LocalBoard board = new LocalBoard();
		SnakeGui game = new SnakeGui(board, 600, 0); 
		game.init();
		// Launch server
		// TODO
		
	}
}
