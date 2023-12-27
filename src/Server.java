public class Server {

	private static final int PORT = 1234;

	public static void main(String[] args) {
		CommunicationManager.startServer(PORT);
	}
}
