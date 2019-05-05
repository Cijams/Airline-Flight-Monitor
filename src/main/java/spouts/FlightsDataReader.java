package spouts;

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
 * The spout.
 */
public class FlightsDataReader extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private String msg = "";

    public FlightsDataReader() {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("src/main/resources/flights.txt"));
            msg = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("hello"));
    }

    public void open(Map map, TopologyContext context, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
    }

    public void nextTuple() {
        Utils.sleep(1000);
        collector.emit(new Values(msg));
    }
}