package org.example;

import java.io.IOException;
import java.util.logging.*;

/**
 * Central logger configuration. Call LoggerConfig.setup() once at application start (safe to call multiple times).
 */
public final class LoggerConfig {
    private static boolean initialized = false;

    private LoggerConfig() { /* utility */ }

    /**
     * Configure logging: console + file handler ("realEstateApp.log"), INFO level.
     * Safe to call multiple times; configuration happens only once.
     */
    public static synchronized void setup() {
        if (initialized) return;

        Logger root = Logger.getLogger("");
        // Do not add multiple handlers if already present (check by name)
        try {
            // Try to add FileHandler
            Handler fileHandler = new FileHandler("realEstateApp.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.INFO);
            root.addHandler(fileHandler);

            // Ensure root logger level
            root.setLevel(Level.INFO);
            initialized = true;
            root.info("LoggerConfig initialized and logging to realEstateApp.log");
        } catch (IOException | SecurityException e) {
            // If file handler fails, print to stderr (also logged to console)
            System.err.println("Failed to initialize file logging: " + e.getMessage());
            root.log(Level.SEVERE, "Failed to initialize file logging", e);
        }
    }
}
