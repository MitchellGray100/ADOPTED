public class Server {

	private final int PORT;

	public Server(int port) {
		PORT = port;
		CommunicationManager.startServer(PORT);
	}
}
