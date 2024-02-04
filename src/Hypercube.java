import java.io.Serializable;
import java.util.List;

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