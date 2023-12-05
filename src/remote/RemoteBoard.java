package remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import game.Goal;
import game.HumanSnake;
import game.Obstacle;
import game.Snake;

/** Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Server.
 * Only for part II of the project.
 * @author luismota
 *
 */

public class RemoteBoard extends Board{
	
	private ConnectionHandler h;
	private ServerSocket ss;
	private static final int PORT = 12345;
	
	private HumanSnake bb;
	
	private void runServer() {
		try {
			ss = new ServerSocket(PORT, 2);
			
			while(true) {
				waitForConnections();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void waitForConnections() throws IOException {
		System.out.println("Waitin...");
		Socket client = ss.accept();
		System.out.println("Found some dude by the name of: " + client.getInetAddress().getHostName());
		
		h = new ConnectionHandler(client);
		h.start();
	}
	
	private class ConnectionHandler extends Thread {
		
		private Socket connection;
		
		private ObjectInputStream in;
		private ObjectOutputStream out;
		
		public ConnectionHandler(Socket connection) {
			this.connection = connection;
		}
		
		@Override
		public void run() {
			try {
				getStream();
				processConnection();
			} catch (IOException e) {
				e.printStackTrace();
			} finally { closeConnection(); }
		}
		
		private void processConnection() {
//			String messagkey;
//			
//			do {
//				messagkey = in.nextLine(); // Thread para a espera.
//				System.out.println(connection.getInetAddress().getHostName() + "RemoteBoardEco" + messagkey);
//				
//				out.println("Server Eco: " + messagkey);
//				
//			} while(!"SnakeEndingMessage".equals(messagkey));
			
		}

		private void getStream() throws IOException {
			out = new ObjectOutputStream(connection.getOutputStream());
			in = new ObjectInputStream(connection.getInputStream());	
		}
		
		private void closeConnection() {
			try {
				if(connection != null) connection.close();
				if(in != null) in.close();
				if(out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	@Override
	public void handleKeyPress(int keyCode) {
		//TODO
	}

	@Override
	public void handleKeyRelease() {
		// TODO
	}

	@Override
	public void init() {
		// TODO 		
	}


	

}
