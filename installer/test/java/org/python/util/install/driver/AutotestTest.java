package org.python.util.install.driver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.python.util.install.InstallerCommandLine;

class AutotestTest  {

    private Autotest _autotest;

    @BeforeEach
    void setUp() throws Exception {
        InstallerCommandLine commandLine = new InstallerCommandLine();
        commandLine.setArgs(new String[0]);
        _autotest = new SilentAutotest(commandLine);
    }

    @Test
    void testCreateDirectories() {
        File rootDir = Autotest.getRootDir();
        File targetDir = _autotest.getTargetDir();
        assertNotNull(rootDir);
        verifyDir(rootDir, false);
        assertNotNull(targetDir);
        verifyDir(targetDir, true);
        assertEquals(rootDir, targetDir.getParentFile());
    }

    @Test
    void testCommandLineArgs() {
        String[] args = new String[] { "-x", "-y", "-z" };
        _autotest.setCommandLineArgs(args);
        int len = _autotest.getCommandLineArgs().length;
        assertEquals(args.length, len);
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], _autotest.getCommandLineArgs()[i]);
        }
    }

    @Test
    void testAddArgument() {
        String[] args = new String[] { "-x", "-y", "-z" };
        _autotest.setCommandLineArgs(args);
        _autotest.addArgument("-u");
        assertEquals(args.length + 1, _autotest.getCommandLineArgs().length);
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], _autotest.getCommandLineArgs()[i]);
        }
        assertEquals("-u", _autotest.getCommandLineArgs()[args.length]);
    }

    @Test
    void testVerify() throws Exception {
        TestVerifier testVerifier = new TestVerifier();
        _autotest.setVerifier(testVerifier);
        assertNotNull(_autotest.getVerifier());
        assertNotNull(_autotest.getVerifier().getTargetDir());
        assertEquals(_autotest.getTargetDir(), testVerifier.getTargetDir());
        try {
            _autotest.getVerifier().verify();
            fail("should have thrown");
        } catch (DriverException de) {
        }

    }

    private void verifyDir(File dir, boolean ensureEmpty) {
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
        if (ensureEmpty) {
            assertTrue(dir.listFiles().length <= 0);
        }
    }

    private static class TestVerifier extends NormalVerifier {
        public void verify() throws DriverException {
            throw new DriverException("test verification failure");
        }
    }

}
