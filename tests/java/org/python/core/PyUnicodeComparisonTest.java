package org.python.core;

import static java.lang.String.format;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.python.core.JavaLangStringProvider.getBeautiful;

import org.junit.Test;
import org.python.util.PythonInterpreter;

public class PyUnicodeComparisonTest {

    // variable name
    private static final String RESULT = "result";

    private String compare() {
        StringBuffer b = new StringBuffer();
        b.append("from org.python.core import JavaLangStringProvider\n");
        b.append("value = JavaLangStringProvider.getBeautiful()\n");
        b.append("result = False\n");
        b.append("if value == '%s':\n");
        b.append("  result = True\n");
        return format(b.toString(), getBeautiful());
    }


    @Test
    public void testCompare() {
        try (PythonInterpreter interpreter = new PythonInterpreter()) {
            interpreter.exec(compare());
            assertResultBoolean(true, interpreter);
        }
    }

    private String compareConstructed() {
        StringBuffer b = new StringBuffer();
        b.append("from org.python.core import JavaLangStringConstructor\n");
        b.append("value = JavaLangStringConstructor('%s').getConstructedValue()\n");
        b.append("result = False\n");
        b.append("if value == '%s':\n");
        b.append("  result = True\n");
        return format(b.toString(), getBeautiful(), getBeautiful());
    }

    @Test
    public void testCompareConstructed() {
        try (PythonInterpreter interpreter = new PythonInterpreter()) {
            interpreter.exec(compareConstructed());
            assertResultBoolean(true, interpreter);
        }
    }


    private void assertResultBoolean(boolean expected, PythonInterpreter interpreter) {
        Object resultObject = interpreter.get(RESULT);
        if (resultObject instanceof PyBoolean) {
            PyBoolean result = (PyBoolean) resultObject;
            if (expected) {
                assertTrue("expected result to be True, but was False", result.getBooleanValue());
            } else {
                assertFalse("expected result to be False, but was True", result.getBooleanValue());
            }
        } else {
            fail("expected result to be PyBoolean but was " + resultObject.getClass().getName());
        }
    }

}
