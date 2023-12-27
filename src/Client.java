import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	private static final int PORT = 1234;
	private volatile static boolean errorFound = false;

	public static void main(String[] args) {
		try (BufferedReader reader = new BufferedReader(new FileReader("ipaddress.txt"))) {
			String IPADDRESS = reader.readLine(); // Reads the first line of the file
			System.out.println("IP found: " + IPADDRESS);
			Thread client1 = new Thread(() -> startClient(IPADDRESS, PORT, "Client 1"));
			client1.start();
			Thread client2 = new Thread(() -> startClient(IPADDRESS, PORT, "Client 2"));
			client2.start();
			Thread client3 = new Thread(() -> startClient(IPADDRESS, PORT, "Client 3"));
			client3.start();
			Thread client4 = new Thread(() -> startClient(IPADDRESS, PORT, "Client 4"));
			client4.start();
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}
	}

	private static void startClient(String hostname, int port) {
		while (true) {
			System.out.println("Trying to connect to Server");
			try (Socket socket = new Socket(hostname, port)) {
				System.out.println("Connected to server at " + hostname + ":" + port);
				handleConnection(socket);
				break;
			} catch (IOException e) {
				System.out.println("Failed to connect");
			}
		}
	}

	private static void startClient(String hostname, int port, String message) {
		while (true) {
			System.out.println("Trying to connect to Server");
			try (Socket socket = new Socket(hostname, port)) {
				System.out.println("Connected to server at " + hostname + ":" + port);
				handleConnection(socket, message);
				break;
			} catch (IOException e) {
				System.out.println("Failed to connect");
			}
		}
	}

	private static void handleConnection(Socket socket) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			// Thread for sending messages
			Thread writeThread = new Thread(() -> {
				try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
					while (true && !errorFound) {
						writer.println("test client");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					consoleReader.close();
					writer.close();
				} catch (IOException e) {
					System.out.println("Error writing to socket: " + e.getMessage());
					errorFound = true;
				}
			});

			// Thread for reading messages
			Thread readThread = new Thread(() -> {
				try {
					String receivedMessage;
					while ((receivedMessage = reader.readLine()) != null && !errorFound) {
						System.out.println("Received: " + receivedMessage);
					}
					reader.close();
				} catch (IOException e) {
					System.out.println("Error reading from socket: " + e.getMessage());
					errorFound = true;
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
			}
		} catch (IOException e) {
			System.out.println("Error handling socket connection: " + e.getMessage());
		}
	}

	private static void handleConnection(Socket socket, String message) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			// Thread for sending messages
			Thread writeThread = new Thread(() -> {
				try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
					while (true && !errorFound) {
						writer.println(message);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					consoleReader.close();
					writer.close();
				} catch (IOException e) {
					System.out.println("Error writing to socket: " + e.getMessage());
					errorFound = true;
				}
			});

			// Thread for reading messages
			Thread readThread = new Thread(() -> {
				try {
					String receivedMessage;
					while ((receivedMessage = reader.readLine()) != null && !errorFound) {
						System.out.println("Received: " + receivedMessage);
					}
					reader.close();
				} catch (IOException e) {
					System.out.println("Error reading from socket: " + e.getMessage());
					errorFound = true;
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
			}
		} catch (IOException e) {
			System.out.println("Error handling socket connection: " + e.getMessage());
		}
	}
}