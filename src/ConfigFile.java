import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Parses the configuration file.
 * 
 * @author Mitchell Gray
 *
 */
public class ConfigFile {
	/**
	 * Singleton instance of ConfigFile.
	 */
	private static ConfigFile instance;

	/**
	 * Constructor for ConfigFile
	 * 
	 * @param configPath Path of the Config File location.
	 */
	private ConfigFile(String configPath) {
	}

	/**
	 * Getter for the ConfigFile Singleton.
	 * 
	 * @param configPath The path of the Config File
	 * @return The ConfigFile Singleton object
	 */
	public static ConfigFile getInstance(String configPath) {
		if (instance == null) {
			instance = new ConfigFile(configPath);
		}
		return instance;
	}

	/**
	 * Parse the queries stored in the ConfigFile.
	 * 
	 * @return List of queries stored as strings.
	 */
	public ArrayList<String> getQueries() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Parse the IPAdresses of the Workers in the ConfigFile.
	 * 
	 * @return
	 */
	public String[] getIPaddresses() {
		// TODO Auto-generated method stub
		return new String[] { "1" };
	}

	/**
	 * Parse the Budget field from the ConfigFile. The budget is the time quantum
	 * for running ADOPT on the workers.
	 * 
	 * @return The budget
	 */
	public long getBudget() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Parse the Result field from the ConfigFile. The result field stores the path
	 * where results should be stored to.
	 * 
	 * @return The Path of the result field.
	 */
	public Path getResultPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
