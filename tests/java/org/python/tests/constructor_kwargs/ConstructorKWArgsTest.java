package org.python.tests.constructor_kwargs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.python.core.PyStringMap;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class ConstructorKWArgsTest  {

    private PythonInterpreter interp;

    @BeforeEach
    protected void setUp() throws Exception {
        PySystemState sys = new PySystemState();
        interp = new PythonInterpreter(new PyStringMap(), sys);
    }

    @Test
    public void testConstructorKWArgs() {
        interp.execfile("tests/python/constructorkwargs_test.py");
    }
}
