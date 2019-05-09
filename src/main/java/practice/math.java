package practice;

import java.util.*;
import java.lang.*;
import java.io.*;

class DistanceCalculator
{
    public static void main (String[] args)
    {
        double distance = miles(33.9425, -118.4081, 33.9800, -118.1522);
        if (distance < 20)
            System.out.println("ye");
        else
            System.out.println("na");

        System.out.println(distance);
    }

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