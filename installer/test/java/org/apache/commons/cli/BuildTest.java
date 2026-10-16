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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BuildTest 
{

    @BeforeEach
    public void setUp()
    {
    }

    @AfterEach
    public void tearDown()
    {
    }

    @Test
    public void testSimple()
    {
        Options opts = new Options();
        
        opts.addOption("a",
                       false,
                       "toggle -a");

        opts.addOption("b",
                       true,
                       "toggle -b");
    }

    @Test
    public void testDuplicateSimple()
    {
        Options opts = new Options();
        opts.addOption("a",
                       false,
                       "toggle -a");

        opts.addOption("a",
                       true,
                       "toggle -a*");
        
        assertEquals( "last one in wins", "toggle -a*", opts.getOption("a").getDescription() );
    }

    @Test
    public void testLong()
    {
        Options opts = new Options();
        
        opts.addOption("a",
                       "--a",
                       false,
                       "toggle -a");

        opts.addOption("b",
                       "--b",
                       true,
                       "set -b");

    }

    @Test
    public void testDuplicateLong()
    {
        Options opts = new Options();
        opts.addOption("a",
                       "--a",
                       false,
                       "toggle -a");

        opts.addOption("a",
                       "--a",
                       false,
                       "toggle -a*");
        assertEquals( "last one in wins", "toggle -a*", opts.getOption("a").getDescription() );
    }
}
