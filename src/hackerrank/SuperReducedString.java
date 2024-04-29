package hackerrank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

public class SuperReducedString {


    /*
     * Complete the 'superReducedString' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts STRING s as parameter.
     */

    public static String superReducedString(String s) {
        int strLen = s.length();
        // Write your code here
        if (strLen == 0) return "Empty String";

        StringBuilder ret = new StringBuilder(strLen);
        char lastAppendedChar = s.charAt(0);
        ret.append(lastAppendedChar);
        for (int ix=1; ix < strLen; ix++) {
            // if the previously added char is the same as the next one, remove them
            if (lastAppendedChar == s.charAt(ix)) {
                ret.deleteCharAt(ret.length()-1);
                lastAppendedChar = ret.length() > 0 ? ret.charAt(ret.length()-1) : ' ';
            } else {
                lastAppendedChar = s.charAt(ix);
                ret.append(lastAppendedChar);
            }
        }
        if (ret.length() == 0) return "Empty String";

        return ret.toString();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader("baab"));
        BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(System.out));

        String s = bufferedReader.readLine();

        String result = SuperReducedString.superReducedString(s);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }

}
