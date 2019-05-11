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
    private int id;
    private String name;
    Map<String, Map<String, Integer>> counter;

    public void cleanup() {
        // sout + write
    }


    public void prepare(Map stormConf, TopologyContext context) {
        this.counter = new HashMap<String, Map<String, Integer>>();
    }

    public void execute(Tuple tuple, BasicOutputCollector collector) {

        try {
            String city = tuple.getString(0);
            String code = tuple.getString(1);
            String callSign = tuple.getString(2).substring(1,4);

            File file = new File("src/main/resources/storm.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(city + " ");
            bw.write(code + " ");
            bw.write(callSign);
            bw.write("\n");
            bw.close();

            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) { }
}