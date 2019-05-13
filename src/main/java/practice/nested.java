package practice;

import java.util.HashMap;
import java.util.Map;

public class nested {
    public static void main(String[] args) {

        Map<String, Map<String, Integer>> counter = new HashMap<String, Map<String, Integer>>();

        String city = "Chicago";
        String code = "MDW";
        String callsign = "UAL";

        if(counter.get(city) == null) {
            counter.put(city, new HashMap<String, Integer>());
            counter.get(city).put(code, 1);
        }
        else if(counter.get(city).get(code) == null) {
            counter.get(city).put(code, 1);
        }
        else {
            counter.get(city).put(code, counter.get(city).get(code)+1);
        }

        code = "RTE";

        if(counter.get(city) == null) {
            counter.put(city, new HashMap<String, Integer>());
            counter.get(city).put(code, 1);
        }
        else if(counter.get(city).get(code) == null) {
            counter.get(city).put(code, 1);
        }
        else {
            counter.get(city).put(code, counter.get(city).get(code)+1);
        }

        if(counter.get(city) == null) {
            counter.put(city, new HashMap<String, Integer>());
            counter.get(city).put(code, 1);
        }
        else if(counter.get(city).get(code) == null) {
            counter.get(city).put(code, 1);
        }
        else {
            counter.get(city).put(code, counter.get(city).get(code)+1);
        }


        System.out.println(counter);

    }
}