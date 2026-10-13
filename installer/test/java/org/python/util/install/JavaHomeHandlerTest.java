package org.python.util.install;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JavaHomeHandlerTest  {

    private static final String JAVA_HOME = JavaHomeHandler.JAVA_HOME;

    private static final String JAVA = "java";

    private static final String SOME_WEIRD_HOME = "some/weird/home";

    private String _originalJavaHome;

    @BeforeEach
    void setUp() throws Exception {
        JavaHomeHandler.reset();
        _originalJavaHome = System.getProperty(JAVA_HOME);
    }

    @AfterEach
    void tearDown() throws Exception {
        System.setProperty(JAVA_HOME, _originalJavaHome);
    }

    @Test
    void testGetExecutableName() throws IOException {
        String executable = new JavaHomeHandler().getExecutableName();
        assertNotNull(executable);
        assertTrue(executable.length() > JAVA.length());
        String homePath = createTempHome().getAbsolutePath();
        executable = new JavaHomeHandler(homePath).getExecutableName();
        assertTrue(executable.length() > JAVA.length());
        assertTrue(executable.indexOf(homePath) >= 0);
        System.setProperty(JAVA_HOME, homePath);
        executable = new JavaHomeHandler().getExecutableName();
        assertTrue(executable.length() > JAVA.length());
        assertTrue(executable.indexOf(homePath) >= 0);
    }

    @Test
    void testGetExecutableName_NonExisting() {
        String executable = new JavaHomeHandler(SOME_WEIRD_HOME).getExecutableName();
        assertEquals(JAVA, executable); // fallback
        System.setProperty(JAVA_HOME, SOME_WEIRD_HOME);
        executable = new JavaHomeHandler().getExecutableName();
        assertEquals(JAVA, executable); // fallback
    }

    @Test
    void testCreateJavaHomeHandler() throws IOException {
        JavaHomeHandler handler = new JavaHomeHandler();
        assertNotNull(handler);
        System.setProperty(JAVA_HOME, SOME_WEIRD_HOME);
        handler = new JavaHomeHandler();
        assertNotNull(handler);
        System.setProperty(JAVA_HOME, createTempHome().getAbsolutePath());
        handler = new JavaHomeHandler();
        assertNotNull(handler);
    }

    @Test
    void testCreateHandler_Deviation() throws IOException {
        JavaHomeHandler handler = new JavaHomeHandler(SOME_WEIRD_HOME);
        assertNotNull(handler);
        handler = new JavaHomeHandler(createTempHome().getAbsolutePath());
        assertNotNull(handler);
    }

    @Test
    void testIsDeviation() throws IOException {
        JavaHomeHandler handler = new JavaHomeHandler(createTempHome().getAbsolutePath());
        assertTrue(handler.isDeviation());
        handler = new JavaHomeHandler();
        assertFalse(handler.isDeviation());
        handler = new JavaHomeHandler(System.getProperty(JAVA_HOME));
        assertFalse(handler.isDeviation());
    }

    @Test
    void testGetJavaHome() throws IOException {
        String tempHome = createTempHome().getAbsolutePath();
        JavaHomeHandler handler = new JavaHomeHandler(tempHome);
        String home = handler.getHome().getAbsolutePath();
        assertEquals(tempHome, home);
        try {
            handler = new JavaHomeHandler(SOME_WEIRD_HOME);
        } catch (InstallerException ie) {
            assertEquals("no valid java home", ie.getMessage());
        }
    }

    @Test
    void testIsValidJavaHome() throws IOException {
        JavaHomeHandler handler = new JavaHomeHandler(SOME_WEIRD_HOME);
        assertFalse(handler.isValidHome());
        handler = new JavaHomeHandler();
        assertTrue(handler.isValidHome());
        handler = new JavaHomeHandler(createTempHome().getAbsolutePath());
        assertTrue(handler.isValidHome());
    }

    private File createTempHome() throws IOException {
        File home = File.createTempFile("JavaHomeHandler", "Test");
        assertTrue(FileHelper.createTempDirectory(home));
        File binDir = new File(home, "bin");
        assertTrue(binDir.mkdirs());
        String executableName = JAVA;
        if (Installation.isWindows()) {
            executableName = executableName.concat(".exe");
        }
        File java = new File(binDir, executableName);
        FileHelper.write(java, "dummy");
        assertTrue(java.exists());
        assertTrue(java.isFile());
        return home;
    }
}
