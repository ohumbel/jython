package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpFormatterTest  {

    private Options _options;

    @BeforeEach
    void setUp() {
        _options = new Options();
        Option aOption = new Option("a", "Aa", false, "option A");
        Option bOption = new Option("b", "Bb", false, "option B");
        OptionGroup group1 = new OptionGroup();
        group1.addOption(aOption);
        group1.addOption(bOption);
        _options.addOptionGroup(group1);
    }

    /**
     * the setUp above used to print [-a | -b] [-a] [-b]
     */
    @Test
    void testOptionGroupDuplication() {
        String help = unifyNewLines(getFormattedHelp());
        String expectedHelp = unifyNewLines(new String("usage: syntax [-a | -b]\n-a,--Aa option A\n-b,--Bb option B\n"));
        assertEquals(expectedHelp, help, "expected usage to be '" + expectedHelp + "' instead of '" + help + "'");
    }

    /**
     * Options following an option group used to be non blank separated: [-b | -a][-o] instead of
     * [-b | -a] [-o]
     */
    @Test
    void testOptionGroupSubsequentOptions() {
        _options.addOption(new Option("o", "Option O"));
        String help = getFormattedHelp();
        assertTrue(help.indexOf("][") < 0);
        assertTrue(help.indexOf("[-o] [-a | -b]") >= 0);
    }

    //
    // private methods
    //
    private String getFormattedHelp() {
        HelpFormatter formatter = new HelpFormatter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        formatter.printHelp(pw, 60, "syntax", null, _options, 0, 1, null, true);
        pw.flush();
        String usage = baos.toString();
        return usage;
    }

    /**
     * replace the windows specific \r\n line endings with java like \n line endings
     * 
     * @param in
     *            The string to be transformed
     * @return The string with unified line endings
     */
    private String unifyNewLines(String in) {
        char[] inChars = in.toCharArray();
        StringBuilder b = new StringBuilder(inChars.length);
        for (int i = 0; i < inChars.length; i++) {
            char current = inChars[i];
            if (current == '\r') {
                if (i < inChars.length) {
                    char next = inChars[i + 1];
                    if (next == '\n') {
                        i++;
                        current = next;
                    }
                }
            }
            b.append(current);
        }
        return b.toString();
    }
}
