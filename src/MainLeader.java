/**
 * Test Runner for running the leader
 * 
 * @author Mitchell Gray
 *
 */
public class MainLeader {
	public static void main(String[] args) {
		// Read the config file to start the leader
		Leader leader = new Leader("ConfigPath");
		// Send a message to Client0
		leader.send(leader.getClient(0), new int[] { 1, 2, 3 }, 1000, new Hypercube(null));
	}
}
