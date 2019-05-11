/*
 *  Christopher Ijams
 *  Airline Flight Monitor
 *
 *  Project tracks flights around the top 40 Airports in the U.S.
 *  Uses real-time location of airplanes to find planes within 20 miles
 *  of each airport to determine the number of flights current leaving
 *  or incoming to each airport. Project receives data from the
 *  Open Sky Network for research purposes.
 *
 *  Project implemented in Apache Storm to expedite data processing.
 */

import bolts.AirlineSorter;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;
import spouts.FlightsDataReader;
import bolts.HubIdentifier;

/**
 *  Determines the topology of the the Apache Spark network.
 *
 *  Current Layout:
 *      Spout: FlightsDataReader reads data from "flights.txt" and emits to Bolt: HubIdentifier.
 *      Bolt: HubIdentifier reads from Spout: FlightDataReader and emits to Bolt: AirlineSorter.
 *      Bolt: AirlineSorter reads from Bolt: HubIdentifier and closes.
 */
public class TopologyMain {
    /**
     * @param args - [0] The flight data. Pull from 'get_flights_data.sh'
     *
     *             - [1] The airport list. Dataset of the top 40 US airports.
     *                   Includes each airport's city name, IATA code,
     *                   latitude and longitude.
     */
    public static void main(String[] args)  {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("flight-data-reader", new FlightsDataReader(), 3);
        builder.setBolt("hub-identifier", new HubIdentifier(), 6)
                .shuffleGrouping("flight-data-reader");
        builder.setBolt("airline-sorter", new AirlineSorter(), 6)
                .shuffleGrouping("hub-identifier");

        // Config sets files and low output debugging mode.
        Config config = new Config();
        config.put("FlightsData", args[0]);
        config.put("AirportsData", args[1]);
        config.setDebug(false);
        config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

        // Currently set for local cluster.
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Flight-Process", config, builder.createTopology());
        Utils.sleep(25000);
        cluster.killTopology("Flight-Process");
        cluster.shutdown();
    }
}