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
			System.out.println("Made client");
//			Client client2 = new Client(IPADDRESS, PORT);
//			Client client3 = new Client(IPADDRESS, PORT);
//			Client client4 = new Client(IPADDRESS, PORT);
//			while (!client1.canSendMessage(client1.getServerSocket())) {
//				System.out.println("Socket not open yet");
//			}
			System.out.println("Can send message");
			System.out.println(client1.sendMessage(client1.getServerSocket(), "hello"));
			System.out.println("First message sent");
			client1.sendMessage(client1.getServerSocket(), "hello2");
			System.out.println("Second message sent");
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}

		System.out.println("Died");

	}

}
