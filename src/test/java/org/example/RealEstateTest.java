package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RealEstate and Panel classes.
 */
public class RealEstateTest {

    private RealEstate baseEstate;
    private Panel panelEstate;

    @BeforeEach
    void setup() {
        // ensure logging is configured for tests
        LoggerConfig.setup();

        baseEstate = new RealEstate("Budapest", 200000, 100, 4, Genre.CONDOMINIUM);
        panelEstate = new Panel("Debrecen", 150000, 60, 3, Genre.CONDOMINIUM, 2, true);
    }

    // 1st Test
    @Test
    void testMakeDiscountUnderTest() {
        double oldPrice = baseEstate.getPrice();
        baseEstate.makeDiscount(10);  // reduce price by 10%
        double expected = oldPrice * 0.9;
        assertEquals(expected, baseEstate.getPrice(), 0.01, "Discount should reduce price correctly");
    }

    // 2nd Test
    @Test
    void testGetTotalPriceUnderTest() {
        // Budapest modifier = +30%
        double expected = 200000 * 100 * 1.30; // price * sqm * modifier
        assertEquals((int) Math.round(expected), baseEstate.getTotalPrice(), "Total price with city modifier incorrect");
    }

    // 3rd Test
    @Test
    void testAverageSqmPerRoomUnderTest() {
        double expected = 100.0 / 4.0;
        assertEquals(expected, baseEstate.averageSqmPerRoom(), 0.001, "Average sqm per room should match");
    }

    // 4th Test
    @Test
    void testPanelTotalPriceUnderTest() {
        // Debrecen +20%, floor (0–2) +5%, insulation +5%
        double base = 150000 * 60;
        double withCity = base * 1.20;
        double withFloor = withCity * 1.05;
        double withIns = withFloor * 1.05;
        int expected = (int) Math.round(withIns);
        assertEquals(expected, panelEstate.getTotalPrice(), "Panel total price (modifiers) mismatch");
    }

    // 5th Test
    @Test
    void testToStringUnderTest() {
        String text = baseEstate.toString();
        assertTrue(text.contains("Budapest"));
        assertTrue(text.contains("Total price"));
        assertTrue(text.contains("Average sqm per room"));
    }
}
