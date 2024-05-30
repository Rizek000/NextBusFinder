package org.example;

import org.onebusaway.gtfs.impl.GtfsRelationalDaoImpl;
import org.onebusaway.gtfs.model.StopTime;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.onebusaway.gtfs.serialization.GtfsReader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GTFSReaderUtil {
    private GtfsMutableRelationalDao dao;

    public GTFSReaderUtil(String zipFilePath) throws IOException {
        dao = new GtfsRelationalDaoImpl();
        GtfsReader reader = new GtfsReader();
        reader.setInputLocation(new File(zipFilePath));
        reader.setEntityStore(dao);
        reader.run();
    }

    public List<StopTime> getStopTimes(String stopId) {
        return dao.getAllStopTimes().stream()
                .filter(stopTime -> stopTime.getStop().getId().getId().equals(stopId))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        try {
            GTFSReaderUtil reader = new GTFSReaderUtil("src/gtfs.zip");
            List<StopTime> stopTimes = reader.getStopTimes("2");
            stopTimes.forEach(stopTime -> System.out.println(stopTime.getDepartureTime()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}