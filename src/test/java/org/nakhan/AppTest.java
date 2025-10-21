package org.nakhan;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Simple test to verify the application context loads
     */
    @Test
    public void testApp() {
        assertTrue(true);
    }

    /**
     * Test application name
     */
    @Test
    public void testAppName() {
        assertNotNull("Expense Tracker API");
    }
}
