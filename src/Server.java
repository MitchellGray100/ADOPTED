import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends ComputeNode {

	private static int clientCounter = 0;
	private final int PORT;
	private static Object lock = new Object();// @TODO look at
	private ArrayList<Socket> clientList = new ArrayList<Socket>();
	private HashMap<InetAddress, Socket> IPSocketMap = new HashMap<InetAddress, Socket>();

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
		Thread searchForClientsThread = new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				System.out.println("Server is listening on port " + port);

				while (true) {
					try {
						Socket socket = serverSocket.accept();
						System.out.println("Client " + clientCounter + " connected: " + socket.getInetAddress());
						clientList.add(socket);
						IPSocketMap.put(socket.getInetAddress(), socket);
						startListening(socket, NodeType.SERVER);

						clientCounter++;
					} catch (IOException e) {
						System.out.println("Server exception: " + e.getMessage());
					}
				}

			} catch (IOException e) {
				System.out.println("Could not listen on port " + port);
			}
		});
		searchForClientsThread.start();
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

	public int getClientListSize() {
		return clientList.size();
	}

	public Socket getSocket(InetAddress address) {
		return IPSocketMap.get(address);
	}

}
