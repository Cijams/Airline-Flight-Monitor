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

        String longitude2 = null;
        String latitude2 = null;

        String callSign = parse[1];
        String longitude1 = parse[5];
        String latitude1 = parse[6];

      //  String longitude2 = airports;

        try {
            for(int i = 0; i < 40; i++) {
               String[] data = airports[i].split(",");
               latitude2 = data[2];
               longitude2 = data[3];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
          //  Object obj = new JSONParser().parse(msg);
          //  JSONObject jo = (JSONObject) obj;

            File file = new File("src/main/resources/storm.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            for(int i = 0; i < 40; i++) {
                String[] data = airports[i].split(",");
                latitude2 = data[2];
                longitude2 = data[3];
                bw.write(latitude2 + "\n");

            }




            bw.write(latitude2 + "\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        collector.emit(new Values(0, 0, 0)); // TODO PROPER OUTPUT
    }

    // remove me
    static int test = 0;

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("airport.city", "airport.code", "call-sign"));
    }

    /**
     * Calculate the distance between two points on the globe given
     * both latitude and longitude.
     *
     * @param latitude1     The latitude of the airplane.
     * @param longitude1    The longitude of the airplane.
     * @param latitude2     The latitude of the airport.
     * @param longitude2    The longitude of the airport.
     * @return              The absolute distance in miles.
     */
    private static double miles(double latitude1, double longitude1,
                                double latitude2, double longitude2) {
        int minutes = 60;
        double toNauticalMiles = 1.1515;
        if ((latitude1 == latitude2) && (longitude1 == longitude2))
            return 0;
            double difference = longitude1 - longitude2;
            double dist = Math.sin(Math.toRadians(latitude1)) * Math.sin(Math.toRadians(latitude2)) +
                    Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                            Math.cos(Math.toRadians(difference));
            return (Math.toDegrees(Math.acos(dist)) * minutes * toNauticalMiles);
    }
}