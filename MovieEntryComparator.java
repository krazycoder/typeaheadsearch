import java.util.Comparator;

/*
 * Comparator class for objects of type MovieEntry 
 */
public class MovieEntryComparator implements Comparator<MovieEntry> {		
	@Override
	public int compare(MovieEntry o1, MovieEntry o2) {
		String title1 = o1.getTitle();
		String title2 = o2.getTitle();
		return title1.compareToIgnoreCase(title2);
	}
}