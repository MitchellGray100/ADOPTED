import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

class Worker extends ComputeNode {
	/**
	 * The socket of the Leader
	 */
	private Socket serverSocket;

	/**
	 * Constructor to create a Worker object
	 * 
	 * @param leaderIPAddress The IPAdress of the Node running the Leader
	 */
	public Worker(String leaderIPAddress) {
		// Connect to the Leader
		try (BufferedReader reader = new BufferedReader(new FileReader("ipaddress.txt"))) {
			String IPADDRESS = reader.readLine(); // Reads the first line of the file
			startClient(IPADDRESS, ComputeNode.PORT);
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Run ADOPT code to run LFTJ on the current task's HyperCube
	 */
	public void processHypercube() {
		// *preexisting LFTJ method*
		// output results to output;
	}

	/**
	 * Send the results of processHypercube() to the Leader
	 */
	public void respond() {
		sendMessage(getServerSocket(), null);// Todo replace null with LFTJ results
	}

	/**
	 * Starts the client. Client connects to server or waits for server to start
	 * indefinitely.
	 * 
	 * @param hostname IPaddress of server.
	 * @param port     Port the server is listening on.
	 */
	private void startClient(String hostname, int port) {
		// Continuously tries to connect to server until successful.
		// Needs to be changed for fault tolerance.
		// Timeout to elect a new leader
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

	/**
	 * Returns the socket of the Leader
	 * 
	 * @return Socket of Leader
	 */
	public Socket getServerSocket() {
		return serverSocket;
	}

	@Override
	void startListening(Socket socket) {
		System.out.println("Started listening");
		try {
			InputStream socketInputStream = socket.getInputStream();
			DataInputStream dis = new DataInputStream(socketInputStream);

			// Asynchronously read data sent to our socket
			Thread readThread = new Thread(() -> {
				try {
					// While there are no errors and we received data
					while (!socket.isClosed() && !errorFound[0]) {
						// Grab length of message (first int of message)
						int length = dis.readInt();
						// Allocate enough room for the message and then read
						byte[] message = new byte[length];
						dis.readFully(message);
						// Deserialize the data sent from the leader
						Object[] deserializedData = deserializeData(message);
					}
				} catch (IOException | ClassNotFoundException e) {
					// Catch error and set errorFound to true
					System.out.println("Error reading from socket: " + e.getMessage() + " " + socket.getInetAddress());
					errorFound[0] = true;
				}
			});
			readThread.start();

		} catch (IOException e) {
			System.err.println(socket.getInetAddress() + " Error encountered when listening: " + e.getMessage());
		}
	}

	/**
	 * Deserializes the data sent by the Server
	 * 
	 * @param data Serialized data from Server
	 * @return Object array containing attributeOrder, budget, and hyperCube
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object[] deserializeData(byte[] data) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
				ObjectInputStream ois = new ObjectInputStream(bis)) {

			int[] attributeOrder = (int[]) ois.readObject();
			long budget = ois.readLong();
			Hypercube nextHyperCube = (Hypercube) ois.readObject();

			return new Object[] { attributeOrder, budget, nextHyperCube };
		}
	}
}
