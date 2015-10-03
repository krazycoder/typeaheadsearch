/* 
 * MovieEntry class.
 * 
 * This class holds the information about a movie entry - year, country code and title.
 */
public class MovieEntry {
	private String rawString;
	private String year;
	private String countryCode;
	private String title;
	private String delimiter = "\t";
	
	public MovieEntry(String movieEntryString) throws MovieEntryException {
		rawString = movieEntryString;
		String[] tokens = rawString.split(delimiter);
		if (tokens.length != 3) {
			throw new MovieEntryException("Invalid format for specifying movie entry");
		}
		year = tokens[0];
		countryCode = tokens[1];
		title = tokens[2];
	}
	
	public String getYear() {
		return year;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public String getTitle() {
		return title;
	}		
	
	public String getRawString() {
		return rawString;
	}
}