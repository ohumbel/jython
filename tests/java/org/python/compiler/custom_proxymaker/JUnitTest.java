package org.python.compiler.custom_proxymaker;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.python.util.ProxyCompiler;

/*
 * This test tests that we can create JUnit Jupiter tests in Python and that JUnit's own reflection system
 * picks our annotations and runs the underlying code
 */
public class JUnitTest {

    @Test
    public void testMethodSignatures() throws Exception {
        ProxyCompiler.compile("tests/python/custom_proxymaker/junit_test.py", "build/testclasses");
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(DiscoverySelectors.selectClass(Class.forName("custom_proxymaker.tests.JUnitTest")))
            .build();
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        TestExecutionSummary summary = listener.getSummary();
        assertEquals(0, summary.getFailures().size(), summary.getFailures().toString());
        assertTrue(summary.getTestsSucceededCount() > 0);
    }
}
