package system.app.email;

import java.io.*;
import java.util.*;

/* email map */
public class StudentEmailMap {

    /* load map */
    public static Map<String, String> load(String path) {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 3) {
                    String id = p[0].trim();
                    String email = p[2].trim();
                    map.put(id, email);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return map;
    }
}
