import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainClient {

	public static void main(String[] args) throws InterruptedException {
		int PORT = 1234;
		try (BufferedReader reader = new BufferedReader(new FileReader("ipaddress.txt"))) {
			String IPADDRESS = reader.readLine(); // Reads the first line of the file
			System.out.println("IP found: " + IPADDRESS);
			Client client1 = new Client(IPADDRESS, PORT);
			Client client2 = new Client(IPADDRESS, PORT);
			client2.sendMessage(client2.getServerSocket(), "hello client2");
			client1.sendMessage(client1.getServerSocket(), "hello client1");
			client1.sendMessage(client1.getServerSocket(), "hello client1 pt 2");
			client2.sendMessage(client2.getServerSocket(), "hello client2 pt 2");
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}

		System.out.println("Died");

	}

}
