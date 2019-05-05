import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;
import spouts.FlightsDataReader;
import bolts.HubIdentifier;

/**
 *  Topology.
 */
public class TopologyMain {

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("FlightData", new FlightsDataReader(), 12);
        builder.setBolt("HubID", new HubIdentifier(), 12).shuffleGrouping("FlightData");
        builder.setBolt("AirlineSort", new HubIdentifier(), 12).shuffleGrouping("HubID");

        Config config = new Config();
        config.setDebug(true);

        if (args != null && args.length > 0) {
            config.setNumWorkers(6);
            config.setNumAckers(6);
            config.setMaxSpoutPending(100);
            config.setMessageTimeoutSecs(20);
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("Flight-Process", config, builder.createTopology());
            Utils.sleep(10000);
            cluster.killTopology("Flight-Process");
            cluster.shutdown();
        }
    }
}