package remote;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientTest {

	public static void main(String[] args) throws UnknownHostException {
		Client c = new Client(InetAddress.getByName("localhost"), 12345);
		c.runClient();
	}
	
}
