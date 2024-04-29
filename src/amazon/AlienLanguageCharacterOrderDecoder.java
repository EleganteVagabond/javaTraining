package amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * There is a new alien language which uses the latin alphabet.
 * However, the order among letters are unknown to you.
 * You receive a list of non-empty words from the dictionary, where words are sorted lexicographically by the rules of this new language.
 * Derive the order of letters in this language.
        Example: Given the following words in dictionary, [ "wrt", "wrf", "er", "ett", "rftt" ] The correct order is: "werft".
        Linked List
        w -> r -> t
        w -> r -> f -> t
        w -> e -> r -> f -> t
        w -> e -> r -> t (f)?
        w -> e -> r -> f -> t

        ae
        be
        ac
        abc
        de
        bcd
        a -> e
        a -> b -> e
        a -> c -> b -> e
        a -> b -> c -> e
        a -> b -> c -> d -> e

 */

public class AlienLanguageCharacterOrderDecoder {
    private static final String[][] testCases = {
        {"wrt", "wrf", "er", "ett", "rftt", "we"}, // werft
        {"e", "d", "c", "b", "ae", "be", "", "a", "e", "acd", "abe", "abd", "bcd", "cde"}, // abcde
        {"ab", "ba"} // CYCLE
    };

    interface Callable {
        void call(String[] testCase);
    }

    public static void main(String[] args) {
        AlienLanguageCharacterOrderDecoder alienLanguageCharacterOrderDecoder = new AlienLanguageCharacterOrderDecoder();

        for (String[] orderedStrings : testCases) {
            String linkedListSolution = alienLanguageCharacterOrderDecoder.decodeLL(orderedStrings).toString();
            System.out.println(linkedListSolution);

            String topographicalSolution = alienLanguageCharacterOrderDecoder.decodeTopSort(orderedStrings).toString();
            System.out.println(topographicalSolution);

        }
        // let's see how fast they are
        for (String[] orderedStrings : testCases) {
            double llAverage = alienLanguageCharacterOrderDecoder.timeAndCapture(orderedStrings, new Callable() {
                public void call(String[] testCase) {
                    alienLanguageCharacterOrderDecoder.decodeLL(testCase);
                }
            });
            System.out.println("Linked list average: " + llAverage);
            double tsAverage = alienLanguageCharacterOrderDecoder.timeAndCapture(orderedStrings, new Callable() {
                public void call(String[] testCase) {
                    alienLanguageCharacterOrderDecoder.decodeTopSort(orderedStrings);
                }
            });
            System.out.println("Topological average: " + tsAverage);
        }
    }

    public double timeAndCapture(String[] testCase, Callable testMethod) {
        long start = System.currentTimeMillis();
        long timesThrough = 10000000;
        for (int i = 0; i < timesThrough; i++) {
            testMethod.call(testCase);
        }
        long end = System.currentTimeMillis();
        return (end-start);
    }

    private List<Character> decodeLL(String[] orderedStrings) {
        LinkedList<Character> alienLanguageCharacterOrder = new LinkedList<Character>();
        for (String orderedString : orderedStrings) {
            if (orderedString.length() == 0) {
                continue;
            }
            char[] orderInfo = orderedString.toCharArray();
            int prevIx = -1;
            boolean[] seen = new boolean[26];
            for (int cIx = 0; cIx < orderInfo.length; cIx++) {
                // 3 values, prev, curr, next
                // if prev != null, make sure curr is after prev but before next (if it exists)
                // if prev == null && next != null, make sure curr is before next
                char currChar = orderInfo[cIx];
                if (seen[currChar-'a']) {
                    // System.out.println("Already saw " + currChar + ", skipping");
                    continue;
                }
                int currIx = alienLanguageCharacterOrder.indexOf(currChar);
                // case 1: if we don't know where it goes, put it at the beginning for now until we get more information
                if (currIx == -1 && prevIx == -1) {
                    alienLanguageCharacterOrder.addFirst(currChar);
                } else if (currIx < prevIx) { //prev ix exists and this current char is in the list behind it
                    int newIx = prevIx+1;
                    if (newIx > alienLanguageCharacterOrder.size()) {
                        alienLanguageCharacterOrder.addLast(currChar);
                    }else {
                        alienLanguageCharacterOrder.add(newIx, currChar);
                    }
                    // remove it from where it was
                    if (currIx != -1) {
                        alienLanguageCharacterOrder.remove(currIx);
                    }
                    currIx = newIx;
                }
                seen[currChar-'a'] = true;
                // case 2
                prevIx = currIx;
            }
        }
        return new ArrayList<Character>(alienLanguageCharacterOrder);
    }

    class DirectedGraphNode {
        public Character letter;
        public Set<DirectedGraphNode> neighbors;

        public DirectedGraphNode(char letter) {
            this.letter = Character.valueOf(letter);
            this.neighbors = new HashSet<DirectedGraphNode>(5);
        }

        public boolean addEdge(DirectedGraphNode neighbor) {
            return neighbors.add(neighbor);
        }

        @Override
        public int hashCode() {
            return letter.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return letter.equals(obj);
        }

        @Override
        public String toString() {
            return neighbors.stream().map(neighbor -> neighbor.letter.toString()).reduce("", String::concat).toString();
        }

    }

    private List<Character> decodeTopSort(String[] orderedStrings) {
        Map<Character, DirectedGraphNode> graph = new HashMap<Character, DirectedGraphNode>(orderedStrings.length);
        Map<Character, Integer> indegree = new HashMap<Character, Integer>(orderedStrings.length);
        buildOrderedGraph(orderedStrings, graph, indegree);
        // System.out.println("graph:" + graph);
        // System.out.println("incoming verteces count: " + indegree);
        List<Character> ret = new ArrayList<Character>(indegree.size());
        // process the graph
        boolean cycle = true;
        do {
            cycle = true;
            Iterator<Entry<Character, Integer>> keyIter = indegree.entrySet().iterator();
            while (keyIter.hasNext()) {
                Entry<Character, Integer> entry = keyIter.next();
                char key = entry.getKey();
                int incomingEdgeCount = entry.getValue();
                if (incomingEdgeCount == 0) {
                    ret.add(key);
                    for (DirectedGraphNode neighbor : graph.get(key).neighbors) {
                        // decrement edge count
                        indegree.put(neighbor.letter, indegree.get(neighbor.letter)-1);
                    }
                    keyIter.remove();
                    cycle = false;
                }
            }
        } while (indegree.size() != 0 && !cycle);

        if (!indegree.isEmpty() && cycle) {
            // System.err.println("Cycle detected in " + Arrays.toString(orderedStrings));
            return new ArrayList<>();

        }
        return ret;
    }

    private void buildOrderedGraph(String[] orderedStrings, Map<Character, DirectedGraphNode> graph,
            Map<Character, Integer> indegree) {
        //  {"wrt", "wrf", "er", "ett", "rftt", "we"}, // werft
        for (String orderedString : orderedStrings) {
            if (orderedString.length() == 0) {
                continue;
            }
            char[] orderedChars = orderedString.toCharArray();
            char prevChar = orderedChars[0];
            DirectedGraphNode prevNode = graph.get(prevChar);
            if (prevNode == null) {
                prevNode = new DirectedGraphNode(prevChar);
                graph.put(orderedChars[0], prevNode);
            }
            if (!indegree.containsKey(prevChar)) {
                indegree.put(prevChar, 0);
            }
            for (int cIx = 1; cIx < orderedChars.length; cIx++) {
                if (orderedChars[cIx] == prevNode.letter) continue;
                DirectedGraphNode current = graph.get(orderedChars[cIx]);
                if (current == null) {
                    current = new DirectedGraphNode(orderedChars[cIx]);
                    graph.put(orderedChars[cIx], current);
                }
                if (prevNode.addEdge(current) ) {
                    indegree.put(current.letter,indegree.getOrDefault(current, 0)+1);
                }
                prevNode = current;
            }
        }
    }
}
