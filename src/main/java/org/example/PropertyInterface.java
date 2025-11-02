package org.example;

/**
 * Common operations for properties.
 */
public interface PropertyInterface {
    /**
     * Reduces the price per sqm by given integer percentage.
     *
     * @param percent percent to reduce (integer, if <= 0 method does nothing)
     */
    void makeDiscount(int percent);

    /**
     * Returns total price (including city modifier) as integer.
     *
     * @return total price rounded to int
     */
    int getTotalPrice();

    /**
     * Average square meters per room.
     *
     * @return average sqm per room as double
     */
    double averageSqmPerRoom();
}
