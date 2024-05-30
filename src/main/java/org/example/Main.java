package org.example;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: busTrips <stopId> <number_of_buses> <relative|absolute>");
            return;
        }

        String stopId = args[0];
        int numberOfBuses = Integer.parseInt(args[1]);
        String timeFormat = args[2];

        try {
            BusScheduler scheduler = new BusScheduler("path/to/gtfs.zip");
            List<String> nextBuses = scheduler.getNextBuses(stopId, numberOfBuses, timeFormat);
            nextBuses.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}