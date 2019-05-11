package bolts;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.*;
import java.util.Map;

public class HubIdentifier extends BaseBasicBolt {
    String air;
    File file;
    String[] airports = new String[40];
    BufferedReader br;

    public void cleanup() { }

    public void prepare(Map stormConf, TopologyContext context){
        prepareFile(stormConf);
    }

    private void prepareFile(Map stormConf) {
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
        }
    }

    public void execute(Tuple tuple, BasicOutputCollector collector) {
        String msg = tuple.getString(0);  // works fine for whole json
        String[] parse = tuple.getString(0).split(",");

        String airportsLongitude;
        String airportLatitude;

        String callSign = parse[1];
        String planeLongitude = parse[5];
        String planeLatitude = parse[6];

        try {
            File file = new File("src/main/resources/storm.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);


            for(int i = 0; i < 40; i++) {
                String[] data = airports[i].split(",");
                airportLatitude = data[2];
                airportsLongitude = data[3];

                double d = calculateDistance(planeLatitude, planeLongitude, airportLatitude, airportsLongitude);
                    // This works, but something is up with input.
                if (d <= 20) {
                    bw.write("DISTANCE: " + d + " |" + msg + "\n");
                    break;
                }
           }
          //  bw.write(msg + "\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        collector.emit(new Values(0, 0, 0)); // TODO PROPER OUTPUT
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("airport.city", "airport.code", "call-sign"));
    }

    /**
     * Calculates the distance between two points on the globe given
     * both latitude and longitude.
     *
     * @param latitude1S     The latitude of the airplane.
     * @param longitude1S    The longitude of the airplane.
     * @param latitude2S    The latitude of the airport.
     * @param longitude2S   The longitude of the airport.
     * @return              The absolute distance in calculateDistance.
     */
    private static double calculateDistance(String latitude1S, String longitude1S,
                                            String latitude2S, String longitude2S) {
        int minutes = 60;
        double toNauticalMiles = 1.1515;
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
            return 900;
        }
    }
}