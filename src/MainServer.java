public class MainServer {

	public static void main(String[] args) {
		int PORT = 1234;
		Server server = new Server(PORT);

		server.sendMessage(server.getClient(0), "hello");
	}

}
