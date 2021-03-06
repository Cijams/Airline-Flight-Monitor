package spouts;

/*
 *  Christopher Ijams
 *  FlightsDataReader
 */

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
 * Apache Storm Spout. Reads data from flights.txt in JSON
 * format and prepares the information to be passed to the
 * first bolt.
 */
public class FlightsDataReader extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private JSONArray flight;
    private static int flightCounter = 0;

    /**
     * Acknowledgment from spout if successful.
     *
     * @param msgID status response from topology.
     */
    public void ack(Object msgID) {
        System.out.println("OK: " + msgID);
    }

    /**
     * Acknowledgment from spout if failed.
     *
     * @param msgID status response from topology.
     */
    public void fail(Object msgID) {
        System.out.println("FAIL: " + msgID);
    }

    /**
     * Used to establish spout output within the topology.
     *
     * @param declarer output field for processing.
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("JSON"));
    }

    /**
     * Reads flight data to for processing and establishes local context of the topology.
     *
     * @param config               The topology configuration established in TopologyMain.
     * @param context              Stored datafields within the Topology.
     * @param spoutOutputCollector Used to establish which bolt to emit too.
     */
    public void open(Map config, TopologyContext context, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
        try {
            Object obj = new JSONParser().parse(new FileReader((config.get("FlightsData").toString())));
            JSONObject jo = (JSONObject) obj;
            flight = (JSONArray) jo.get("states");
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Data stream that is distributed to each instance of
     * HubIdentifier indefinitely.
     */
    public void nextTuple() {
        Utils.sleep(75);
        collector.emit(new Values(flight.get(flightCounter++) + ""));
    }
}