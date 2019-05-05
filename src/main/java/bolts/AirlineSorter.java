package bolts;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AirlineSorter extends BaseBasicBolt {
    public void execute(Tuple tuple, BasicOutputCollector collector) { }
    public void declareOutputFields(OutputFieldsDeclarer declarer) { }
}