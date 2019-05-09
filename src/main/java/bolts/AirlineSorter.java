package bolts;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;



import org.apache.storm.shade.org.json.simple.JSONArray;
import org.apache.storm.shade.org.json.simple.JSONObject;
import org.apache.storm.shade.org.json.simple.parser.JSONParser;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import java.io.*;
import java.util.Map;









public class AirlineSorter extends BaseBasicBolt {
    String air;
    File file;
    String[] airports = new String[40];


    public void cleanup() { }

    public void prepare(Map stormConf, TopologyContext context){
        try {
            file = new File(stormConf.get("AirportsData").toString());
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(file));
            int index = 0;
            while (index < 50) {
                air = br.readLine();
                if (air != null)
                    airports[index] = air;
                br.readLine();
                index += 1;
            }
        } catch (Exception e) {
        }
    }
    static int test = 0;

    public void execute(Tuple tuple, BasicOutputCollector collector) {



        // get call sign, latitude and longitude of each flight data.

        // check if this flight is within 20 miles of any of the 40 airports.

        // if so, emit the airport city, airport code, and flight.





        String callSign = tuple.getString(0);

        try {
            //  Object obj = new JSONParser().parse(msg);
            //  JSONObject jo = (JSONObject) obj;

            File file = new File("src/main/resources/CALLS.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(airports[test++] + "\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) { }
}