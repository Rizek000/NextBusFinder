package org.example;

import org.onebusaway.gtfs.model.StopTime;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BusScheduler {

    private GTFSReaderUtil gtfsReader;

    public BusScheduler(String gtfsZipPath) throws IOException {
        this.gtfsReader = new GTFSReaderUtil(gtfsZipPath);
    }

    public List<String> getNextBuses(String stopId, int maxBuses, String timeFormat) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endTime = currentTime.plusHours(2);

        List<StopTime> stopTimes = gtfsReader.getStopTimes(stopId);
        List<StopTime> filteredStopTimes = stopTimes.stream()
                .filter(stopTime -> {
                    LocalTime arrivalTime = secondsSinceMidnightToLocalTime(stopTime.getArrivalTime());
                    return arrivalTime.isAfter(currentTime.toLocalTime()) &&
                            arrivalTime.isBefore(endTime.toLocalTime());
                })
                .sorted((st1, st2) -> Integer.compare(st1.getDepartureTime(), st2.getDepartureTime()))
                .collect(Collectors.toList());

        return filteredStopTimes.stream()
                .limit(maxBuses)
                .map(stopTime -> formatOutput(stopTime, timeFormat, currentTime))
                .collect(Collectors.toList());
    }

    private LocalTime secondsSinceMidnightToLocalTime(int seconds) {
        return LocalTime.ofSecondOfDay(seconds);
    }

    private String formatOutput(StopTime stopTime, String timeFormat, LocalDateTime currentTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = secondsSinceMidnightToLocalTime(stopTime.getDepartureTime());
        if (timeFormat.equals("relative")) {
            long minutes = java.time.Duration.between(currentTime.toLocalTime(), time).toMinutes();
            return String.format("Route %s: %d mins", stopTime.getTrip().getRoute().getId().getId(), minutes);
        } else {
            return String.format("Route %s: %s", stopTime.getTrip().getRoute().getId().getId(), time.format(formatter));
        }
    }

    public static void main(String[] args) {

        try {
            BusScheduler scheduler = new BusScheduler("path/to/gtfs.zip");
            List<String> nextBuses = scheduler.getNextBuses("12345", 5, "absolute");
            nextBuses.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}