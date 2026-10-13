package org.python.core;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/** Odd tests of the Python {@code str} implementation that are best done from Java. */
class PyStringTest {

    /**
     * Construction of a {@code PyString} from a {@code String} with characters > {@code 0xff}
     * results in an {@code IllegalArgumentException} that quotes a safely-escaped version of the
     * string.
     */
    @Test
    void preventNonBytePyString() {
        try {
            // Quercus cerris (turkey oak) in Turkish. https://tr.wikipedia.org
            Py.newString("Saçlı meşe");
        } catch (IllegalArgumentException iae) {
            String m = iae.getMessage();
            assertTrue(m.contains("Sa\\xe7l\\u0131 me\\u015fe"), "Quotes message");
            return;
        }
        fail("Expected exception not thrown");
    }
}
