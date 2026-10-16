package org.python.util.install.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DrivableConsoleTest  {

    private DrivableConsole _console;
    private Tunnel _tunnel;

    @BeforeEach
    protected void setUp() throws IOException {
        _tunnel = new Tunnel();
        _console = new DrivableConsole(_tunnel);
    }

    @Test
    public void testDrive() throws Exception {
        // sequence matters here (have to fork off the driver thread first
        ConsoleDriver driver = new ConsoleDriver(_tunnel, getAnswers());
        driver.start();
        _console.handleConsoleIO();
    }

    private List<String> getAnswers() {
        List<String> answers = new ArrayList<>();
        answers.add("1");
        answers.add("2");
        answers.add("3");
        return answers;
    }

}
