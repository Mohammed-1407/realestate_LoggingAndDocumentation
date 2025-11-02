package org.example;

import java.util.logging.Logger;

/**
 * Panel (apartment) type of RealEstate with floor and insulation modifiers.
 */
public class Panel extends RealEstate implements PanelInterface {
    private static final Logger LOGGER = Logger.getLogger(Panel.class.getName());

    private int floor;           // which floor the apartment is on
    private boolean isInsulated; // external insulation

    /**
     * Default constructor.
     */
    public Panel() {
        super();
        LOGGER.info("Called Panel() constructor");
    }

    /**
     * Full constructor for Panel.
     *
     * @param city          city name
     * @param price         price per sqm
     * @param sqm           square meters
     * @param numberOfRooms number of rooms
     * @param genre         property genre
     * @param floor         floor number
     * @param isInsulated   insulation flag
     */
    public Panel(String city, double price, int sqm, double numberOfRooms, Genre genre,
                 int floor, boolean isInsulated) {
        super(city, price, sqm, numberOfRooms, genre);
        this.floor = floor;
        this.isInsulated = isInsulated;
        LOGGER.info(String.format("Called Panel(...) constructor city=%s, floor=%d, insulated=%b",
                city, floor, isInsulated));
    }

    /**
     * Returns the floor.
     *
     * @return floor number
     */
    public int getFloor() {
        LOGGER.info("Called Panel.getFloor()");
        return floor;
    }

    /**
     * Returns whether the apartment is insulated.
     *
     * @return insulation flag
     */
    public boolean isInsulated() {
        LOGGER.info("Called Panel.isInsulated()");
        return isInsulated;
    }

    /**
     * Sets the floor number.
     *
     * @param floor new floor number
     */
    public void setFloor(int floor) {
        LOGGER.info("Called Panel.setFloor(" + floor + ")");
        this.floor = floor;
    }

    /**
     * Sets insulation flag.
     *
     * @param insulated new insulation flag
     */
    public void setInsulated(boolean insulated) {
        LOGGER.info("Called Panel.setInsulated(" + insulated + ")");
        isInsulated = insulated;
    }

    /**
     * Calculates total price including floor and insulation modifiers on top of base RealEstate total.
     *
     * @return total price rounded to nearest integer
     */
    @Override
    public int getTotalPrice() {
        LOGGER.info("Called Panel.getTotalPrice()");
        // start with RealEstate base total (which includes city modifier)
        double total = super.getTotalPrice();

        // apply floor modifiers:
        // floors 0-2 => +5% ; floor 10 => -5%; otherwise no floor modifier
        if (floor >= 0 && floor <= 2) {
            total = total * 1.05;
        } else if (floor == 10) {
            total = total * 0.95;
        }

        // insulated => +5%
        if (isInsulated) total = total * 1.05;

        return (int) Math.round(total);
    }

    /**
     * Checks if other property has same total price.
     *
     * @param other other RealEstate to compare
     * @return true when equal total price, false otherwise
     */
    @Override
    public boolean hasSameAmount(RealEstate other) {
        LOGGER.info("Called Panel.hasSameAmount(other)");
        if (other == null) return false;
        return this.getTotalPrice() == other.getTotalPrice();
    }

    /**
     * Calculates room price (base, without modifiers).
     *
     * @return average base room price rounded to integer
     */
    @Override
    public int roomprice() {
        LOGGER.info("Called Panel.roomprice()");
        // average room price WITHOUT any settlement/floor/insulation modifiers:
        // base total = price per sqm * sqm
        double baseTotal = super.getPrice() * super.getSqm();
        double rooms = super.getNumberOfRooms();
        if (rooms <= 0) return 0;
        double avg = baseTotal / rooms;
        return (int) Math.round(avg);
    }

    /**
     * Human readable representation of Panel.
     *
     * @return string with details and computed values
     */
    @Override
    public String toString() {
        LOGGER.info("Called Panel.toString()");
        StringBuilder sb = new StringBuilder();
        sb.append("Panel Apartment [city=").append(getCity())
                .append(", genre=").append(getGenre())
                .append(", pricePerSqm=").append(String.format("%.2f", getPrice()))
                .append(", sqm=").append(getSqm())
                .append(", rooms=").append(getNumberOfRooms())
                .append(", floor=").append(floor)
                .append(", insulated=").append(isInsulated())
                .append("]\nTotal price (with modifiers): ").append(getTotalPrice())
                .append("\nAverage sqm per room: ").append(String.format("%.2f", averageSqmPerRoom()))
                .append("\nAverage room price (base, no modifiers): ").append(roomprice());
        return sb.toString();
    }
}
