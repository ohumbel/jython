/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 * 
 * $Id: OptionGroupTest.java 3134 2007-03-02 07:20:08Z otmarhumbel $
 */

package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author John Keyes (john at integralsource.com)
 * @version $Revision: 3134 $
 */
class OptionGroupTest {

    private Options _options = null;
    private CommandLineParser parser = new PosixParser();


    @BeforeEach
    void setUp()
    {
        Option file = new Option( "f", "file", false, "file to process" );
        Option dir = new Option( "d", "directory", false, "directory to process" );
        OptionGroup group = new OptionGroup();
        group.addOption( file );
        group.addOption( dir );
        _options = new Options().addOptionGroup( group );

        Option section = new Option( "s", "section", false, "section to process" );
        Option chapter = new Option( "c", "chapter", false, "chapter to process" );
        OptionGroup group2 = new OptionGroup();
        group2.addOption( section );
        group2.addOption( chapter );

        _options.addOptionGroup( group2 );
        _options.addOption( "r", "revision", false, "revision number" );
    }

    @Test
    void testSingleOptionFromGroup()
    {
        String[] args = new String[] { "-f" };

        try
        {
            CommandLine cl = parser.parse( _options, args);

            assertTrue(!cl.hasOption("r"), "Confirm -r is NOT set");
            assertTrue(cl.hasOption("f"), "Confirm -f is set");
            assertTrue(!cl.hasOption("d"), "Confirm -d is NOT set");
            assertTrue(!cl.hasOption("s"), "Confirm -s is NOT set");
            assertTrue(!cl.hasOption("c"), "Confirm -c is NOT set");
            assertTrue(cl.getArgList().size() == 0, "Confirm no extra args");
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    void testSingleOption()
    {
        String[] args = new String[] { "-r" };

        try
        {
            CommandLine cl = parser.parse( _options, args);

            assertTrue(cl.hasOption("r"), "Confirm -r is set");
            assertTrue(!cl.hasOption("f"), "Confirm -f is NOT set");
            assertTrue(!cl.hasOption("d"), "Confirm -d is NOT set");
            assertTrue(!cl.hasOption("s"), "Confirm -s is NOT set");
            assertTrue(!cl.hasOption("c"), "Confirm -c is NOT set");
            assertTrue(cl.getArgList().size() == 0, "Confirm no extra args");
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    void testTwoValidOptions()
    {
        String[] args = new String[] { "-r", "-f" };

        try
        {
            CommandLine cl = parser.parse( _options, args);

            assertTrue(cl.hasOption("r"), "Confirm -r is set");
            assertTrue(cl.hasOption("f"), "Confirm -f is set");
            assertTrue(!cl.hasOption("d"), "Confirm -d is NOT set");
            assertTrue(!cl.hasOption("s"), "Confirm -s is NOT set");
            assertTrue(!cl.hasOption("c"), "Confirm -c is NOT set");
            assertTrue(cl.getArgList().size() == 0, "Confirm no extra args");
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    void testSingleLongOption()
    {
        String[] args = new String[] { "--file" };

        try
        {
            CommandLine cl = parser.parse( _options, args);

            assertTrue(!cl.hasOption("r"), "Confirm -r is NOT set");
            assertTrue(cl.hasOption("f"), "Confirm -f is set");
            assertTrue(!cl.hasOption("d"), "Confirm -d is NOT set");
            assertTrue(!cl.hasOption("s"), "Confirm -s is NOT set");
            assertTrue(!cl.hasOption("c"), "Confirm -c is NOT set");
            assertTrue(cl.getArgList().size() == 0, "Confirm no extra args");
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    void testTwoValidLongOptions()
    {
        String[] args = new String[] { "--revision", "--file" };

        try
        {
            CommandLine cl = parser.parse( _options, args);

            assertTrue(cl.hasOption("r"), "Confirm -r is set");
            assertTrue(cl.hasOption("f"), "Confirm -f is set");
            assertTrue(!cl.hasOption("d"), "Confirm -d is NOT set");
            assertTrue(!cl.hasOption("s"), "Confirm -s is NOT set");
            assertTrue(!cl.hasOption("c"), "Confirm -c is NOT set");
            assertTrue(cl.getArgList().size() == 0, "Confirm no extra args");
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    void testNoOptionsExtraArgs()
    {
        String[] args = new String[] { "arg1", "arg2" };

        try
        {
            CommandLine cl = parser.parse( _options, args);

            assertTrue(!cl.hasOption("r"), "Confirm -r is NOT set");
            assertTrue(!cl.hasOption("f"), "Confirm -f is NOT set");
            assertTrue(!cl.hasOption("d"), "Confirm -d is NOT set");
            assertTrue(!cl.hasOption("s"), "Confirm -s is NOT set");
            assertTrue(!cl.hasOption("c"), "Confirm -c is NOT set");
            assertTrue(cl.getArgList().size() == 2, "Confirm TWO extra args");
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    void testTwoOptionsFromGroup()
    {
        String[] args = new String[] { "-f", "-d" };

        CommandLine cl = null;
        try
        {
            cl = parser.parse( _options, args);
            fail( "two arguments from group not allowed" );
        }
        catch (ParseException e)
        {
            if( !( e instanceof AlreadySelectedException ) )
            {
                fail( "incorrect exception caught:" + e.getMessage() + " in " + cl );
            }
        }
    }

    @Test
    void testTwoLongOptionsFromGroup()
    {
        String[] args = new String[] { "--file", "--directory" };

        CommandLine cl = null;
        try
        {
            cl = parser.parse( _options, args);
            fail( "two arguments from group not allowed" );
        }
        catch (ParseException e)
        {
            if( !( e instanceof AlreadySelectedException ) )
            {
                fail( "incorrect exception caught:" + e.getMessage() + " in " + cl );
            }
        }
    }

    @Test
    void testTwoOptionsFromDifferentGroup()
    {
        String[] args = new String[] { "-f", "-s" };

        try
        {
            CommandLine cl = parser.parse( _options, args);
            assertTrue(!cl.hasOption("r"), "Confirm -r is NOT set");
            assertTrue(cl.hasOption("f"), "Confirm -f is set");
            assertTrue(!cl.hasOption("d"), "Confirm -d is NOT set");
            assertTrue(cl.hasOption("s"), "Confirm -s is set");
            assertTrue(!cl.hasOption("c"), "Confirm -c is NOT set");
            assertTrue(cl.getArgList().size() == 0, "Confirm NO extra args");
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }


}
