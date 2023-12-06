package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import environment.Board;

public class Server {
	// TODO
	
	private ConnectionHandler h;
	private ServerSocket ss;
	private static final int PORT = 12345;
	
	private Board b;
	
	private void runServer() {
		try {
			
			ss = new ServerSocket(PORT);
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
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally { closeConnection(); }
		}
		
		private void processConnection() throws IOException, ClassNotFoundException {
			
			do {
				
				int key = (int) in.readObject(); // Thread para a espera.
				
				out.writeObject(b);
				
			} while(!b.isFinished());
			
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
	
}
