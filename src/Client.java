import java.io.IOException;
import java.net.Socket;

public class Client extends ComputeNode {

	private static final int PORT = 1234;

	public Client(String ipaddress, int port) {
		Thread client = new Thread(() -> startClient(ipaddress, PORT));
		client.start();
	}

	/**
	 * Starts the client. Client connects to server or waits for server to start
	 * indefinitely.
	 * 
	 * @param hostname IPaddress of server.
	 * @param port     Port the server is listening on.
	 * @param message  What to send the server.
	 */
	private void startClient(String hostname, int port) {
		while (true) {
			System.out.println("Trying to connect to Server");
			try (Socket socket = new Socket(hostname, port)) {
				System.out.println("Connected to server at " + hostname + ":" + port);
				startListening(socket);
//				handleConnection(socket, message, NodeType.CLIENT);
				break;
			} catch (IOException e) {
				System.out.println("Failed to connect");
			}
		}
	}
}