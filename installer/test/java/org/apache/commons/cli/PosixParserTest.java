package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PosixParserTest  {

    private static final String TEST_SHORT = "t";
    private static final String TEST_LONG = "test";
    private static final String TEST_DESC = "test option";

    private static final String TEST_SHORT_OPTION = "-t";
    private static final String TEST_LONG_OPTION = "--test";
    
    private static final String ARGUMENT = "argument";

    private Options _options;
    private Parser _parser;

    @BeforeEach
    void setUp() {
        _parser = new PosixParser();

        _options = new Options();
        Option testOption = new Option(TEST_SHORT, TEST_LONG, false, TEST_DESC);
        _options.addOption(testOption);
    }

    /**
     * test that an unknown single option and a double hyphen option (with or without argument) are treated the same
     */
    @Test
    void testFlattenStop() {
        boolean stopAtNonOption = true; // means unallowed tokens should not be added
        String[] args;
        String[] expectedFlattened;

        // unknown single dash option
        args = new String[] { "-u" };
        expectedFlattened = new String[0];
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
        args = new String[] { "-u", TEST_SHORT_OPTION };
        expectedFlattened = new String[] { TEST_SHORT_OPTION };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));

        // unknown double dash option
        args = new String[] { "--unknown" };
        expectedFlattened = new String[0];
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
        args = new String[] { "--unknown", TEST_LONG_OPTION };
        expectedFlattened = new String[] { TEST_LONG_OPTION };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));

        // unknown double dash option with argument after =
        args = new String[] { "--unknown=" + ARGUMENT };
        expectedFlattened = new String[0];
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
        args = new String[] { "--unknown="+ARGUMENT, TEST_LONG_OPTION };
        expectedFlattened = new String[] { TEST_LONG_OPTION };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));

        // unknown double dash option with argument after ' '
        args = new String[] { "--unknown", ARGUMENT };
        expectedFlattened = new String[] { ARGUMENT };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
        args = new String[] { "--unknown", ARGUMENT, TEST_LONG_OPTION };
        expectedFlattened = new String[] { ARGUMENT, TEST_LONG_OPTION };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
    }

    /**
     * test that an unknown single option and a double hyphen option (with or without argument) are treated the same
     */
    @Test
    void testFlattenNoStop() {
        boolean stopAtNonOption = false; // means every token should be added
        String[] args;
        String[] expectedFlattened;

        // unknown single dash option
        args = new String[] { "-u" };
        expectedFlattened = new String[] { "-u" };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
        args = new String[] { "-u", TEST_SHORT_OPTION };
        expectedFlattened = new String[] { "-u", TEST_SHORT_OPTION };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));

        // unknown double dash option
        args = new String[] { "--unknown" };
        expectedFlattened = new String[] { "--unknown" };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
        args = new String[] { "--unknown", TEST_LONG_OPTION };
        expectedFlattened = new String[] { "--unknown", TEST_LONG_OPTION };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));

        // unknown double dash option with argument after =
        args = new String[] { "--unknown=" + ARGUMENT };
        expectedFlattened = new String[] { "--unknown", ARGUMENT };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
        args = new String[] { "--unknown="+ ARGUMENT, TEST_LONG_OPTION };
        expectedFlattened = new String[] { "--unknown", ARGUMENT, TEST_LONG_OPTION };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));

        // unknown double dash option with argument after ' '
        args = new String[] { "--unknown", ARGUMENT };
        expectedFlattened = new String[] { "--unknown", ARGUMENT };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
        args = new String[] { "--unknown", ARGUMENT, TEST_LONG_OPTION };
        expectedFlattened = new String[] { "--unknown", ARGUMENT, TEST_LONG_OPTION };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
    }

    /**
     * test that a misspelled long option (-test instead of --test) is not interpreted as -t est
     */
    @Test
    void testMisspelledLongOption() {
        boolean stopAtNonOption = false; // means every token should be added
        String[] args;
        String[] expectedFlattened;

        // unknown single dash long option
        String singleDashLongOption = "-" + TEST_LONG; 
        args = new String[] { singleDashLongOption };
        expectedFlattened = new String[] { singleDashLongOption };
        assertArraysEquals(expectedFlattened, _parser.flatten(_options, args, stopAtNonOption));
    }
    
    //
    // private stuff
    //

    /**
     * Assert that the content of the specified object arrays is equal
     */
    private void assertArraysEquals(Object[] correct, Object[] tested) {
        assertEquals(correct.length, tested.length, "different array lengths");
        for (int i = 0; i < correct.length; i++) {
            assertEquals(correct[i], tested[i], "position " + i + " of array differs");
        }
    }

}
