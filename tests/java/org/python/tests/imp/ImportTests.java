package org.python.tests.imp;

import org.junit.jupiter.api.Test;

import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.core.imp;

public class ImportTests  {

    @Test
    public void testImportFromJava() {
        PySystemState.initialize();
        PyObject submodule = imp.load("testpkg.submodule");
        PyObject module = imp.load("testpkg");
        module.__getattr__("test").__call__();
    }
}
