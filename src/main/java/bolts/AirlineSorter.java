package bolts;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;


public class AirlineSorter extends BaseBasicBolt {

    public void cleanup() { }

    public void prepare(Map stormConf, TopologyContext context) {

    }

    public void execute(Tuple tuple, BasicOutputCollector collector) {

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) { }
}