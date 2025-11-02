package org.example;

/**
 * Simple main to demonstrate logging and project start.
 */
public class Main {
    /**
     * Application entry point.
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        // configure logging
        LoggerConfig.setup();

        // simple greeting and demo
        System.out.println("Hello");
    }
}
