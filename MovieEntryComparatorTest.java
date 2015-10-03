import static org.junit.Assert.*;

import org.junit.Test;

public class MovieEntryComparatorTest {
	
	@Test
	public void inequalityComparisonTest() {
		MovieEntry m1 = null;
		MovieEntry m2 = null;
		try {
			m1 = new MovieEntry("2009\tUS\tAAA");
			m2 = new MovieEntry("2008\tUK\tBBB");
		} catch (MovieEntryException ex) {
			// We should not be here.
			fail();
		}
		MovieEntryComparator comparator = new MovieEntryComparator();
		assertEquals(-1, comparator.compare(m1, m2));
		assertEquals(1, comparator.compare(m2, m1));
	}
	
	@Test
	public void equalityComparisonTest() {
		MovieEntry m1 = null;
		MovieEntry m2 = null;
		try {
			m1 = new MovieEntry("2009\tUS\tAAA");
			m2 = new MovieEntry("2008\tUK\tAAA");
		} catch (MovieEntryException ex) {
			// We should not be here.
			fail();
		}
		MovieEntryComparator comparator = new MovieEntryComparator();
		assertEquals(0, comparator.compare(m1, m2));
	}
}