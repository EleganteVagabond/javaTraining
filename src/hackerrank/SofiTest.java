package hackerrank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SofiTest {
    /*
     * Complete the function below.
     * https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page=[topic]
     */

    static int getTopicCount(String topic) {
        int topicCount = 0;
        // TODO  use the new try with resources methodology
        try { // i know we can use
            // open the url
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=text&format=json&page="+topic);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.connect();
            // check the response code
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close(); // make sure we close the connection
                String[] strings = response.toString().split(" ");

                // couldn't get this to work so went old school
                //topicCount = Arrays.stream(strings).reduce(0,(int cnt,String str) -> cnt += str.toLowerCase().contains(topic.toLowerCase) ? 1 : 0);
                for (int i=0;i<strings.length;i++) {
                    if(strings[i].contains(topic)) {
                        // might be more than one occurrence?
                        int index = strings[i].indexOf(topic);
                        while (index != -1) {
                            topicCount++;
                            index = strings[i].indexOf(topic,index+1);
                        }
                    }
                }
            } else {
                System.out.println("What went wrong here?" + status);
            }
        }catch(Exception e) {
            System.err.println(e.toString());
        }
        return topicCount;

    }

    public static void main(String[] args) throws IOException{
    	// to load from a file
        Scanner in = new Scanner(new StringReader("pizza"));
        final String fileName = "output";
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        int res;
        String _topic;
        try {
            _topic = in.nextLine();
        } catch (Exception e) {
            _topic = null;
        }

        try {
            res = getTopicCount(_topic);
        } catch (Exception e) {
            res = 0;
        }

        System.out.println("Found " + _topic + " " + res + " times");

        bw.write(String.valueOf(res));
        bw.newLine();

        bw.close();
    }
}