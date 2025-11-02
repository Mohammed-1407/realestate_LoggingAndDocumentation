package org.example;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basic RealEstate class representing a property with price, sqm and rooms.
 */
public class RealEstate implements PropertyInterface {
    private static final Logger LOGGER = Logger.getLogger(RealEstate.class.getName());

    private String city;
    private double price;         // price per sqm
    private int sqm;
    private double numberOfRooms;
    private Genre genre;

    /**
     * Default constructor.
     */
    public RealEstate() {
        LOGGER.info("Called RealEstate() constructor");
    }

    /**
     * Full constructor.
     *
     * @param city          city name
     * @param price         price per sqm
     * @param sqm           square meters
     * @param numberOfRooms number of rooms
     * @param genre         genre
     */
    public RealEstate(String city, double price, int sqm, double numberOfRooms, Genre genre) {
        this.city = city;
        this.price = price;
        this.sqm = sqm;
        this.numberOfRooms = numberOfRooms;
        this.genre = genre;
        LOGGER.info(String.format("Called RealEstate(...) constructor city=%s, price=%.2f, sqm=%d, rooms=%.1f",
                city, price, sqm, numberOfRooms));
    }

    // getters / setters (only a few shown; add more if needed)

    /**
     * Returns the city.
     *
     * @return city name
     */
    public String getCity() {
        LOGGER.info("Called RealEstate.getCity()");
        return city;
    }

    /**
     * Returns price per sqm.
     *
     * @return price per sqm
     */
    public double getPrice() {
        LOGGER.info("Called RealEstate.getPrice()");
        return price;
    }

    /**
     * Returns square meters.
     *
     * @return sqm
     */
    public int getSqm() {
        LOGGER.info("Called RealEstate.getSqm()");
        return sqm;
    }

    /**
     * Returns number of rooms.
     *
     * @return number of rooms
     */
    public double getNumberOfRooms() {
        LOGGER.info("Called RealEstate.getNumberOfRooms()");
        return numberOfRooms;
    }

    /**
     * Returns genre.
     *
     * @return genre
     */
    public Genre getGenre() {
        LOGGER.info("Called RealEstate.getGenre()");
        return genre;
    }

    /**
     * Sets city.
     *
     * @param city city name
     */
    public void setCity(String city) {
        LOGGER.info("Called RealEstate.setCity(" + city + ")");
        this.city = city;
    }

    /**
     * Sets price per sqm.
     *
     * @param price price per sqm
     */
    public void setPrice(double price) {
        LOGGER.info("Called RealEstate.setPrice(" + price + ")");
        this.price = price;
    }

    /**
     * Sets sqm.
     *
     * @param sqm square meters
     */
    public void setSqm(int sqm) {
        LOGGER.info("Called RealEstate.setSqm(" + sqm + ")");
        this.sqm = sqm;
    }

    /**
     * Sets number of rooms.
     *
     * @param numberOfRooms number of rooms
     */
    public void setNumberOfRooms(double numberOfRooms) {
        LOGGER.info("Called RealEstate.setNumberOfRooms(" + numberOfRooms + ")");
        this.numberOfRooms = numberOfRooms;
    }

    /**
     * Sets genre.
     *
     * @param genre genre
     */
    public void setGenre(Genre genre) {
        LOGGER.info("Called RealEstate.setGenre(" + genre + ")");
        this.genre = genre;
    }

    /**
     * Reduce the price per sqm by percent.
     *
     * @param percent integer percent to reduce (if <= 0 does nothing)
     */
    @Override
    public void makeDiscount(int percent) {
        LOGGER.info("Called RealEstate.makeDiscount(" + percent + ")");
        if (percent <= 0) return;
        // reduce price per sqm by percent
        price = price * (1.0 - percent / 100.0);
    }

    /**
     * Calculate total price (price * sqm) with city modifier.
     *
     * @return total price as rounded int
     */
    @Override
    public int getTotalPrice() {
        LOGGER.info("Called RealEstate.getTotalPrice()");
        double base = price * sqm;           // base total
        double modifier = 0.0;

        if (city != null) {
            String c = city.trim().toLowerCase();
            if (c.equals("budapest")) modifier = 0.30;        // +30%
            else if (c.equals("debrecen")) modifier = 0.20;   // +20%
            else if (c.equals("nyíregyháza") || c.equals("nyiregyhaza")) modifier = 0.15; // +15%
        }

        double total = base * (1.0 + modifier);
        return (int) Math.round(total);
    }

    /**
     * Average square meters per room.
     *
     * @return average sqm per room or 0 if rooms <= 0
     */
    @Override
    public double averageSqmPerRoom() {
        LOGGER.info("Called RealEstate.averageSqmPerRoom()");
        if (numberOfRooms <= 0) return 0.0;
        return (double) sqm / numberOfRooms;
    }

    /**
     * Human readable representation.
     *
     * @return string describing the real estate
     */
    @Override
    public String toString() {
        LOGGER.info("Called RealEstate.toString()");
        StringBuilder sb = new StringBuilder();
        sb.append("RealEstate [city = ").append(city)
                .append(", genre = ").append(genre)
                .append(", pricePerSqm = ").append(String.format("%.2f", price))
                .append(", sqm = ").append(sqm)
                .append(", rooms = ").append(numberOfRooms)
                .append("]\nTotal price (with city modifier): ").append(getTotalPrice())
                .append("\nAverage sqm per room: ").append(String.format("%.2f", averageSqmPerRoom()));
        return sb.toString();
    }
}
