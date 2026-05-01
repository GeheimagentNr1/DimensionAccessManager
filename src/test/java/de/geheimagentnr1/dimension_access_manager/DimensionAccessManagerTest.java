package de.geheimagentnr1.dimension_access_manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DimensionAccessManagerTest {

    @Test
    void modIdIsValid() {

        String modId = "dimension_access_manager";
        assertTrue( modId.matches( "[a-z][a-z0-9_]{1,63}" ) );
    }
}
