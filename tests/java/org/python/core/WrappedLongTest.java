package org.python.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.python.util.PythonInterpreter;

class WrappedLongTest  {

    // Simulate the use case where you want to expose some (possibly mutable)
    // java long field to an interpreter without having to set the value to a
    // new PyLong each time it changes.
    @SuppressWarnings("serial")
    static class WrappedLong extends PyLong {
        public WrappedLong() {
            super(0);
        }

        private long mutableValue;

        @Override
        public BigInteger getValue() {
            return BigInteger.valueOf(mutableValue);
        }

        public void setMutableValue(final long newValue) {
            mutableValue = newValue;
        }
    }

    private PythonInterpreter interp;
    private WrappedLong a, b;

    @BeforeEach
    void setUp() throws Exception {
        interp = new PythonInterpreter(new PyStringMap(), new PySystemState());
        a = new WrappedLong();
        b = new WrappedLong();
        a.setMutableValue(13000000000L);
        b.setMutableValue(17000000000L);
        interp.set("a", a);
        interp.set("b", b);
    }

    @Test
    void testAdd() {
        interp.exec("c = a + b");
        assertEquals(new PyLong(30000000000L), interp.get("c"));
        b.setMutableValue(18000000000L);
        interp.exec("c = a + b");
        assertEquals(new PyLong(31000000000L), interp.get("c"));
    }

    @Test
    void testMod() {
        interp.exec("c = b % a");
        assertEquals(new PyLong(4000000000L), interp.get("c"));
    }
}
