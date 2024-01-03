import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class ComputeNode {

	public final static int PORT = 1234;
	final boolean[] errorFound = new boolean[] { false };

	/**
	 * Allows socket to start receiving messages.
	 * 
	 * @param socket The specified socket.
	 */
	abstract void startListening(Socket socket);

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
