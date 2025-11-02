package org.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Agent that loads properties and produces a report.
 */
public class RealEstateAgent {

    private static final Logger LOGGER = Logger.getLogger(RealEstateAgent.class.getName());

    // TreeSet sorted by total price (ascending), then by city name
    private final TreeSet<RealEstate> properties = new TreeSet<>(
            Comparator.comparingInt(RealEstate::getTotalPrice)
                    .thenComparing(p -> Optional.ofNullable(p.getCity()).orElse("").toLowerCase())
    );

    /**
     * Constructor. Ensures logging is configured.
     */
    public RealEstateAgent() {
        LoggerConfig.setup();
        LOGGER.info("Created RealEstateAgent instance");
    }

    /**
     * Returns an unmodifiable view of properties known by the agent.
     *
     * @return set of properties
     */
    public Set<RealEstate> getProperties() {
        LOGGER.info("Called RealEstateAgent.getProperties()");
        return Collections.unmodifiableSet(properties);
    }

    /**
     * Load properties from file (or sample data if file is missing or unreadable).
     *
     * @param filename path to input file
     */
    public void load(String filename) {
        LOGGER.info("Called RealEstateAgent.load(" + filename + ")");
        List<String> lines;
        Path path = Paths.get(filename);
        if (Files.exists(path)) {
            try {
                lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                LOGGER.info("Read " + lines.size() + " lines from " + filename);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error reading file, using sample data: " + e.getMessage(), e);
                lines = sampleData();
            }
        } else {
            LOGGER.info("'" + filename + "' not found — loading sample data.");
            lines = sampleData();
        }

        for (String line : lines) {
            if (line == null) continue;
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                RealEstate r = parseLine(line);
                if (r != null) properties.add(r);
            } catch (Exception ex) {
                // keep it simple: skip malformed lines but notify
                LOGGER.log(Level.SEVERE, "Skipping invalid line: " + line + " -> " + ex.getMessage(), ex);
            }
        }
        LOGGER.info("Finished loading properties. Total properties: " + properties.size());
    }

    /**
     * Returns built-in sample data lines used when file is absent/unreadable.
     *
     * @return list of sample data lines
     */
    private List<String> sampleData() {
        LOGGER.info("Called RealEstateAgent.sampleData()");
        return Arrays.asList(
                "REALESTATE#Budapest#250000#100#4#CONDOMINIUM",
                "REALESTATE#Debrecen#220000#120#5#FAMILYHOUSE",
                "REALESTATE#Nyíregyháza#110000#60#2#FARM",
                "REALESTATE#Nyíregyháza#250000#160#6#FAMILYHOUSE",
                "REALESTATE#Kisvárda#150000#50#2#CONDOMINIUM",
                "REALESTATE#Nyíregyháza#150000#68#4#CONDOMINIUM#4#yes",
                "PANEL#Budapest#180000#70#3#CONDOMINIUM#4#no",
                "PANEL#Debrecen#120000#35#2#CONDOMINIUM#0#yes",
                "PANEL#Tiszaújváros#120000#750#3#CONDOMINIUM#10#no",
                "PANEL#Nyíregyháza#170000#80#3#CONDOMINIUM#7#no"
        );
    }

    /**
     * Parse a single line from file/sample data into RealEstate or Panel object.
     *
     * @param line input line
     * @return RealEstate object or Panel
     */
    private RealEstate parseLine(String line) {
        LOGGER.info("Called RealEstateAgent.parseLine() for line: " + line);
        String[] parts = line.split("#");
        if (parts.length < 6) throw new IllegalArgumentException("Not enough fields");

        String cls = parts[0].trim();
        String city = parts[1].trim();
        double price = Double.parseDouble(parts[2].trim());
        int sqm = Integer.parseInt(parts[3].trim());
        double rooms = Double.parseDouble(parts[4].trim());
        Genre genre = Genre.valueOf(parts[5].trim().toUpperCase());

        if ("PANEL".equalsIgnoreCase(cls)) {
            if (parts.length < 8) throw new IllegalArgumentException("Panel requires floor and insulation");
            int floor = Integer.parseInt(parts[6].trim());
            String ins = parts[7].trim().toLowerCase();
            boolean insulated = ins.equals("yes") || ins.equals("y") || ins.equals("true");
            LOGGER.info("Parsed Panel: city=" + city + ", floor=" + floor + ", insulated=" + insulated);
            return new Panel(city, price, sqm, rooms, genre, floor, insulated);
        } else {
            LOGGER.info("Parsed RealEstate: city=" + city);
            return new RealEstate(city, price, sqm, rooms, genre);
        }
    }

    /**
     * Produce the requested report, print to console and write to output file.
     *
     * @param outputFilename output file to write the report
     */
    public void produceReport(String outputFilename) {
        LOGGER.info("Called RealEstateAgent.produceReport(" + outputFilename + ")");
        StringBuilder sb = new StringBuilder();
        String lineSeparator = "------------------------------------------------------------\n";

        if (properties.isEmpty()) {
            sb.append("No properties available.\n");
            writeOutput(outputFilename, sb.toString());
            System.out.println(sb.toString());
            return;
        }

        int count = properties.size();

        sb.append(lineSeparator);
        sb.append("REAL ESTATE REPORT\n");
        sb.append(lineSeparator);

        // 1) Average square meter price
        double avgPricePerSqm = properties.stream()
                .mapToDouble(RealEstate::getPrice)
                .average()
                .orElse(0.0);
        sb.append(String.format("1) Average square meter price of real estate: %.2f%n%n", avgPricePerSqm));

        // 2) Price of the cheapest property
        RealEstate cheapest = properties.first();
        sb.append(String.format("2) Price of the cheapest property: %d%n%n", cheapest.getTotalPrice()));

        // 3) Average square meter per room of the most expensive apartment in Budapest
        Optional<RealEstate> mostExpBudapest = properties.stream()
                .filter(p -> p.getCity() != null && p.getCity().trim().equalsIgnoreCase("Budapest"))
                .max(Comparator.comparingInt(RealEstate::getTotalPrice));
        double avgSqmPerRoomMostExpBud = mostExpBudapest.map(RealEstate::averageSqmPerRoom).orElse(0.0);
        sb.append(String.format("3) Average sqm per room of the most expensive apartment in Budapest: %.2f%n%n", avgSqmPerRoomMostExpBud));

        // 4) Total price of all properties
        long totalPrices = properties.stream().mapToLong(RealEstate::getTotalPrice).sum();
        sb.append(String.format("4) Total price of all properties: %d%n%n", totalPrices));

        // 5) List of CONDOMINIUM properties under average total price
        double averageTotalPrice = (double) totalPrices / count;
        sb.append("5) CONDOMINIUM properties with total price <= average price of properties:\n");
        sb.append(lineSeparator);

        List<RealEstate> condosUnderAvg = properties.stream()
                .filter(p -> p.getGenre() == Genre.CONDOMINIUM)
                .filter(p -> p.getTotalPrice() <= averageTotalPrice)
                .collect(Collectors.toList());

        if (condosUnderAvg.isEmpty()) {
            sb.append("  (none)\n\n");
        } else {
            for (RealEstate r : condosUnderAvg) {
                sb.append(r.toString()).append("\n");
                sb.append(lineSeparator);
            }
        }

        // 6) Average square meter price (again)
        sb.append(String.format("6) Average square meter price of real estate (repeated): %.2f%n%n", avgPricePerSqm));

        // 7) Total price of properties (again)
        sb.append(String.format("7) Total price of properties (repeated): %d%n", totalPrices));

        // print and write
        String result = sb.toString();
        System.out.println(result);
        writeOutput(outputFilename, result);
        LOGGER.info("Report produced and written to " + outputFilename);
    }

    /**
     * Write the report content to file.
     *
     * @param filename output filename
     * @param content  content to write
     */
    private void writeOutput(String filename, String content) {
        LOGGER.info("Called RealEstateAgent.writeOutput(" + filename + ")");
        try {
            Files.write(Paths.get(filename), content.getBytes(StandardCharsets.UTF_8));
            System.out.println("Report saved to " + filename);
            LOGGER.info("Report saved to " + filename);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing report: " + e.getMessage(), e);
        }
    }
}
