package bolts;

import org.apache.storm.shade.org.json.simple.JSONObject;
import org.apache.storm.shade.org.json.simple.parser.JSONParser;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class HubIdentifier extends BaseBasicBolt {

    public void cleanup() { }

    public void prepare() {

    }

    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String msg = tuple.getString(0);  // works fine for whole json
        String[] parse = tuple.getString(0).split(",");

        String callSign = parse[1];
        String longitude = parse[5];
        String latitude = parse[6];


        try {
          //  Object obj = new JSONParser().parse(msg);
          //  JSONObject jo = (JSONObject) obj;





            File file = new File("src/main/resources/storm.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(msg + "\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        collector.emit(new Values(callSign, longitude, latitude)); //
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("world"));
    }
}