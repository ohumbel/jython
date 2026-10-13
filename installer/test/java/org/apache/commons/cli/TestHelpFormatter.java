/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 * 
 * $Id: TestHelpFormatter.java 3134 2007-03-02 07:20:08Z otmarhumbel $
 */
package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.junit.jupiter.api.Test;

/**
 * Test case for the HelpFormatter class
 *
 * @author Slawek Zachcial
 * @author John Keyes ( john at integralsource.com )
 **/
class TestHelpFormatter {

    @Test
    void testFindWrapPos() throws Exception {
        HelpFormatter hf = new HelpFormatter();

        String text = "This is a test.";
        // text width should be max 8; the wrap postition is 7
        assertEquals(7, hf.findWrapPos(text, 8, 0), "wrap position");
        // starting from 8 must give -1 - the wrap pos is after end
        assertEquals(-1, hf.findWrapPos(text, 8, 8), "wrap position 2");
        // if there is no a good position before width to make a wrapping look for the next one
        text = "aaaa aa";
        assertEquals( 4, hf.findWrapPos(text, 3, 0),"wrap position 3");
    }

    @Test
    void testPrintWrapped() throws Exception {
        StringBuffer sb = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();

        String text = "This is a test.";
        String expected;

        expected = "This is a" + hf.defaultNewLine + "test.";
        hf.renderWrappedText(sb, 12, 0, text);
        assertEquals(expected, sb.toString(), "single line text");

        sb.setLength(0);
        expected = "This is a" + hf.defaultNewLine + "    test.";
        hf.renderWrappedText(sb, 12, 4, text);
        assertEquals(expected, sb.toString(), "single line padded text");

        text = "aaaa aaaa aaaa" + hf.defaultNewLine + "aaaaaa" + hf.defaultNewLine + "aaaaa";

        expected = text;
        sb.setLength(0);
        hf.renderWrappedText(sb, 16, 0, text);
        assertEquals(expected, sb.toString(), "multi line text");

        expected = "aaaa aaaa aaaa" + hf.defaultNewLine + "    aaaaaa" + hf.defaultNewLine + "    aaaaa";
        sb.setLength(0);
        hf.renderWrappedText(sb, 16, 4, text);
        assertEquals(expected, sb.toString(), "multi-line padded text");
    }

    @Test
    void testPrintOptions() throws Exception {
        StringBuffer sb = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();
        final int leftPad = 1;
        final int descPad = 3;
        final String lpad = hf.createPadding(leftPad);
        final String dpad = hf.createPadding(descPad);
        Options options = null;
        String expected = null;

        options = new Options().addOption("a", false, "aaaa aaaa aaaa aaaa aaaa");
        expected = lpad + "-a" + dpad + "aaaa aaaa aaaa aaaa aaaa";
        hf.renderOptions(sb, 60, options, leftPad, descPad);
        assertEquals(expected, sb.toString(), "simple non-wrapped option");

        int nextLineTabStop = leftPad + descPad + "-a".length();
        expected = lpad + "-a" + dpad + "aaaa aaaa aaaa" + hf.defaultNewLine + hf.createPadding(nextLineTabStop)
                        + "aaaa aaaa";
        sb.setLength(0);
        hf.renderOptions(sb, nextLineTabStop + 17, options, leftPad, descPad);
        assertEquals(expected, sb.toString(), "simple wrapped option");

        options = new Options().addOption("a", "aaa", false, "dddd dddd dddd dddd");
        expected = lpad + "-a,--aaa" + dpad + "dddd dddd dddd dddd";
        sb.setLength(0);
        hf.renderOptions(sb, 60, options, leftPad, descPad);
        assertEquals(expected, sb.toString(), "long non-wrapped option");

        nextLineTabStop = leftPad + descPad + "-a,--aaa".length();
        expected = lpad + "-a,--aaa" + dpad + "dddd dddd" + hf.defaultNewLine + hf.createPadding(nextLineTabStop)
                        + "dddd dddd";
        sb.setLength(0);
        hf.renderOptions(sb, 25, options, leftPad, descPad);
        assertEquals(expected, sb.toString(), "long wrapped option");

        options = new Options().addOption("a", "aaa", false, "dddd dddd dddd dddd").addOption("b", false,
                        "feeee eeee eeee eeee");
        expected = lpad + "-a,--aaa" + dpad + "dddd dddd" + hf.defaultNewLine + hf.createPadding(nextLineTabStop)
                        + "dddd dddd" + hf.defaultNewLine + lpad + "-b      " + dpad + "feeee eeee" + hf.defaultNewLine
                        + hf.createPadding(nextLineTabStop) + "eeee eeee";
        sb.setLength(0);
        hf.renderOptions(sb, 25, options, leftPad, descPad);
        assertEquals(expected, sb.toString(), "multiple wrapped options");
    }

    @Test
    void testAutomaticUsage() throws Exception {
        HelpFormatter hf = new HelpFormatter();
        Options options = null;
        String expected = "usage: app [-a]";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(out);

        options = new Options().addOption("a", false, "aaaa aaaa aaaa aaaa aaaa");
        hf.printUsage(pw, 60, "app", options);
        pw.flush();
        assertEquals(expected, out.toString().trim(), "simple auto usage");
        out.reset();

        expected = "usage: app [-a] [-b]";
        options = new Options().addOption("a", false, "aaaa aaaa aaaa aaaa aaaa").addOption("b", false, "bbb");
        hf.printUsage(pw, 60, "app", options);
        pw.flush();
        assertEquals(expected, out.toString().trim(), "simple auto usage");
        out.reset();
    }
}
