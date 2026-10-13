/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 * 
 * $Id: ValuesTest.java 3134 2007-03-02 07:20:08Z otmarhumbel $
 */

package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValuesTest {
    /** CommandLine instance */
    private CommandLine _cmdline = null;
    private Option _option = null;

    @BeforeEach
    void setUp() {
        Options opts = new Options();

        opts.addOption("a", false, "toggle -a");

        opts.addOption("b", true, "set -b");

        opts.addOption("c", "c", false, "toggle -c");

        opts.addOption("d", "d", true, "set -d");

        OptionBuilder.withLongOpt("e");
        OptionBuilder.hasArgs();
        OptionBuilder.withDescription("set -e ");
        opts.addOption(OptionBuilder.create('e'));

        opts.addOption("f", "f", false, "jk");

        OptionBuilder.withLongOpt("g");
        OptionBuilder.hasArgs(2);
        OptionBuilder.withDescription("set -g");
        opts.addOption(OptionBuilder.create('g'));

        OptionBuilder.withLongOpt("h");
        OptionBuilder.hasArgs(2);
        OptionBuilder.withDescription("set -h");
        opts.addOption(OptionBuilder.create('h'));

        OptionBuilder.withLongOpt("i");
        OptionBuilder.withDescription("set -i");
        opts.addOption(OptionBuilder.create('i'));

        OptionBuilder.withLongOpt("j");
        OptionBuilder.hasArgs();
        OptionBuilder.withDescription("set -j");
        OptionBuilder.withValueSeparator('=');
        opts.addOption(OptionBuilder.create('j'));

        OptionBuilder.withLongOpt("k");
        OptionBuilder.hasArgs();
        OptionBuilder.withDescription("set -k");
        OptionBuilder.withValueSeparator('=');
        opts.addOption(OptionBuilder.create('k'));

        OptionBuilder.withLongOpt("m");
        OptionBuilder.hasArgs();
        OptionBuilder.withDescription("set -m");
        OptionBuilder.withValueSeparator();
        _option = OptionBuilder.create('m');

        opts.addOption(_option);

        String[] args = new String[] { "-a", "-b", "foo", "--c", "--d", "bar", "-e", "one", "two", "-f", "arg1", "arg2",
                "-g", "val1", "val2", "arg3", "-h", "val1", "-i", "-h", "val2", "-jkey=value", "-j", "key=value",
                "-kkey1=value1", "-kkey2=value2", "-mkey=value" };

        CommandLineParser parser = new PosixParser();

        try {
            _cmdline = parser.parse(opts, args);
        } catch (ParseException e) {
            fail("Cannot setUp() CommandLine: " + e.toString());
        }
    }

    @Test
    void testShortArgs() {
        assertTrue(_cmdline.hasOption("a"));
        assertTrue(_cmdline.hasOption("c"));

        assertNull(_cmdline.getOptionValues("a"));
        assertNull(_cmdline.getOptionValues("c"));
    }

    @Test
    void testShortArgsWithValue() {
        assertTrue(_cmdline.hasOption("b"));
        assertTrue(_cmdline.getOptionValue("b").equals("foo"));
        assertTrue(_cmdline.getOptionValues("b").length == 1);

        assertTrue(_cmdline.hasOption("d"));
        assertTrue(_cmdline.getOptionValue("d").equals("bar"));
        assertTrue(_cmdline.getOptionValues("d").length == 1);
    }

    @Test
    void testMultipleArgValues() {
        _cmdline.getOptionValues("e");
        String[] values = new String[] { "one", "two" };
        assertTrue(_cmdline.hasOption("e"));
        assertTrue(_cmdline.getOptionValues("e").length == 2);
        assertTrue(Arrays.equals(values, _cmdline.getOptionValues("e")));
    }

    @Test
    void testTwoArgValues() {
        _cmdline.getOptionValues("g");
        String[] values = new String[] { "val1", "val2" };
        assertTrue(_cmdline.hasOption("g"));
        assertTrue(_cmdline.getOptionValues("g").length == 2);
        assertTrue(Arrays.equals(values, _cmdline.getOptionValues("g")));
    }

    @Test
    void testComplexValues() {
        _cmdline.getOptionValues("h");
        String[] values = new String[] { "val1", "val2" };
        assertTrue(_cmdline.hasOption("i"));
        assertTrue(_cmdline.hasOption("h"));
        assertTrue(_cmdline.getOptionValues("h").length == 2);
        assertTrue(Arrays.equals(values, _cmdline.getOptionValues("h")));
    }

    @Test
    void testExtraArgs() {
        String[] args = new String[] { "arg1", "arg2", "arg3" };
        assertTrue(_cmdline.getArgs().length == 3);
        assertTrue(Arrays.equals(args, _cmdline.getArgs()));
    }

    @Test
    void testCharSeparator() {
        // tests the char methods of CommandLine that delegate to
        // the String methods
        String[] values = new String[] { "key", "value", "key", "value" };
        assertTrue(_cmdline.hasOption("j"));
        assertTrue(_cmdline.hasOption('j'));
        assertTrue(_cmdline.getOptionValues("j").length == 4);
        assertTrue(_cmdline.getOptionValues('j').length == 4);
        assertTrue(Arrays.equals(values, _cmdline.getOptionValues("j")));
        assertTrue(Arrays.equals(values, _cmdline.getOptionValues('j')));

        values = new String[] { "key1", "value1", "key2", "value2" };
        assertTrue(_cmdline.hasOption("k"));
        assertTrue(_cmdline.hasOption('k'));
        assertTrue(_cmdline.getOptionValues("k").length == 4);
        assertTrue(_cmdline.getOptionValues('k').length == 4);
        assertTrue(Arrays.equals(values, _cmdline.getOptionValues("k")));
        assertTrue(Arrays.equals(values, _cmdline.getOptionValues('k')));

        values = new String[] { "key", "value" };
        assertTrue(_cmdline.hasOption("m"));
        assertTrue(_cmdline.hasOption('m'));
        assertTrue(_cmdline.getOptionValues("m").length == 2);
        assertTrue(_cmdline.getOptionValues('m').length == 2);
        assertTrue(Arrays.equals(values, _cmdline.getOptionValues("m")));
        assertTrue(Arrays.equals(values, _cmdline.getOptionValues('m')));
    }
}
