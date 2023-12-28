public class Server implements ComputeNode {

	private final int PORT;

	public Server(int port) {
		PORT = port;
		CommunicationManager.startServer(PORT);
	}
}
