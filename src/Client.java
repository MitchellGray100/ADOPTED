import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Client implements ComputeNode {

	private static final int PORT = 1234;

	public Client(String ipaddress) {
		Thread client = new Thread(() -> CommunicationManager.startClient(ipaddress, PORT, "Client"));
		client.start();
	}

	public static void main(String[] args) {
		try (BufferedReader reader = new BufferedReader(new FileReader("ipaddress.txt"))) {
			String IPADDRESS = reader.readLine(); // Reads the first line of the file
			System.out.println("IP found: " + IPADDRESS);
			Thread client1 = new Thread(() -> CommunicationManager.startClient(IPADDRESS, PORT, "Client 1"));
			client1.start();
			Thread client2 = new Thread(() -> CommunicationManager.startClient(IPADDRESS, PORT, "Client 2"));
			client2.start();
			Thread client3 = new Thread(() -> CommunicationManager.startClient(IPADDRESS, PORT, "Client 3"));
			client3.start();
			Thread client4 = new Thread(() -> CommunicationManager.startClient(IPADDRESS, PORT, "Client 4"));
			client4.start();
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}
	}

	public static void main2(String[] args) {
		try (BufferedReader reader = new BufferedReader(new FileReader("ipaddress.txt"))) {
			String IPADDRESS = reader.readLine(); // Reads the first line of the file
			System.out.println("IP found: " + IPADDRESS);
			Thread client5 = new Thread(() -> CommunicationManager.startClient(IPADDRESS, PORT, "Client 5"));
			client5.start();
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}
	}
}