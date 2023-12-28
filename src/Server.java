import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends ComputeNode {

	private static int clientCounter = 0;
	private final int PORT;
	private static Object lock = new Object();// @TODO look at
	private ArrayList<Socket> clientList = new ArrayList<Socket>();

	public Server(int port) {
		PORT = port;
		startServer(PORT);
	}

	/**
	 * Starts the server instance. Server listens on given port.
	 * 
	 * @param port Port to listen for.
	 */
	private void startServer(int port) {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server is listening on port " + port);

			while (true) {
				try {
					synchronized (lock) {
						Socket socket = serverSocket.accept();
						System.out.println("Client " + clientCounter + "connected: " + socket.getInetAddress());
						clientList.add(socket);
						startListening(socket);

						clientCounter++;
					}
				} catch (IOException e) {
					System.out.println("Server exception: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println("Could not listen on port " + port);
		}
	}

	/**
	 * Get the i'th client that connected to the server.
	 * 
	 * @return The i'th client to have connected. List includes closed sockets as
	 *         well.
	 */
	public Socket getClient(int i) {
		return clientList.get(i);
	}
}
