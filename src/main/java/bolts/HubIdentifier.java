package bolts;

/*
 *  Christopher Ijams
 *  HubIdentifier
 */

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import java.io.*;
import java.util.Map;

/**
 * Identifies all flights within 20 miles of the top 40 airlines.
 */
public class HubIdentifier extends BaseBasicBolt {
    private String[] airports = new String[40];

    public void cleanup() { }

    public void prepare(Map stormConf, TopologyContext context){
        prepareFile(stormConf);
    }

    /**
     * Prepares the file to be parsed by execute().
     *
     * @param stormConf The topology configuration established in TopologyMain.
     */
    private void prepareFile(Map stormConf) {
        String air;
        File file;
        BufferedReader br;

        try {
            file = new File(stormConf.get("AirportsData").toString());
            br = new BufferedReader(new FileReader(file));

            int index = 0;
            while (index < 50) {
                air = br.readLine();
                if (air != null)
                    airports[index] = air;
                br.readLine();
                index += 1;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Reads data from the established 40 airports and compares the
     * current flight within the node. If distance is less than 20
     * miles, information forwarded to AirlineSorter.
     *
     * @param tuple The incoming data from spout: FlightsDataReader.
     * @param collector Topology information and object used to emit data.
     */
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String[] flightData = tuple.getString(0).split(",");

        // Flight data.
        String planeLongitude = flightData[5];
        String planeLatitude = flightData[6];
        String callSign = flightData[1];

        // Airport data.
        String airportsLongitude;
        String airportLatitude;
        String airportCity;
        String airportCode;

        try {
            File file = new File("src/main/resources/storm.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            for(int i = 0; i < 40; i++) {
                String[] data = airports[i].split(",");
                airportCity = data[0];
                airportCode = data[1];
                airportLatitude = data[2];
                airportsLongitude = data[3];

                if (calculateDistance(planeLatitude, planeLongitude, airportLatitude, airportsLongitude) <= 20) {
                    if (callSign.length() > 2)
                        collector.emit(new Values(airportCity, airportCode, callSign));
                        break;
                }
           }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to establish bolt output within the topology.
     *
     * @param declarer output field.
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("airport.city", "airport.code", "call-sign"));
    }

    /**
     * Calculates the distance between two points on the globe given
     * both latitude and longitude.
     *
     * @param latitude1S     The latitude of the airplane.
     * @param longitude1S    The longitude of the airplane.
     * @param latitude2S     The latitude of the airport.
     * @param longitude2S    The longitude of the airport.
     * @return               The absolute distance in calculateDistance.
     */
    private static double calculateDistance(String latitude1S, String longitude1S,
                                            String latitude2S, String longitude2S) {
        int minutes = 60;
        double toNauticalMiles = 1.1515;
        double ParseFailureCode = 99999;
        try {
            double latitude1 = Double.parseDouble(latitude1S);
            double latitude2 = Double.parseDouble(latitude2S);
            double longitude1 = Double.parseDouble(longitude1S);
            double longitude2 = Double.parseDouble(longitude2S);

            if ((latitude1 == latitude2) && (longitude1 == longitude2))
                return 0;
            double difference = longitude1 - longitude2;
            double dist = Math.sin(Math.toRadians(latitude1)) * Math.sin(Math.toRadians(latitude2)) +
                    Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                            Math.cos(Math.toRadians(difference));
            return (Math.toDegrees(Math.acos(dist)) * minutes * toNauticalMiles);
        } catch (Exception e) {
            return ParseFailureCode;
        }
    }
}