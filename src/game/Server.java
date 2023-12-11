package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import environment.Board;
import environment.LocalBoard;
import gui.SnakeGui;
import remote.RemoteBoard;

public class Server {
	// TODO
	
	private ExecutorService clientPool;
	private ServerSocket ss;
	private static final int PORT = 12345;
	
	private static final String[] NAMES = {"Kazuhira Miller", "Revolver Ocelot"};
	private int i;
	
	private Board board;
	
	public void runServer(Board b) {
		try {
			i = 0;
			clientPool = Executors.newFixedThreadPool(10);
			ss = new ServerSocket(PORT);
		
			board = b;
			
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
		
		clientPool.execute(new ConnectionHandler(client));
	}
	
	private class ConnectionHandler extends Thread {
		
		private Socket connection;
		
		private ObjectInputStream in;
		private ObjectOutputStream out;
		
		private HumanSnake clientSnake;
		
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
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally { closeConnection(); }
		}
		
		private void processConnection() throws IOException, InterruptedException {
			clientSnake = new HumanSnake(board.getNextSnakeId(), board, NAMES[i]); i++;
			
			board.addSnake(clientSnake); clientSnake.start();
			
			while(!board.isFinished() && clientSnake.isAlive()) {
				try {
					out.reset();
					out.writeObject(new BoardData(board));
					clientSnake.changeDirection((int) in.readObject());
					sleep(Board.REMOTE_REFRESH_INTERVAL);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
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
	
	public static void main(String[] args) {
		LocalBoard board = new LocalBoard();
		Server server = new Server();
		server.runServer(board);
	}
	
}
