package org.python.expose.generate;

import org.junit.jupiter.api.BeforeEach;
import org.python.core.PySystemState;
import org.python.core.RegistryKey;

/**
 * Initializes PySystemState in its setUp for use in subclasses.
 */
public abstract class InterpTestCase {

    @BeforeEach
    public void setUp() throws Exception {
        System.setProperty(RegistryKey.PYTHON_CACHEDIR_SKIP, "true");
        PySystemState.initialize();
    }
}
