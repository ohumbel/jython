package org.python.util.install.driver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.python.util.install.FileHelper;

class NormalVerifierTest  {

    private NormalVerifier _verifier;

    @BeforeEach
    void setUp() throws Exception {
        _verifier = new NormalVerifier();
        // use a directory containing spaces as target directory
        File targetDir = createTargetDirectory();
        assertTrue(targetDir.exists());
        assertTrue(targetDir.isDirectory());
        _verifier.setTargetDir(targetDir);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (_verifier.getTargetDir() != null) {
            File autotestFile = new File(_verifier.getTargetDir().getCanonicalPath(),
                                         NormalVerifier.AUTOTEST_PY);
            if (autotestFile.exists()) {
                assertTrue(autotestFile.delete());
            }
        }
    }

    // have to install jython first in order to activate this test
    @Test
    void testVerify() throws Exception {}

    @Test
    void testGetSimpleCommand() throws Exception {
        String prefix = _verifier.getTargetDir().toString().concat(File.separator);
        String expectedCommand = "bin/jython";
        String expectedArgument = prefix.concat("autotest.py");
        String[] command = _verifier.getSimpleCommand();
        assertNotNull(command);
        assertEquals(2, command.length);
        assertEquals(expectedCommand, command[0]);
        assertEquals(expectedArgument, command[1]);
    }

    @Test
    void testDoShellScriptTests() {
        assertTrue(_verifier.doShellScriptTests());
    }

    @Test
    void testGetShellScriptTestCommandDir() throws DriverException, IOException {
        String expectedDir = _verifier.getTargetDir()
                .getCanonicalPath()
                .concat(File.separator)
                .concat("bin");
        assertEquals(expectedDir, _verifier.getShellScriptTestCommandDir().getCanonicalPath());
    }


    private File createTargetDirectory() throws IOException {
        File tmpFile = File.createTempFile("NormalVerifierTest_", "with spaces");
        FileHelper.createTempDirectory(tmpFile);
        return tmpFile;
    }
}
