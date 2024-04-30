import java.io.Serializable;
import java.util.List;

/**
 * Placeholder class from the original ADOPT project.
 * 
 * @author Mitchell Gray
 *
 */
public class Hypercube implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Pair<Integer, Integer>> intervals;

	public Hypercube(List<Pair<Integer, Integer>> intervals) {
		this.intervals = intervals;
	}
}