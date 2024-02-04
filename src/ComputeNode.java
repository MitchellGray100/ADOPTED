import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
	 * @param socket         The socket connecting the client and server.
	 * @param serializedData The message to send over the socket.
	 * @param type           Whether or not the compute node is a server or client.
	 * @return whether or not the message sent successfully
	 */
	protected boolean sendMessage(Socket socket, byte[] serializedData) {
		if (socket == null || !socket.isConnected())
			return false;

		try {
			OutputStream out = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);

			Thread writeThread = new Thread(() -> {
				try {
					dos.write(serializedData);
				} catch (IOException e) {
					e.printStackTrace();
				}
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

	protected String deserializeClientMessage() {
		return null;
	}

}
