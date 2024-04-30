import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Test class for running the Worker
 * 
 * @author Mitchell Gray
 *
 */
public class MainWorker {
	public static void main(String[] args) {
		// Read the IP address of the leader. Stored in file so raw IP isn't stored in
		// repo.
		try (BufferedReader reader = new BufferedReader(new FileReader("ipaddress.txt"))) {
			String IPADDRESS = reader.readLine(); // Reads the first line of the file
			System.out.println("IP found: " + IPADDRESS);
			Worker worker = new Worker(IPADDRESS);
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}
	}
}
