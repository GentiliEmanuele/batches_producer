package it.sabd2425.utils;

import it.sabd2425.cli.StartBenchmarkCommand;
import it.sabd2425.gc25client.data.BenchConfig;

public class Utils {
    public static BenchConfig toBenchConfig(StartBenchmarkCommand command) {
        var o = command.getLimit();
        return o.map(integer -> new BenchConfig(command.getApiToken(), command.getName(), integer, command.isTest()))
                .orElseGet(() -> new BenchConfig(command.getApiToken(), command.getName(), command.isTest()));
    }

    public static String getOrThrow(String name) {
        var val = System.getenv(name);
        if (val == null) {
            throw new IllegalStateException("Environment variable " + name + " not set");
        }
        return val;
    }
}
