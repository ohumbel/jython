package org.python.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.python.util.PythonInterpreter;

public class WrappedBooleanTest  {

    // Simulate the use case where you want to expose some (possibly mutable)
    // java boolean field to an interpreter without having to set the value to a
    // new PyBoolean each time it changes.
    @SuppressWarnings("serial")
    static class WrappedBoolean extends PyBoolean {
        public WrappedBoolean() {
            super(true);
        }

        private boolean mutableValue;

        @Override
        public boolean getBooleanValue() {
            return mutableValue;
        }

        public void setMutableValue(final boolean newValue) {
            mutableValue = newValue;
        }
    }

    private PythonInterpreter interp;
    private WrappedBoolean a, b;

    @BeforeEach
    protected void setUp() throws Exception {
        interp = new PythonInterpreter(new PyStringMap(), new PySystemState());
        a = new WrappedBoolean();
        b = new WrappedBoolean();
        a.setMutableValue(true);
        b.setMutableValue(false);
        interp.set("a", a);
        interp.set("b", b);
    }

    @Test
    public void testAnd() {
        interp.exec("c = a and b");
        assertEquals(new PyBoolean(false), interp.get("c"));
        b.setMutableValue(true);
        interp.exec("c = a and b");
        assertEquals(new PyBoolean(true), interp.get("c"));
    }

    @Test
    public void testOr() {
        interp.exec("c = a or b");
        assertEquals(new PyBoolean(true), interp.get("c"));
        a.setMutableValue(false);
        interp.exec("c = a or b");
        assertEquals(new PyBoolean(false), interp.get("c"));
    }
}
