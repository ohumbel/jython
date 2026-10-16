package org.python.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for PyTuple as Java Tuple.
 */
public class PyTupleTest  {

    private PyTuple p = null;

    @BeforeEach
    protected void setUp() throws Exception {
        p = new PyTuple(new PyString("foo"), new PyString("bar"));
    }

    @AfterEach
    protected void tearDown() throws Exception {
        p = null;
    }

    // Test for http://bugs.jython.org/issue1419
    // "Bug in PyTuple.indexOf and PyTuple.indexOf"
    @Test
    public void testIndexOf() {
        PyTuple p = new PyTuple(new PyString("foo"), new PyString("bar"));
        assertEquals(0, p.indexOf("foo"));
        assertEquals(1, p.indexOf("bar"));
    }
    
    @Test
    public void testToArray() {
        // In Jython 2.5.0 if an array was passed into toArray() that was
        // too short, an Object[] was always returned instead of an array
        // of the proper type.
        Object[] test = new String[1];
        String[] s = (String[])p.toArray(test);
        assertEquals(s[0], "foo");
        assertEquals(s[1], "bar");
    }
}
