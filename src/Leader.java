import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

class Leader extends ComputeNode {
	private ConfigFile config;
	private MonteCarloSearchTree MCST;
	public ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
	private static int clientCounter = 0;
	private ArrayList<Socket> clientList = new ArrayList<Socket>();
	private HashMap<InetAddress, Socket> IPSocketMap = new HashMap<InetAddress, Socket>();

	public Leader(String configPath) {
		startServer(ComputeNode.PORT);
		config = ConfigFile.getInstance(configPath);

		while (config.getIPaddresses().length != getClientListSize()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		MCST = new MonteCarloSearchTree();
	}

	public void runQueries() {
		ArrayList<String> queries = config.getQueries();
		for (String query : queries) {
			runQuery(query);
		}
	}

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

	public void runWithWorker(Socket socket) {
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

	public boolean sendTest(Socket socket, String message) {
		return sendMessage(socket, message);
	}

	private boolean send(Socket socket, int[] attributeOrder, long budget, Hypercube nextHyperCube) {
		String serializedData = serializeData(attributeOrder, budget, nextHyperCube);
		return sendMessage(socket, serializedData);
	}

	/**
	 * Takes in the data needed to send to the client and turns it into bytes.
	 * 
	 * @param attributeOrder Attribute order to run on
	 * @param budget         time allocated to run
	 * @param nextHyperCube  Hypercube to process
	 * @return The serialized data
	 */
	private String serializeData(int[] attributeOrder, long budget, Hypercube nextHyperCube) {
		return null;// TODO
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

	public int getClientListSize() {
		return clientList.size();
	}

	public Socket getSocket(InetAddress address) {
		return IPSocketMap.get(address);
	}

	@Override
	void startListening(Socket socket) {
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			Thread readThread = new Thread(() -> {
				try {
					String receivedMessage;
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
