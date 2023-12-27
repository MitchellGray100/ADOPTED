public class Server {

	private static final int PORT = 1234;
	private static final Object lock = new Object();

	public static void main(String[] args) {
		CommunicationManager.startServer(PORT);
	}
}
