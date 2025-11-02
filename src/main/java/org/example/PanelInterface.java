package org.example;

/**
 * Panel-specific helper operations.
 */
public interface PanelInterface {
    /**
     * Returns true if the other RealEstate has the same total price as this.
     *
     * @param other other RealEstate
     * @return true if same total price
     */
    boolean hasSameAmount(RealEstate other);

    /**
     * Average price of one room (no city/floor/insulation modifiers)
     *
     * @return average room price as integer
     */
    int roomprice();
}
