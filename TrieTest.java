import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

public class TrieTest {
	
	@Test
	public void singleTrieTest() {
		Trie<String> trie = new Trie<>();
		trie.putSuffixEntry("abc", "foo");
		Trie<String> trieA = trie.getPrefixedTrie("a");
		Collection<Trie<String>> trieACollection = trieA.getSuffixTries();
		assertEquals(1, trieACollection.size());
	}
	
	@Test
	public void multipleTrieTest() {
		Trie<String> trie = new Trie<>();
		trie.putSuffixEntry("ab", "foo");
		trie.putSuffixEntry("ac", "bar");
		Trie<String> trieA = trie.getPrefixedTrie("a");
		Collection<Trie<String>> trieACollection = trieA.getSuffixTries();
		assertEquals(2, trieACollection.size());
	}
}