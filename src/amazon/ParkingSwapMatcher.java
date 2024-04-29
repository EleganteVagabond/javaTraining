package amazon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ParkingSwapMatcher {

    public class ParkingSwapRequest {
        private String name;
        private String fromBuilding;
        private String toBuilding;

        public ParkingSwapRequest(String name, String fromBuilding, String toBuilding) {
            this.name = name;
            this.fromBuilding = fromBuilding;
            this.toBuilding = toBuilding;
        }

        public String getEnqueueString() {
            return this.fromBuilding + ":" + this.toBuilding;
        }

        public String getSearchString() {
            return this.toBuilding + ":" + this.fromBuilding;
        }

        @Override
        public String toString() {
            return "ParkingSwapRequest [ name=" + name + ", fromBuilding=" + fromBuilding + ", toBuilding=" + toBuilding
                    + "]";
        }
    }

    /**
     * Finds swap matches by pairing movers using a FIFO priority algorithm
     *
     * @param requests
     * @return
     */
    private List<String[]> findSwapMatches(List<ParkingSwapRequest> requests) {
        Hashtable<String, Queue<String>> requestsByBuilding = new Hashtable<String, Queue<String>>();
        List<String[]> matches = new ArrayList<String[]>(requests.size() / 2);

        for (ParkingSwapRequest request : requests) {
            if (!makeMatch(requestsByBuilding, matches, request)) {
                addToWaitList(requestsByBuilding, request);
            }
        }
        return matches;
    }

    private boolean makeMatch(Hashtable<String, Queue<String>> requestsByBuilding, List<String[]> matches,
            ParkingSwapRequest request) {
        boolean hasMatch = false;
        String match = getMatch(requestsByBuilding, request);
        if (match != null) {
            matches.add(new String[] { match, request.name });
            hasMatch = true;
        }
        return hasMatch;
    }

    private void addToWaitList(Hashtable<String, Queue<String>> requestsByBuilding, ParkingSwapRequest request) {
        Queue<String> waitList = this.getRequestQueue(request.getEnqueueString(), requestsByBuilding);
        waitList.add(request.name);
    }

    private String getMatch(Hashtable<String, Queue<String>> requestsByBuilding, ParkingSwapRequest request) {
        Queue<String> matchList = this.getRequestQueue(request.getSearchString(), requestsByBuilding);
        String match = matchList.poll();
        return match;
    }

    private Queue<String> getRequestQueue(String requestedSwap, Hashtable<String, Queue<String>> requestsByBuilding) {
        // reverse the order to match the format we put things into the queue with
        Queue<String> requestQueue = requestsByBuilding.get(requestedSwap);
        if (requestQueue == null) {
            requestQueue = new LinkedList<String>();
            requestsByBuilding.put(requestedSwap, requestQueue);
        }
        return requestQueue;
    }

    public static void main(String[] args) {
        String[][] dataSet = { { "drew", "b1", "b2" }, { "drew2", "b1", "b2" }, { "john", "b2", "b1" },
                { "dave", "b2", "b1" }, { "charlene", "b3", "b5" }, { "joseph", "b3", "b4" }, { "jill", "b3", "b5" },
                { "joe", "b5", "b3" }, { "bill", "b1", "b2" }, };
        ParkingSwapMatcher matcher = new ParkingSwapMatcher();

        List<ParkingSwapRequest> requests = new ArrayList<ParkingSwapRequest>(dataSet.length);
        for (String[] data : dataSet) {
            requests.add(matcher.new ParkingSwapRequest(data[0], data[1], data[2]));
        }

        List<String[]> matches = matcher.findSwapMatches(requests);
        for (String[] match : matches) {
            System.out.println(Arrays.toString(match));
        }

    }

}
