package org.python.core;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.python.core.JavaLangStringProvider.getSmallOUmlaut;

import org.junit.Test;
import org.python.util.PythonInterpreter;

public class PyUnicodeReplaceTest {

    // variable name
    private static final String RESULT = "result";

    private String replace() {
        StringBuffer b = new StringBuffer();
        b.append("from org.python.core import JavaLangStringProvider\n");
        b.append("result = JavaLangStringProvider.getBeautiful().replace('%s', 'oe')\n");
        return format(b.toString(), getSmallOUmlaut());
    }

    @Test
    public void testReplace() {
        try (PythonInterpreter interpreter = new PythonInterpreter()) {
            interpreter.exec(replace());
            assertResultEquals("schoen", interpreter);
        }
    }

    private void assertResultEquals(String expected, PythonInterpreter interpreter) {
        Object resultObject = interpreter.get(RESULT);
        if (resultObject instanceof PyString) {
            PyString result = (PyString) resultObject;
            assertEquals(expected, result.getString());
        } else {
            fail("expected result to be PyString but was " + resultObject.getClass().getName());
        }
    }

}
