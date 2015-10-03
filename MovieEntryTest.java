import static org.junit.Assert.*;

import org.junit.Test;

public class MovieEntryTest {
	
	@Test
	public void validConstructorTest() {
		MovieEntry m = null;
		String validMovieEntryString = "2009\tUS\tAAA BBB CCC";
		try {
			m = new MovieEntry(validMovieEntryString);
		} catch (MovieEntryException ex) {
			// We should not be here.
			fail();
		}
		assertEquals(validMovieEntryString, m.getRawString());
		assertEquals("2009", m.getYear());
		assertEquals("US", m.getCountryCode());
		assertEquals("AAA BBB CCC", m.getTitle());
	}
	
	@Test
	public void invalidConstructorTest() {
		// Too many tokens.
		try {
			new MovieEntry("2009\tUS\tAAA\tsomething");
		} catch (MovieEntryException ex) {
			// Expected exception.
			assertTrue(ex.toString().contains("Invalid format"));
		}
		// Too few tokens.
		try {
			new MovieEntry("2009\tUS");
		} catch (MovieEntryException ex) {
			// Expected exception.
			assertTrue(ex.toString().contains("Invalid format"));
		}
		// Wrong delimiter.
		try {
			new MovieEntry("2009 US AAA");
		} catch (MovieEntryException ex) {
			// Expected exception.
			assertTrue(ex.toString().contains("Invalid format"));
		}
	}
}