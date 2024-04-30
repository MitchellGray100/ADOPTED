import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * A node within the architecture of ADOPTED. This can be a Worker or Leader.
 * 
 * @author Mitchell Gray
 *
 */
public abstract class ComputeNode {

	/**
	 * The Port for Workers to listen to. The Port for Leaders to broadcast to.
	 */
	public final static int PORT = 1234;

	/**
	 * Whether or not an error occurred at any point while a node was running.
	 */
	final boolean[] errorFound = new boolean[] { false };

	/**
	 * Allows socket to start receiving messages.
	 * 
	 * @param socket The specified socket to receive messages on.
	 */
	abstract void startListening(Socket socket);

	/**
	 * Sends message between the server and client.
	 * 
	 * @param socket         The socket connecting the client and server.
	 * @param serializedData The message to send over the socket.
	 * @param type           Whether or not the compute node is a server or client.
	 * @return whether or not the message sent successfully
	 */
	protected boolean sendMessage(Socket socket, byte[] serializedData) {
		// If something is wrong with the socket
		if (socket == null || !socket.isConnected())
			return false;

		try {
			OutputStream out = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);

			// Asynchronously write data to the output stream.
			Thread writeThread = new Thread(() -> {
				try {
					dos.write(serializedData);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			writeThread.start();

		} catch (IOException e) {
			// Socket error, set errorFound to true
			System.out.println("Error handling socket connection: " + e.getMessage());
			errorFound[0] = true;
			return false;
		}

		return true;
	}

	/**
	 * Check that the message should be able to be sent without failure.
	 * 
	 * @param socket The socket to check if it's working
	 * @return Whether or not the socket is working
	 */
	protected boolean canSendMessage(Socket socket) {
		return !(socket == null || !socket.isConnected());
	}

	/**
	 * Deserializes the data sent by the worker.
	 * 
	 * @return Object of the client message
	 */
	protected String deserializeClientMessage() {
		return null;
	}

}
