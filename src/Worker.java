import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class Worker extends ComputeNode {
	private Socket serverSocket;

	public Worker(String leaderIPAddress) {
		try (BufferedReader reader = new BufferedReader(new FileReader("ipaddress.txt"))) {
			String IPADDRESS = reader.readLine(); // Reads the first line of the file
			startClient(IPADDRESS, ComputeNode.PORT);
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}
	}

	public void processHypercube() {
		// *preexisting LFTJ method*
		// output results to output;
	}

	public void respond() {
		sendMessage(getServerSocket(), null);// Todo replace null with LFTJ results
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
			try {
				Socket socket = new Socket(hostname, port);
				System.out.println("Connected to server at " + hostname + ":" + port);
				serverSocket = socket;
				startListening(socket);
				break;
			} catch (IOException e) {
				System.out.println("Failed to connect");
			}
		}
	}

	public Socket getServerSocket() {
		return serverSocket;
	}

	@Override
	void startListening(Socket socket) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			Thread readThread = new Thread(() -> {
				try {
					String receivedMessage;
					while ((receivedMessage = reader.readLine()) != null && !errorFound[0]) {
						System.out.println(receivedMessage);
					}
					reader.close();
				} catch (IOException e) {
					System.out.println("Error reading from socket: " + e.getMessage() + " " + socket.getInetAddress());
					errorFound[0] = true;
				}
			});
			readThread.start();

		} catch (IOException e) {
			System.err.println(socket.getInetAddress() + " Error encountered when listening: " + e.getMessage());
		}
	}
}
