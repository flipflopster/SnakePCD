package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import environment.Board;
import gui.SnakeGui;
import remote.RemoteBoard;

public class Server {
	// TODO
	
	protected ExecutorService pool;
	//private ConnectionHandler h;
	private ServerSocket ss;
	private static final int PORT = 12345;
	
	private RemoteBoard b;
	
	public void runServer(RemoteBoard b) {
		try {
			pool = Executors.newFixedThreadPool(10);
			ss = new ServerSocket(PORT);
		
			this.b = b;
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
		
		pool.execute(new ConnectionHandler(client));
	}
	
	private class ConnectionHandler extends Thread {
		
		private Socket connection;
		
		private ObjectInputStream in;
		private ObjectOutputStream out;
		
		private HumanSnake hs;
		
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
			hs = new HumanSnake(b.getNextSnakeId(), b);
			b.addSnake(hs);
			hs.start();
			while(!b.isFinished()) {
				try {
					out.reset();
					b.setChanged();
					BoardData bd = new BoardData(b);
					out.writeObject(bd);
					int key = (int) in.readObject(); // Thread para a espera.
					hs.changeDirection(key);
					b.setChanged();
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
		RemoteBoard board = new RemoteBoard();
		Server server = new Server();
		server.runServer(board);
	}
	
}
