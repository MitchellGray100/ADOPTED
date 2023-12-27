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
		try {
			connectWithIPAddress(ipaddress);
		}
		catch(NetworkingError e) {
			System.err.println("Could not connect to machine: " + ipaddress);
		}


		boolean connected = send(
			ipaddress, 
			"182", 
			MCST.getAttributeOrder(), 
			config.getBudget(), 
			MCST.getNextHyperCube()
		);
		while(connected) {
			if(response) {
				appendOutputFile(response.file, config.getResultPath());
				if(MCST.hasNextHypercube())
					send(ipaddress, "182", attributeOrder, hypercube)
				else
					endConnectionWithIPAddress(ipaddress);
			}
		}
	}

	private boolean send(String IPAddress, String leaderIPAdress, int[] attributeOrder, long budget,
			Hypercube nextHyperCube) {
		// TODO Auto-generated method stub
		return false;
	}

	private void endConnectionWithIPAddress(String ipaddress) {
		// TODO Auto-generated method stub

	}

	private void connectWithIPAddress(String ipaddress) {
		// TODO Auto-generated method stub

	}
}
