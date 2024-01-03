import java.nio.file.Path;
import java.util.ArrayList;

public class ConfigFile {
	private static ConfigFile instance;

	private ConfigFile(String configPath) {
	}

	public static ConfigFile getInstance(String configPath) {
		if (instance == null) {
			instance = new ConfigFile(configPath);
		}
		return instance;
	}

	public ArrayList<String> getQueries() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getIPaddresses() {
		// TODO Auto-generated method stub
		return new String[] { "1" };
	}

	public long getBudget() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Path getResultPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
