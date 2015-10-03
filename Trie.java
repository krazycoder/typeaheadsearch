import java.util.Collection;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/*
 * Trie data structure implementation.
 * 
 * This template can be used for any value type T.
 * This class is thread-safe.
 */
public final class Trie<T> {
    private T value;
    private final ConcurrentMap<Integer, Trie<T>> suffixes =
        new ConcurrentSkipListMap<Integer, Trie<T>>();

    public Trie() {}

    public Collection<Trie<T>> getSuffixTries() {
        return suffixes.values();
    }

    public void putSuffixEntry(CharSequence sequence, T newValue) {
        Trie<T> currentTrie = this;
        int position = 0;
        while (position < sequence.length()) {
        	int nextCodepoint = Character.codePointAt(sequence, position);
            currentTrie = currentTrie.appendCodepoint(nextCodepoint);
            position += Character.charCount(nextCodepoint);
        }
        currentTrie.value = newValue;
    }

    public T getValue() {
        return value;
    }

    public Trie<T> getPrefixedTrie(CharSequence prefix) {
    	Trie<T> trie = this;
        int position = 0;
        while (position < prefix.length() && trie != null) {
        	int codepoint = Character.codePointAt(prefix, position);
            trie = trie.getSuffixTrie(codepoint);
            position += Character.charCount(codepoint);
        }
        return trie;
    }
    
    private Trie<T> appendCodepoint(Integer c) {
	    Trie<T> suffix = getSuffixTrie(c);
        if (suffix != null) {
        	return suffix;
        }
        suffix = new Trie<T>();
        Trie<T> existingSuffix = suffixes.putIfAbsent(c, suffix);
        if (existingSuffix != null) {
            return existingSuffix;
        }
        return suffix;
     }

    private Trie<T> getSuffixTrie(Integer nextCodepoint) {
    	return suffixes.get(nextCodepoint);
    }
}