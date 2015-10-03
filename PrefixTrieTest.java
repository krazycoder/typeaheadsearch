import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class PrefixTrieTest {
	
	@Test
	public void addNewEntryTest() {
		PrefixTrie trie = new PrefixTrie();
		assertNull(trie.getValue("abc"));
		trie.addEntry("abc", 123);
		Set<Integer> expectedSet = new HashSet<>();
		expectedSet.add(123);
		assertEquals(expectedSet, trie.getValue("abc"));
	}
	
	@Test
	public void addExistingEntryTest() {
		PrefixTrie trie = new PrefixTrie();
		trie.addEntry("abc", 123);
		trie.addEntry("abc", 456);
		Set<Integer> expectedSet = new HashSet<>();
		expectedSet.add(123);
		expectedSet.add(456);
		assertEquals(expectedSet, trie.getValue("abc"));
	}
	
	@Test
	public void hasKeyTest() {
		PrefixTrie trie = new PrefixTrie();
		assertFalse(trie.hasKey("abc"));
		trie.addEntry("abc", 123);
		assertTrue(trie.hasKey("abc"));
	}
	
	@Test
	public void getValueTest() {
		PrefixTrie trie = new PrefixTrie();
		assertNull(trie.getValue("abc"));
		trie.addEntry("abc", 123);
		Set<Integer> expectedSet = new HashSet<>();
		expectedSet.add(123);
		assertEquals(expectedSet, trie.getValue("abc"));
		// Non-existent keys will yield null value.
		assertNull(trie.getValue("foo"));
	}
	
	@Test
	public void findMatchingValuesTest() {
		PrefixTrie trie = new PrefixTrie();
		trie.addEntry("a", 123);
		trie.addEntry("abcd", 456);
		trie.addEntry("abcxyz", 789);
		trie.addEntry("xyz", 0);
		Set<Integer> expectedSet = new HashSet<>();
		// Match all values with prefix "a".
		expectedSet.add(123);
		expectedSet.add(456);
		expectedSet.add(789);
		assertEquals(expectedSet, trie.findMatchingValues("a"));
		expectedSet.clear();
		// Match all values with prefix "ab".
		expectedSet.add(456);
		expectedSet.add(789);
		assertEquals(expectedSet, trie.findMatchingValues("ab"));
		// All values under node "abc" should also be the same.
		assertEquals(expectedSet, trie.findMatchingValues("abc"));
		expectedSet.clear();
		// Match only the value associated with prefix "abcd".
		expectedSet.add(456);
		assertEquals(expectedSet, trie.findMatchingValues("abcd"));
		expectedSet.clear();
		// Match only the value associated with prefix starting with "xyz".
		expectedSet.add(0);
		assertEquals(expectedSet, trie.findMatchingValues("xyz"));
	}
}