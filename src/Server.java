import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static final int PORT = 1234;
	private static final String IPADDRESS = "10.0.0.12";
	private static final Object lock = new Object();

	public static void main(String[] args) {
		startServer(PORT);
	}

	private static void startServer(int port) {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server is listening on port " + port);

			while (true) {
				try {
					Socket socket = serverSocket.accept();
					System.out.println("Client connected: " + socket.getInetAddress());
					Thread clientThread = new Thread(() -> handleConnection(socket));
					clientThread.start();
				} catch (IOException e) {
					System.out.println("Server exception: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println("Could not listen on port " + port);
		}
	}

	private static void handleConnection(Socket socket) {
		final boolean[] errorFound = { false };
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			// Thread for sending messages
			Thread writeThread = new Thread(() -> {
				try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
					String userInput;
					while (true && !errorFound[0]) {
						writer.println("test server" + socket.getInetAddress());
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
						synchronized (lock) {
							System.out.println(receivedMessage);
//							try (PrintWriter out = new PrintWriter(new FileWriter("output.txt", true))) {
//								out.println(receivedMessage);
//							} catch (IOException e) {
//								System.out.println("Error writing to file: " + e.getMessage());
//							}
						}
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
