package org.python.util.install.driver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.python.util.install.FileHelper;
import org.python.util.install.Installation;

public class NormalVerifierTest  {

    private NormalVerifier _verifier;

    @BeforeEach
    protected void setUp() throws Exception {
        _verifier = new NormalVerifier();
        // use a directory containing spaces as target directory
        File targetDir = createTargetDirectory();
        assertTrue(targetDir.exists());
        assertTrue(targetDir.isDirectory());
        _verifier.setTargetDir(targetDir);
    }

    @AfterEach
    protected void tearDown() throws Exception {
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
    public void testVerify() throws Exception {}

    @Test
    public void testGetSimpleCommand() throws Exception {
        String prefix = _verifier.getTargetDir().getCanonicalPath().concat(File.separator);
        String expectedCommand = prefix.concat("jython");
        if (Installation.isWindows()) {
            expectedCommand = expectedCommand.concat(".bat");
        }
        String expectedArgument = prefix.concat("autotest.py");
        String[] command = _verifier.getSimpleCommand();
        assertNotNull(command);
        assertEquals(2, command.length);
        assertEquals(expectedCommand, command[0]);
        assertEquals(expectedArgument, command[1]);
    }

    @Test
    public void testDoShellScriptTests() {
        assertTrue(_verifier.doShellScriptTests());
    }

    @Test
    public void testGetShellScriptTestCommandDir() throws DriverException, IOException {
        String expectedDir = _verifier.getTargetDir()
                .getCanonicalPath()
                .concat(File.separator)
                .concat("bin");
        assertEquals(expectedDir, _verifier.getShellScriptTestCommandDir().getCanonicalPath());
    }


    @Test
    public void testGetShellScriptTestCommand() throws Exception {
        String prefix = _verifier.getShellScriptTestCommandDir()
                .getCanonicalPath()
                .concat(File.separator);
        String expectedCommand = prefix.concat("jython_test");
        if (Installation.isWindows()) {
            expectedCommand = expectedCommand.concat(".bat");
        }
        String[] command = _verifier.getSimpleCommand();
        assertNotNull(command);
        assertEquals(1, command.length);
        String commandFileName = command[0];
        assertEquals(expectedCommand, commandFileName);
        File commandFile = new File(commandFileName);
        assertTrue(commandFile.exists());
        String contents = FileHelper.readAll(commandFile);
        assertNotNull(contents);
        assertFalse(contents.length() == 0);
    }

    private File createTargetDirectory() throws IOException {
        File tmpFile = File.createTempFile("NormalVerifierTest_", "with spaces");
        FileHelper.createTempDirectory(tmpFile);
        return tmpFile;
    }
}
