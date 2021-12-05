package bgu.spl.mics.application;

import com.google.gson.Gson;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        String gpus="";
        try {
            // create Gson instance
            Gson gson = new Gson();
            Gson gson2 = new Gson();

            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\Gil Khais\\Desktop\\SPL2\\example_input.json"));

            // convert JSON file to map
            Map<?, ?> map = gson.fromJson(reader, Map.class);

            // print map entries
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
                if(entry.getKey().equals("GPUS")){
                    gpus = entry.getValue().toString();
                }
            }

            // close reader
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
