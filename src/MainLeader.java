
public class MainLeader {
	public static void main(String[] args) {
		Leader leader = new Leader("ConfigPath");
		leader.sendTest(leader.getClient(0), "testing");
	}
}
