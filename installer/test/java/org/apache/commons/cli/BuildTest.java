/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 * 
 * $Id: BuildTest.java 2662 2006-02-18 14:20:33Z otmarhumbel $
 */

package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BuildTest {

    @Test
    void testSimple() {
        Options opts = new Options();

        opts.addOption("a", false, "toggle -a");

        opts.addOption("b", true, "toggle -b");
    }

    @Test
    void testDuplicateSimple() {
        Options opts = new Options();
        opts.addOption("a", false, "toggle -a");

        opts.addOption("a", true, "toggle -a*");

        assertEquals("toggle -a*", opts.getOption("a").getDescription(), "last one in wins");
    }

    @Test
    void testLong() {
        Options opts = new Options();

        opts.addOption("a", "--a", false, "toggle -a");

        opts.addOption("b", "--b", true, "set -b");

    }

    @Test
    void testDuplicateLong() {
        Options opts = new Options();
        opts.addOption("a", "--a", false, "toggle -a");

        opts.addOption("a", "--a", false, "toggle -a*");
        assertEquals("toggle -a*", opts.getOption("a").getDescription(), "last one in wins");
    }
}
