package spouts;

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

/**
 * Apache Spark Spout. Reads data from flights.txt in JSON
 * format and prepares the information to be passed to the
 * first bolt.
 */
public class FlightsDataReader extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private String msg = "";
    private static int counter = 0;
    private JSONArray flight;
    private Object obj;

    public FlightsDataReader() {}

    public void ack(Object msgID) {
        System.out.println("OK: " + msgID);
    }

    public void close() {}

    public void fail(Object msgID) {
        System.out.println("FAIL: " + msgID);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("JSON"));
    }

    public void open(Map config, TopologyContext context, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
        try {
            obj = new JSONParser().parse(new FileReader((config.get("FlightsData").toString())));
            JSONObject jo = (JSONObject) obj;
            flight = (JSONArray) jo.get("states");
            msg = flight.get(0)+"";
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public void nextTuple() {
        Utils.sleep(1000);
        msg = flight.get(counter++)+"";
        collector.emit(new Values(msg));
    }
}
