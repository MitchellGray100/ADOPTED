import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class ComputeNode {

	final boolean[] errorFound = { false };

	/**
	 * Allows socket to start receiving messages.
	 * 
	 * @param socket The specified socket.
	 */
	protected void startListening(Socket socket) {
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

	/**
	 * Sends message between the server and client.
	 * 
	 * @param socket  The socket connecting the client and server.
	 * @param message The message to send over the socket.
	 * @param type    Whether or not the compute node is a server or client.
	 * @return whether or not the message sent successfully
	 */
	protected boolean sendMessage(Socket socket, String message) {
		if (socket == null || !socket.isConnected())
			return false;

		try {
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			Thread writeThread = new Thread(() -> {
				try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
					writer.println(message);
					writer.close();
					consoleReader.close();
				} catch (IOException e) {
					System.out.println("Error writing to socket: " + e.getMessage());
					errorFound[0] = true;
				}
			});
			writeThread.start();

		} catch (IOException e) {
			System.out.println("Error handling socket connection: " + e.getMessage());
		}

		return true;
	}

	protected boolean canSendMessage(Socket socket) {
		return !(socket == null || !socket.isConnected());
	}

}
