import java.util.ArrayList;

class Leader {
	private ConfigFile config;
	private MonteCarloSearchTree MCST;

	public Leader(String configPath) {
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
			runWithWorker(ipaddress);
		}
	}

	public void runWithWorker(String ipaddress) {
		Client client = new Client(ipaddress);

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

	private boolean send(String IPAddress, String leaderIPAdress, int[] attributeOrder, long budget,
			Hypercube nextHyperCube) {
		// TODO Auto-generated method stub
		return false;
	}
}
