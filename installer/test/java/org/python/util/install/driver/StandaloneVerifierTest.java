package org.python.util.install.driver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.python.util.install.Installation;
import org.python.util.install.JarInstaller;

class StandaloneVerifierTest  {

    private StandaloneVerifier _verifier;

    @BeforeEach
    void setUp() throws Exception {
        _verifier = new StandaloneVerifier();
        File targetDir = null;
        // have to install jython first in order to activate this test
        // targetDir = new File("C:/Temp/jython.autoinstall.root_54159_dir/006
        // consoleTest_54165_dir");
        _verifier.setTargetDir(targetDir);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (_verifier.getTargetDir() != null) {
            File autotestFile = new File(_verifier.getTargetDir().getCanonicalPath(),
                                         StandaloneVerifier.AUTOTEST_PY);
            if (autotestFile.exists()) {
                assertTrue(autotestFile.delete());
            }
        }
    }

    @Test
    void testVerify() throws Exception {
        if (_verifier.getTargetDir() != null) {
            _verifier.verify();
        }
    }

    @Test
    void testGetSimpleCommand() throws Exception {
        File javaHome = new File(System.getProperty("java.home"));
        assertNotNull(javaHome);
        assertTrue(javaHome.exists());
        File targetDir = new File(System.getProperty(("user.dir"))); // any existing dir
        assertNotNull(targetDir);
        assertTrue(targetDir.exists());
        String prefix = targetDir.getCanonicalPath().concat(File.separator);
        String expectedCommand = javaHome.getCanonicalPath()
                .concat(File.separator)
                .concat("bin")
                .concat(File.separator)
                .concat("java");
        if (Installation.isWindows()) {
            expectedCommand = expectedCommand.concat(".exe");
        }
        String expectedArgument = prefix.concat("autotest.py");
        _verifier.setTargetDir(targetDir);
        String[] command = _verifier.getSimpleCommand();
        assertNotNull(command);
        assertEquals(4, command.length);
        assertEquals(expectedCommand, command[0]);
        assertEquals("-jar", command[1]);
        assertEquals(prefix.concat(JarInstaller.JYTHON_JAR), command[2]);
        assertEquals(expectedArgument, command[3]);
    }

    @Test
    void testDoShellScriptTests() {
        // we cannot do shell script tests in standalone mode
        assertFalse(_verifier.doShellScriptTests());
    }
}
