package org.example;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "busscheduler", mixinStandardHelpOptions = true, version = "1.0",
        description = "BusScheduler",
        subcommands = {BusScheduler.class})
public class Main implements Runnable {

    @Option(names = {"-v", "--version"}, versionHelp = true, description = "Display info v")
    boolean versionInfoRequested;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Display help")
    boolean usageHelpRequested;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {

        System.out.println("BusScheduler is running.");
    }
}