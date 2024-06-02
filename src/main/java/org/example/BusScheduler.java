package org.example;


import java.io.IOException;


import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine;

import java.io.*;


@Command(name = "scheduler", mixinStandardHelpOptions = true, description = "Commands to manage the bus schedule")
public class BusScheduler implements Runnable {

    @Option(names = {"-f", "--file"}, description = "GTFS file to process", required = true)
    private String gtfsFile;

    @Option(names = {"-o", "--output"}, description = "Output directory")
    private String outputDir;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new BusScheduler()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("Processing GTFS file: " + gtfsFile);
        if (outputDir != null) {
            System.out.println("Output directory: " + outputDir);
        }
        processGTFSFile(gtfsFile, outputDir);
    }

    private void processGTFSFile(String gtfsFile, String outputDir) {
        try {
            File file = new File(gtfsFile);
            if (!file.exists()) {
                System.err.println("GTFS file not found: " + gtfsFile);
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {

                System.out.println(line);

            }
            br.close();

            if (outputDir != null) {
                File outDir = new File(outputDir);
                if (!outDir.exists()) {
                    if (!outDir.mkdirs()) {
                        System.err.println("Failed to create output directory: " + outputDir);
                        return;
                    }
                }

                File outputFile = new File(outDir, "output.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
                bw.write("Processing completed successfully.");
                bw.close();
            }

        } catch (IOException e) {
            System.err.println("Error processing GTFS file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
