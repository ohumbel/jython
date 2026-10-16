package org.python.tests.props;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;



import org.python.core.PyStringMap;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class BeanPropertyTest  {

    private PythonInterpreter interp;

    @BeforeEach
    protected void setUp() throws Exception {
        interp = new PythonInterpreter(new PyStringMap(), new PySystemState());
    }

    @Test
    public void testReadonly() {
        //This used to cause an NPE see http://bugs.jython.org/issue1295 
        interp.exec("from org.python.tests.props import Readonly;Readonly().a = 'test'");
    }

    @Test
    public void testShadowing() {
        interp.execfile("tests/python/prop_test.py");
    }

}
