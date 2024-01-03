import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainWorker {
	public static void main(String[] args) {
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
