package org.python.util.install;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StartScriptGeneratorTest  {

    private static final String AT_DATE = "@DATE";

    private StartScriptGenerator _generator;

    private File _targetDir;

    @BeforeEach
    void setUp() throws Exception {
        String userDirName = System.getProperty("user.dir"); // only true in eclipse ?
        File userDir = new File(userDirName);
        File parentDir = userDir.getParentFile();
        assertTrue(parentDir.exists());
        _targetDir = new File(parentDir, "jython");
        if (!_targetDir.exists()) {
            _targetDir = new File(parentDir, "jython-trunk");
        }
        assertTrue(_targetDir.exists());
        assertTrue(_targetDir.isDirectory());
        _targetDir = new File(_targetDir, "src");
        _targetDir = new File(_targetDir, "shell");
        assertTrue(_targetDir.exists());
        assertTrue(_targetDir.isDirectory());
        _generator = new StartScriptGenerator(_targetDir, new JavaHomeHandler());
    }

    @Test
    void testUnix() throws IOException {
    }

    @Test
    void testWindows() throws IOException {
    }
}
