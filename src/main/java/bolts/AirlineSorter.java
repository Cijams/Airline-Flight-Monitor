package bolts;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

public class AirlineSorter extends BaseBasicBolt {
    public void execute(Tuple tuple, BasicOutputCollector collector) { }
    public void declareOutputFields(OutputFieldsDeclarer declarer) { }
}