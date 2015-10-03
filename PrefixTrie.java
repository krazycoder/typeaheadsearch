import java.util.HashSet;
import java.util.Set;

/*
 * PrefixTrie class for adding and retrieving values based on prefixes.
 */
public class PrefixTrie {

  private final Trie<Set<Integer>> trie = new Trie<>();

  public void addEntry(String key, Integer value) {
	  Set<Integer> valueSet = new HashSet<>();
	  String myKey = key.toLowerCase();
	  Set<Integer> oldValue = getValue(myKey);
	  if (oldValue != null) {
		  valueSet.addAll(oldValue);
	  }
	  valueSet.add(value);
	  trie.putSuffixEntry(myKey, valueSet);
  }

  public boolean hasKey(String key) {
	  String myKey = key.toLowerCase();
	  Trie<Set<Integer>> root = trie.getPrefixedTrie(myKey);
	  return root != null && root.getValue() != null;
  }

  public Set<Integer> getValue(String key) {
	  String myKey = key.toLowerCase();
	  Trie<Set<Integer>> root = trie.getPrefixedTrie(myKey);
	  if (root != null) {
		  return root.getValue();
      }
      return null;
  }

  public Set<Integer> findMatchingValues(String prefix) {
	  String myKey = prefix.toLowerCase();
	  Set<Integer> matchingEntries = new HashSet<>();
      Trie<Set<Integer>> root = trie.getPrefixedTrie(myKey);
      findMatchingValuesInternal(root, matchingEntries);
      return matchingEntries;
  }

  private void findMatchingValuesInternal(Trie<Set<Integer>> root,
		  Set<Integer> matchingEntries) {
      if (root != null) {
        Set<Integer> value = root.getValue();
        if (value != null) {
          matchingEntries.addAll(value);
        }
        for (Trie<Set<Integer>> suffixTrie : root.getSuffixTries()) {
        	findMatchingValuesInternal(suffixTrie, matchingEntries);
        }
      }
  }
}