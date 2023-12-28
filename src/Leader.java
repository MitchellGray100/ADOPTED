import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

class Leader {
	private ConfigFile config;
	private MonteCarloSearchTree MCST;
	private Server server;

	public Leader(String configPath) {
		this.server = new Server(1234);
		this.config = new ConfigFile(configPath);
		MCST = new MonteCarloSearchTree();
	}

	public void runQueries() {
		ArrayList<String> queries = config.getQueries();
		for (String query : queries) {
			runQuery(query);
		}
	}

	public void runQuery(String query) {
		for (String ipaddress : config.getIPaddresses()) {
			try {
				runWithWorker(InetAddress.getByName(ipaddress));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void runWithWorker(InetAddress ipaddress) {
		send(ipaddress, "182", MCST.getAttributeOrder(), config.getBudget(), MCST.getNextHyperCube());
		// Wait for response
//		while(connected) {
//			if(response) {
//				appendOutputFile(response.file, config.getResultPath());
//				if(MCST.hasNextHypercube())
//					send(ipaddress, "182", attributeOrder, hypercube)
//				else
//					endConnectionWithIPAddress(ipaddress);
//			}
//		}
	}

	private boolean send(InetAddress IPAddress, String leaderIPAdress, int[] attributeOrder, long budget,
			Hypercube nextHyperCube) {
		String serializedData = serializeData(IPAddress, attributeOrder, budget, nextHyperCube);
		return server.sendMessage(server.getSocket(IPAddress), serializedData);
	}

	private String serializeData(InetAddress IPAddress, int[] attributeOrder, long budget, Hypercube nextHyperCube) {
		return null;// TODO
	}
}
