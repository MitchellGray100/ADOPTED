
public class MainLeader {
	public static void main(String[] args) {
		Leader leader = new Leader("ConfigPath");
		leader.send(leader.getClient(0), new int[] { 1, 2, 3 }, 1000, new Hypercube(null));
	}
}
