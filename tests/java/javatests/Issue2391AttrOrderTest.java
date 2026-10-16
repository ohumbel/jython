package javatests;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.python.util.PythonInterpreter;

public class Issue2391AttrOrderTest {

    @Test
    public void testAttribute() {
        try (PythonInterpreter interpreter = new PythonInterpreter()) {
            interpreter.exec("from " + getClass().getPackage().getName() + " import DoubleHolder");
            interpreter.exec("d = DoubleHolder()");
            assertEquals("0.0", interpreter.eval("d.number").toString());
            interpreter.exec("d.number = 3.0");
            assertEquals("3.0", interpreter.eval("d.number").toString());
        }
    }
}
