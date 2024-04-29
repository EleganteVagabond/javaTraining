package hackerrank;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CountSort4 {

    public static void countSort(List<List<String>> arr) {
        // Write your code here

        HashMap<Integer, StringBuilder> ret = new HashMap<Integer, StringBuilder>(20);
        int mid = (int)(arr.size()/2);
        for (int ix = 0; ix < arr.size(); ix++) {
            List<String> pair = arr.get(ix);
            int assoc = Integer.parseInt(pair.get(0));
            StringBuilder sb = getBuilderForAssociation(ret, assoc);
            appendAssociatedStringToBuilder(mid, ix, pair, sb);
        }
        StringBuilder sb = new StringBuilder();
        for (StringBuilder retSb : ret.values()) {
            sb.append(retSb);
        }
        System.out.println(sb.toString());
    }

    private static void appendAssociatedStringToBuilder(int mid, int ix, List<String> pair, StringBuilder sb) {
        if (ix < mid) {
            sb.append("-");
        } else {
            sb.append(pair.get(1));
        }
        sb.append(' ');
    }

    private static StringBuilder getBuilderForAssociation(HashMap<Integer, StringBuilder> ret, int assoc) {
        StringBuilder sb = ret.get(assoc);
        if (sb == null) {
            sb = new StringBuilder(50);
            ret.put(assoc, sb);
        }
        return sb;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(
                "20\n0 ab\n6 cd\n0 ef\n6 gh\n4 ij\n0 ab\n6 cd\n0 ef\n6 gh\n0 ij\n4 that\n3 be\n0 to\n1 be\n5 question\n1 or\n2 not\n4 is\n2 to\n4 the"));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<List<String>> arr = new ArrayList<>();

        IntStream.range(0, n).forEach(i -> {
            try {
                arr.add(Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" ")).collect(toList()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        CountSort4.countSort(arr);
    }

}
