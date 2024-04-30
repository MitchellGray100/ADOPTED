import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

class Leader extends ComputeNode {
	/**
	 * The config file to get fields from
	 */
	private ConfigFile config;

	/**
	 * The MCST to receive task info from
	 */
	private MonteCarloSearchTree MCST;

	/**
	 * Keeps track of number of clients connected to leader.
	 */
	private static int clientCounter = 0;

	/**
	 * Store all client Sockets
	 */
	private ArrayList<Socket> clientList = new ArrayList<Socket>();

	/**
	 * Given an IPAdress, get the corresponding socket
	 */
	private HashMap<InetAddress, Socket> IPSocketMap = new HashMap<InetAddress, Socket>();

	/**
	 * Constructor for the leader
	 * 
	 * @param configPath Path to the config file
	 */
	public Leader(String configPath) {
		startServer(ComputeNode.PORT);
		config = ConfigFile.getInstance(configPath);

		// Wait until all clients have connected.
		// Needs changed for fault tolerance.
		while (config.getIPaddresses().length != getClientListSize()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		MCST = new MonteCarloSearchTree();
	}

	// Run all queries in config file on the workers.
	public void runQueries() {
		ArrayList<String> queries = config.getQueries();
		for (String query : queries) {
			runQuery(query);
		}
	}

	// Run query across all workers
	public void runQuery(String query) {
		for (String ipaddress : config.getIPaddresses()) {
			try {
				runWithWorker(this.getSocket(InetAddress.getByName(ipaddress)));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Start running query on a worker.
	 * 
	 * @param socket The socket to communicate to the worker with.
	 */
	public void runWithWorker(Socket socket) {
		// Send initial message to workers
		send(socket, MCST.getAttributeOrder(), config.getBudget(), MCST.getNextHyperCube());
		// Wait for response
//		while(connected) {
//			if(response) {
//				appendOutputFile(response.file, config.getResultPath());
//				if(MCST.hasNextHypercube())
//					send(ipaddress, "182", attributeOrder, hypercube)
//				else
//					endConnectionWithIPAddress(ipaddress);
//			}
//		}
	}

	/**
	 * Send a task to a worker
	 * 
	 * @param socket         The socket to communnicate with the worker.
	 * @param attributeOrder The attribute order to run the task with.
	 * @param budget         The time quantum for the ADOPT code
	 * @param nextHyperCube  The hypercube to run on
	 * @return Whether or not the message was sent successfully
	 */
	public boolean send(Socket socket, int[] attributeOrder, long budget, Hypercube nextHyperCube) {
		byte[] serializedData;
		try {
			// Serialize the task and send it
			serializedData = serializeData(attributeOrder, budget, nextHyperCube);
			return sendMessage(socket, serializedData);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Takes in the data needed to send to the client and turns it into bytes.
	 * 
	 * @param attributeOrder Attribute order to run on
	 * @param budget         time allocated to run
	 * @param nextHyperCube  Hypercube to process
	 * @return The serialized data
	 * @throws IOException
	 */
	private byte[] serializeData(int[] attributeOrder, long budget, Hypercube nextHyperCube) throws IOException {
		byte[] serializedData;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos)) {

			oos.writeObject(attributeOrder);
			oos.writeLong(budget);
			oos.writeObject(nextHyperCube);

			oos.flush();
			serializedData = bos.toByteArray();
		}

		try (ByteArrayOutputStream bosWithLength = new ByteArrayOutputStream();
				DataOutputStream dosWithLength = new DataOutputStream(bosWithLength)) {
			// Write length of message and then the data
			dosWithLength.writeInt(serializedData.length);
			dosWithLength.write(serializedData);
			return bosWithLength.toByteArray();
		}
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

				// Continuously add clients as they attempt to connect.
				// A failed worker can crash and restart and reconnect successfully here.
				while (true) {
					try {
						Socket socket = serverSocket.accept();
						System.out.println("Client " + clientCounter + " connected: " + socket.getInetAddress());
						clientList.add(socket);
						IPSocketMap.put(socket.getInetAddress(), socket);
						startListening(socket);

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

	/**
	 * Returns the ClientList size
	 * 
	 * @return the size.
	 */
	public int getClientListSize() {
		return clientList.size();
	}

	/**
	 * Gets the socket corresponding to a worker IPaddress.
	 * 
	 * @param address
	 * @return the socket
	 */
	public Socket getSocket(InetAddress address) {
		return IPSocketMap.get(address);
	}

	@Override
	void startListening(Socket socket) {
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			Thread readThread = new Thread(() -> {
				try {
					byte[] receivedMessage;
					while (!socket.isClosed() && !errorFound[0]) {
						// Read the length of the next message
						int length = in.readInt();
						if (length > 0) {
							// Read the message
							byte[] message = new byte[length];
							in.readFully(message);
						}
//						String results = deserializeClientMessage(receivedMessage);
//						appendOutputFile(results, config.getResultPath());
//						if(MCST.hasNextHypercube())
//							send(socket, attributeOrder, budget, hypercube)
//						else
//							socket.close();
					}
					in.close();
				} catch (IOException e) {
					System.out.println("Error reading from socket: " + e.getMessage() + " " + socket.getInetAddress());
				}
			});
			readThread.start();

		} catch (IOException e) {
			System.err.println(socket.getInetAddress() + " Error encountered when listening: " + e.getMessage());
		}
	}
}
