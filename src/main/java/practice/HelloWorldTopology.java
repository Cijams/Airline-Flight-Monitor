package practice;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

public class HelloWorldTopology {

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("Hello", new HelloWorldSpout(), 12);
        builder.setBolt("World", new HelloWorldBolt(), 12).shuffleGrouping("Hello");
        builder.setBolt("WorldTwo", new HelloWorldBolt(), 12).shuffleGrouping("World");
        builder.setBolt("WorldThree", new HelloWorldBolt(), 12).shuffleGrouping("WorldTwo");

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
            cluster.submitTopology("Hello-World", config, builder.createTopology());
            Utils.sleep(10000);
            cluster.killTopology("Hello-World");
            cluster.shutdown();
        }
    }
}