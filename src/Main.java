import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		int PORT = 1234;
		Server server = new Server(1234);
		try (BufferedReader reader = new BufferedReader(new FileReader("ipaddress.txt"))) {
			String IPADDRESS = reader.readLine(); // Reads the first line of the file
			System.out.println("IP found: " + IPADDRESS);
			Client client1 = new Client(IPADDRESS, PORT);
			Client client2 = new Client(IPADDRESS, PORT);
			Client client3 = new Client(IPADDRESS, PORT);
			Client client4 = new Client(IPADDRESS, PORT);
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}
		server.sendMessage(server.getClient(0), "hello");
	}

}
