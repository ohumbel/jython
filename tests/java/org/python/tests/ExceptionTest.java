package org.python.tests;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.python.core.Py;
import org.python.util.PythonInterpreter;

class ExceptionTest  {

    public static class Checked extends Exception {}

    public interface Thrower {
        void checked() throws Checked;

        void checkedOrRuntime(boolean checked) throws Checked;
    }

    @BeforeEach
    void setUp() {
        String raiser =
            "from java.lang import Throwable\n" +
            "from org.python.tests.ExceptionTest import Checked, Thrower\n" +
            "class Raiser(Thrower):\n" +
            "    def checked(self):\n" +
            "         raise Checked()\n" +
            "    def checkedOrRuntime(self, checked):\n" +
            "         if checked:\n" +
            "             raise Checked()\n" +
            "         else:\n" +
            "             raise Throwable()\n" +
            "r = Raiser()";
        try (PythonInterpreter interp = new PythonInterpreter()) {
            interp.exec(raiser);
            t = Py.tojava(interp.get("r"), Thrower.class);
        }
    }

    @Test
    void testRaisingCheckedException() {
        try {
            t.checked();
            fail("Calling checked should raise Checked!");
        } catch (Checked c) {
            // All is as it should be.
        }
        try {
            t.checkedOrRuntime(true);
            fail("Calling checkedOrRuntime(true) should raise Checked!");
        } catch (Checked c) {
            // All is as it should be.
        }
    }

    @Test
    void testRaisingRuntimeException() {
        try {
            t.checkedOrRuntime(false);
            fail("Calling checkedOrRuntime(false) should raise Throwable!");
        } catch (Checked c) {
            fail("Calling checkedOrRuntime(false) should raise Throwable!");
        } catch (Throwable t) {
            // All is as it should be.
        }
    }

    protected Thrower t;


}
