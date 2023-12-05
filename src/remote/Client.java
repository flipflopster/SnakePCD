package remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import game.HumanSnake;

/** Remore client, only for part II
 * 
 * @author luismota
 *
 */

public class Client {

	private Socket connection;
	
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
			connection = new Socket(ipAddr, port);
			
			getStream();
			processConnection();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally { closeConnection(); }
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

	private void processConnection() {
		
	}

	private void getStream() throws IOException {
		out = new ObjectOutputStream(connection.getOutputStream());
		in = new ObjectInputStream(connection.getInputStream());	
	}
	
	public static void main(String[] args) throws UnknownHostException {
		// TODO
		Client c = new Client(InetAddress.getByName("localhost"), 12345);
		c.runClient();
	}

}
