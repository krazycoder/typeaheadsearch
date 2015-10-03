import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/* 
 * Main implementation class for Type Ahead Search problem.
 * 
 * This class defines method to handle process_file, query and quit commands.
 */
public final class TypeAheadSearch {
	private static final String welcomeBanner = "Welcome";
	private static final String goodbyeMessage = "Goodbye!";
	private static final String processFileCmd = "process_file";
	private static final String queryCmd = "query";
	private static final String quitCmd = "quit";
	private static final String emptyCmd = "";
	
	/* Logger object for debugging purposes */
	private static final Logger logging = Logger.getLogger("TypeAheadSearchLogger");
	private static final String logFile = "exec.log";
	
	/* Processor thread to process file contents in the background */
	private static Thread processorThread;
	
	/* Queue to track files to be processed */
	private static final Queue<String> queue = new LinkedList<>();
	private static final int maxQueueSize = 100;
	private static final long queueProcessingIntervalMillis = 1000;
	
	/* Hash map of movie title words -> movie entry string */
	private static final HashMap<Integer, MovieEntry> map = new HashMap<>();
	
	/* Trie to lookup prefix matches for query string */
	private static final PrefixTrie trie = new PrefixTrie();
	private static final int maxQueryResultSize = 10; 
	
	/* Validates the input command and arguments */
	private static boolean validateCmd(
			String[] input, int expLength, String expCmd, String expParams) {
		if (input.length != expLength) {
			System.out.println(String.format("Usage: [%s] %s", expCmd, expParams));
			return false;
		}
		return true;
	}
	
	/* Adds the movie entry to HashMap and Trie data structures */
	private static void addMovieEntry(String movieEntryString) {
		logging.info(String.format("Found movie entry %s", movieEntryString));
		MovieEntry movieEntry;
		try {
			movieEntry = new MovieEntry(movieEntryString);
		} catch (MovieEntryException ex) {
			logging.warning(ex.toString());
			return;
		}
		Integer hashKey = movieEntry.getRawString().hashCode();
		logging.info(String.format("Creating an entry for [%s] in the hash map",
				movieEntry.getRawString()));
		map.put(hashKey, movieEntry);
		String[] movieTitleWords = movieEntry.getTitle().split(" ");
		for (String movieTitleWord : movieTitleWords) {
			logging.info(String.format("Adding movie title word [%s] into Trie",
					movieTitleWord));
			trie.addEntry(movieTitleWord, hashKey);
		}
	}
	
	/* Processes the contents of input file containing movie entries */
	private static void processFile(String fileName) {
		logging.info(String.format("Processing file [%s]", fileName));
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String movieEntry;
			while ((movieEntry = br.readLine()) != null) {
				addMovieEntry(movieEntry);
			}
		} catch (IOException ex) {
			logging.warning(ex.toString());
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				logging.warning(ex.toString());
			}
		}
	}
	
	/* Background thread to read files from the Queue and process them */
	private static void processQueuedRequests() {
		logging.info("Started processor operation in the background");
		boolean timeToQuit = false;
		while (!timeToQuit) {
			try {
				Thread.sleep(queueProcessingIntervalMillis);
				if (queue.isEmpty()) {
					logging.fine("No new files to process");
					continue;
				}
				processFile(queue.remove());
			} catch (InterruptedException ex) {
				logging.info("Got interrupt message; time to quit.");
				timeToQuit = true;
			}
		}
	}
	
	/* Request processing for process_file command */
	public static void newProcessFileRequest(String fileName) throws MovieEntryException {
		if (queue.size() >= maxQueueSize) {
			throw new MovieEntryException(
					"Too many process_file requests queued up; dropping new request");
		}
		logging.info(String.format("Queueing file [%s] for processing", fileName));
		queue.add(fileName);
	}
	
	/* Request processing for query command */
	public static void newQueryRequest(String prefix) throws MovieEntryException {
		List<MovieEntry> movieEntries = new ArrayList<>();
		logging.info(String.format(
				"Querying for movies whose titles contain words starting with %s", prefix));
		for (Integer hashKey : trie.findMatchingValues(prefix)) {
			MovieEntry movieEntry = map.get(hashKey);
			if (movieEntry == null) {
				logging.warning(String.format(
						"Unable to find corresponding movie entry for hash key [%s]", hashKey));
			} else {
				movieEntries.add(movieEntry);
			}
		}
		if (movieEntries.isEmpty()) {
			System.out.println("No matching entries found");
			return;
		}
		Collections.sort(movieEntries, new MovieEntryComparator());
		int resultCount = 0;
		for (MovieEntry movieEntry : movieEntries) {
			if (resultCount >= maxQueryResultSize) {
				logging.info("Trimming result set to match max page size; more results were found");
				return;
			}
			System.out.println(movieEntry.getRawString());
			resultCount++;
		}
	}
	
	/* Initiates the state of the program */
	public static void initState() {
		System.out.println(welcomeBanner);
		try {
			FileHandler fileHandler = new FileHandler(logFile);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
			logging.addHandler(fileHandler);
			logging.setUseParentHandlers(false);
		} catch (SecurityException|IOException ex) {
			logging.warning(ex.toString());
		}
		Runnable r = new Runnable() {
			public void run() {
				processQueuedRequests();
			}
		};
		processorThread = new Thread(r);
		processorThread.start();
	}
	
	/* Runs the command line prompt for user input and calls the appropriate method */
	public static void runPrompt() {
		Scanner inputReader = new Scanner(System.in);
		boolean timeToQuit = false;
		while (!timeToQuit) {
			System.out.print("$ ");
			String[] input = inputReader.nextLine().split("\\s+");
			try {
				switch (input[0].toLowerCase()) {
				case processFileCmd:
					if (!validateCmd(input, 2, processFileCmd, "<filename>")) break;
					newProcessFileRequest(input[1]);
					break;
				case queryCmd:
					if (!validateCmd(input, 2, queryCmd, "<text>")) break;
					newQueryRequest(input[1]);
					break;
				case quitCmd:
					if (!validateCmd(input, 1, quitCmd, "")) break;
					timeToQuit = true;
					break;
				case emptyCmd:
					break;
				default:
					System.out.println(String.format(
							"Invalid command; choose from [%s, %s, %s]",
							processFileCmd, queryCmd, quitCmd));
				}
			} catch (MovieEntryException ex) {
				logging.severe(ex.toString());
				System.out.println(String.format(
						"Internal error occurred; see %s for details", logFile));
			}	
		}
		inputReader.close();
	}
	
	/* Cleans up the state before exiting */
	public static void cleanState() {
		processorThread.interrupt();
		System.out.println(goodbyeMessage);
	}
	
	/* Main entry point */
	public static void main(String[] args) {
		initState();
		runPrompt();
		cleanState();
	}
}
