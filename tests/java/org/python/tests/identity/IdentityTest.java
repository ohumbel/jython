package org.python.tests.identity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.python.core.PyStringMap;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

class IdentityTest  {

    private PythonInterpreter interp;

    @BeforeEach
    protected void setUp() throws Exception {
        PySystemState sys = new PySystemState();
        interp = new PythonInterpreter(new PyStringMap(), sys);
    }

    @Test
    void testReadonly() {
        interp.execfile("tests/python/identity_test.py");
    }
}
