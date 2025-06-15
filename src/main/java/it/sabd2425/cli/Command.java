package it.sabd2425.cli;

import picocli.CommandLine;

import javax.validation.constraints.Positive;

public class Command implements Runnable {
    @CommandLine.Option(names = "--apitoken", required = true)
    private String apiToken;

    @CommandLine.Option(names = "--name", required = true)
    private String name;

    @CommandLine.Option(names = "--limit", required = true, description = "Maximum number of batches")
    @Positive
    private Integer limit;

    @CommandLine.Option(names = "--test", defaultValue = "true")
    private boolean test;

    @Override
    public void run() {
        // unused
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getName() {
        return name;
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean isTest() {
        return test;
    }
}