package javatests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;
import org.python.core.PyStringMap;
import org.python.core.PySystemState;
import org.python.util.InteractiveInterpreter;

class Issue2007Test {

    /**
     * Verify that {@code print_function} works in scripts. This was always the case.
     */
    @Test
    void testPrintFunctionInScript() {
        try (CapturingInterpreter interp = new CapturingInterpreter()) {
            interp.exec("from __future__ import print_function\n" + "print(1, 2, 3)\n");
            assertEquals("1 2 3\n", interp.getOutput());
        }
    }

    /**
     * Verify that print_function works when passing individual statements to an interactive
     * interpreter. If issue 2007 is present, this will fail as the second statement will print
     * {@code '(1, 2, 3)'} instead of {@code '1 2 3'}.
     */
    @Test
    void testPrintFunctionInInteractive() {
        try (CapturingInterpreter interp = new CapturingInterpreter()) {
            interp.runsource("from __future__ import print_function");
            interp.runsource("print(1, 2, 3)");
            assertEquals("1 2 3\n", interp.getOutput());
        }
    }

    private static class CapturingInterpreter extends InteractiveInterpreter {

        private final StringWriter writer = new StringWriter();

        CapturingInterpreter() {
            super(new PyStringMap(), new PySystemState());
            setOut(writer);
        }

        String getOutput() {
            close();
            return writer.toString();
        }
    }
}
