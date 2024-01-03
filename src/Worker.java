import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

class Worker {
	File output;
	Client client;

	public Worker(String leaderIPAddress, int[] attributeOrder, String budget, Hypercube hypercube, Path outputPath) {
		try (BufferedReader reader = new BufferedReader(new FileReader("ipaddress.txt"))) {
			String IPADDRESS = reader.readLine(); // Reads the first line of the file
			client = new Client(IPADDRESS, ComputeNode.PORT);
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			System.exit(1);
		}
	}

	public void processHypercube() {
		// *preexisting LFTJ method*
		// output results to output;
	}

	public void respond() {
		client.sendMessage(client.getServerSocket(), null);// Todo replace null with LFTJ results
	}

}
