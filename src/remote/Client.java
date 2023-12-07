package remote;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import environment.Board;
import gui.SnakeGui;

/** Remore client, only for part II
 * 
 * @author luismota
 *
 */

public class Client {
	
	private RemoteBoard b;
	
	private Socket server;
	private InetAddress ipAddr;
	private int port;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public Client(InetAddress byName, int port) {
		ipAddr = byName;
		this.port = port;
	}

	public void runClient() {
		try {
			server = new Socket(ipAddr, port);
			
			getStream();
			processConnection();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally { closeConnection(); }
	}
	
	private void closeConnection() {
		try {
			if(server != null) server.close();
			if(in != null) in.close();
			if(out != null) out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processConnection() throws ClassNotFoundException, IOException {
		b = (RemoteBoard) in.readObject();
		while(!b.isFinished()) {
			out.writeObject(b.getKey());
			
			b = (RemoteBoard) in.readObject();
			b.setChanged();
		}
	}

	private void getStream() throws IOException {
		out = new ObjectOutputStream(server.getOutputStream());
		in = new ObjectInputStream(server.getInputStream());	
	}
	
	private RemoteBoard getBoard() {
		return b;
	}
	
	public static void main(String[] args) throws UnknownHostException {
		Client c = new Client(InetAddress.getByName("localhost"), 12345);
		c.runClient();
		SnakeGui game = new SnakeGui(c.getBoard(), 600, 0);
		game.init();
	}
}
