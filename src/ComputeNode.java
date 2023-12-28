import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class ComputeNode {

	final boolean[] errorFound = new boolean[] { false };

	/**
	 * Allows socket to start receiving messages.
	 * 
	 * @param socket The specified socket.
	 */
	protected void startListening(Socket socket, NodeType type) {
		System.out.println("ErrorFound: " + errorFound[0]);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			try {
				String receivedMessage;
				while ((receivedMessage = reader.readLine()) != null && !errorFound[0]) {
					System.out.println(receivedMessage);
				}
				reader.close();
			} catch (IOException e) {
				System.out.println("Error reading from socket: " + e.getMessage() + " " + socket.getInetAddress());
				if (type.equals(NodeType.CLIENT))
					errorFound[0] = true;
			}
			System.out.println("Killed completely");

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
				writer.println(message);
			});
			writeThread.start();

		} catch (IOException e) {
			System.out.println("Error handling socket connection: " + e.getMessage());
			errorFound[0] = true;
		}

		return true;
	}

	protected boolean canSendMessage(Socket socket) {
		return !(socket == null || !socket.isConnected());
	}

}
