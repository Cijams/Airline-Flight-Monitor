package bolts;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class AirlineSorter extends BaseBasicBolt {
    private Map<String, Map<String, Integer>> counter;

    File file;
    FileWriter fw;
    BufferedWriter bw;

    public void cleanup() {
        try {
            File file = new File("src/main/resources/storm.txt");
            FileWriter fw = new FileWriter(file.getCanonicalFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            String test = "";

            for(Map.Entry<String, Map<String, Integer>> entry : counter.entrySet())
                test += entry.getKey()+" "+entry.getValue() + "\n";
               // System.out.println(entry.getKey()+" "+entry.getValue());

            bw.write(test);
            bw.flush();
            bw.close();
        } catch (Exception e ) {
        }
    }

    /**
     * Initialize "counter" to a map in the form of:
     *
     * Airport:
     *      Airline: Occurrence
     *
     * @param stormConf The topology configuration established in TopologyMain
     * @param context Stored datafields within the Topology
     */
    public void prepare(Map stormConf, TopologyContext context) {
        this.counter = new HashMap<String, Map<String, Integer>>();
    }

    public void execute(Tuple tuple, BasicOutputCollector collector) {
        try {
            String city = tuple.getString(0);
            String code = tuple.getString(1);
            String callSign = tuple.getString(2).substring(1,4);

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
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) { }
}