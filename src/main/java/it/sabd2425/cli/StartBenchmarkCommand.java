package it.sabd2425.cli;

import picocli.CommandLine;

import javax.validation.constraints.Positive;
import java.util.Optional;

public class StartBenchmarkCommand implements Runnable{
    @CommandLine.Option(names = "--apitoken", required = true)
    private String apiToken;

    @CommandLine.Option(names = "--name", required = true)
    private String name;

    @CommandLine.Option(names = "--limit", description = "Maximum number of batches")
    @Positive
    private Integer limit;

    @CommandLine.Option(names = "--test", defaultValue = "true")
    private boolean test;

    @Override
    public void run() {
        // unused
    }

    public static StartBenchmarkCommand fromArgs(String[] args) {
        var cmd = new StartBenchmarkCommand();
        new CommandLine(cmd).parseArgs(args);
        return cmd;
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getName() {
        return name;
    }

    public Optional<Integer> getLimit() {
        return Optional.ofNullable(limit);
    }

    public boolean isTest() {
        return test;
    }
}
