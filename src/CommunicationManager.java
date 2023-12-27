import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CommunicationManager {
	private static int clientCounter = 0;
	private static Object lock = new Object();

	public static void startServer(int port) {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server is listening on port " + port);

			while (true) {
				try {
					synchronized (lock) {
						Socket socket = serverSocket.accept();
						System.out.println("Client " + clientCounter + "connected: " + socket.getInetAddress());

						Thread clientThread = new Thread(
								() -> handleConnection(socket, String.valueOf(clientCounter), NodeType.SERVER));

						clientThread.start();

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

	public static void startClient(String hostname, int port, String message) {
		while (true) {
			System.out.println("Trying to connect to Server");
			try (Socket socket = new Socket(hostname, port)) {
				System.out.println("Connected to server at " + hostname + ":" + port);
				handleConnection(socket, message, NodeType.CLIENT);
				break;
			} catch (IOException e) {
				System.out.println("Failed to connect");
			}
		}
	}

	public static void handleConnection(Socket socket, String message, NodeType type) {
		final boolean[] errorFound = { false };
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			// Thread for sending messages
			Thread writeThread = new Thread(() -> {
				try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
					while (true && !errorFound[0]) {
						if (type.equals(NodeType.CLIENT))
							writer.println(message);
						else
							writer.println("Server received message from: client " + message);

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					writer.close();
					consoleReader.close();
				} catch (IOException e) {
					System.out.println("Error writing to socket: " + e.getMessage());
					errorFound[0] = true;
				}
			});

			// Thread for reading messages
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
			writeThread.start();

			// Wait for threads to finish
			try {
				readThread.join();
				writeThread.join();
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted: " + e.getMessage());
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Error closing socket: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println("Error handling socket connection: " + e.getMessage());
		}
	}
}