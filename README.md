Type Ahead Search
========

#### Overview
This Java source code implements a prompt to allow processing of new movie
entries and allow querying by words present in movie titles.

#### How to run
Make sure that JRE and JDK are installed on your machine.

$ javac TypeAheadSearch.java \
        TypeAheadSearchException.java \
        Trie.java \
        PrefixTrie.java \
        MovieEntry.java \
        MovieEntryException.java \
        MovieEntryComparator.java
        
$ java TypeAheadSearch

This should compile and run the main executable which will provide a prompt.
The list of allowed commands and arguments are:

* process-file <filename>
The filename should be the complete path to the file located on the local
file-system.

* query <prefix>
There should be no spaces or regular expressions in the prefix to be matched.
It should be an exact matching prefix for any word present in the movie titles.

* quit
Use this to exit out of the program.

#### Design
This assignment consists of two main components:
a) to be able to process the file contents in the background.
b) to be able to query by movie titles.

The design intends to provide the minimum query latency while searching for
movies for optimal user experience. Hence, majority of the processing and
manipulation of data structures happens in the background. The query command
serves a simple lookup for matching prefixes and returns them in "sorted"
order.

We use a Queue to keep track of files to be processed and the actual processing
is done in the background by a spawned thread. Each movie entry is read from the
file and we construct a Trie structure with the node keys being the words present
in the movie title, and the node values being a set of HashKeys.

Each movie entry is indexed in a HashMap, with the key being the HashCode of the
entire string (which is the HashKey), and the value being the entire string.
This allows us to reduce storage complexity by not duplicating the whole movie
entry string for each word present in its title. The HashKey is stored
corresponding to the word tokens in the Trie, and later used to lookup the
entire movie entry string from the HashMap (at query time).

For querying prefixes, we just get all matching entries from the Trie, use the
set of HashKeys associated with the matching nodes, and lookup their
corresponding entry strings from the HashMap. We limit the number of results
to a certain max size (without any pagination options).

#### Debugging
The program maintains a log file in the directory from which it's executed. It
contains useful information about the current processing state and warnings
about unexpected encounters in the program.
