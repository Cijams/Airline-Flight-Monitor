package bolts;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;


public class AirlineSorter extends BaseBasicBolt {

    public void cleanup() { }

    public void prepare(Map stormConf, TopologyContext context) {

    }

    public void execute(Tuple tuple, BasicOutputCollector collector) {

        try {
            String printMe = tuple.getString(0);
            String printMe2 = tuple.getString(1);
            String printMe3 = tuple.getString(2);

            File file = new File("src/main/resources/storm.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(printMe + " ");
            bw.write(printMe2 + " ");
            bw.write(printMe3 + " | " + printMe3.length());
            bw.write("\n");
            bw.close();

            } catch (Exception e) {
            e.printStackTrace();


        }


    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) { }
}