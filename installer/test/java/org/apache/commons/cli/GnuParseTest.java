/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 * 
 * $Id: GnuParseTest.java 3134 2007-03-02 07:20:08Z otmarhumbel $
 */

package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GnuParseTest 
{
    private Options _options = null;
    private CommandLineParser _parser = null;

    @BeforeEach
    public void setUp()
    {
        _options = new Options()
            .addOption("a",
                       "enable-a",
                       false,
                       "turn [a] on or off")
            .addOption("b",
                       "bfile",
                       true,
                       "set the value of [b]")
            .addOption("c",
                       "copt",
                       false,
                       "turn [c] on or off");

        _parser = new GnuParser( );
    }

    @AfterEach
    public void tearDown()
    {
    }

    @Test
    public void testSimpleShort()
    {
        String[] args = new String[] { "-a",
                                       "-b", "toast",
                                       "foo", "bar" };

        try
        {
            CommandLine cl = _parser.parse(_options, args);

            assertTrue(cl.hasOption("a"), "Confirm -a is set");
            assertTrue(cl.hasOption("b"), "Confirm -b is set");
            assertTrue(cl.getOptionValue("b").equals("toast"), "Confirm arg of -b");
            assertTrue(cl.getArgList().size() == 2, "Confirm size of extra args");
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    public void testSimpleLong()
    {
        String[] args = new String[] { "--enable-a",
                                       "--bfile", "toast",
                                       "foo", "bar" };

        try
        {
            CommandLine cl = _parser.parse(_options, args);
            
            assertTrue(cl.hasOption("a"), "Confirm -a is set");
            assertTrue(cl.hasOption("b"), "Confirm -b is set");
            assertTrue(cl.getOptionValue("b").equals("toast"), "Confirm -b is set");
            assertTrue(cl.getArgList().size() == 2, "Confirm size of extra args");
        } 
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    public void testExtraOption()
    {
        String[] args = new String[] { "-a", "-d", "-b", "toast",
                                       "foo", "bar" };

        boolean caught = false;

        try
        {
            CommandLine cl = _parser.parse(_options, args);
            
            assertTrue(cl.hasOption("a"), "Confirm -a is set");
            assertTrue(cl.hasOption("b"), "Confirm -b is set");
            assertTrue(cl.getOptionValue("b").equals("toast"), "confirm arg of -b");
            assertTrue(cl.getArgList().size() == 3, "Confirm size of extra args");
        }
        catch (UnrecognizedOptionException e)
        {
            caught = true;
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
        assertTrue(caught, "Confirm size of extra args");
    }

    @Test
    public void testMissingArg()
    {

        String[] args = new String[] { "-b" };

        boolean caught = false;

        CommandLine cl = null;
        try
        {
            cl = _parser.parse(_options, args);
        }
        catch (MissingArgumentException e)
        {
            caught = true;
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }

        assertTrue(caught, "Confirm MissingArgumentException caught " + cl);
    }

    @Test
    public void testStop()
    {
        String[] args = new String[] { "-c",
                                       "foober",
                                       "-b",
                                       "toast" };

        try
        {
            CommandLine cl = _parser.parse(_options, args, true);
            assertTrue(cl.hasOption("c"), "Confirm -c is set");
            assertTrue(cl.getArgList().size() == 3, "Confirm  3 extra args: " + cl.getArgList().size());
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    public void testMultiple()
    {
        String[] args = new String[] { "-c",
                                       "foobar",
                                       "-b",
                                       "toast" };

        try
        {
            CommandLine cl = _parser.parse(_options, args, true);
            assertTrue(cl.hasOption("c"), "Confirm -c is set");
            assertTrue(cl.getArgList().size() == 3, "Confirm  3 extra args: " + cl.getArgList().size());

            cl = _parser.parse(_options, cl.getArgs() );

            assertTrue(!cl.hasOption("c"), "Confirm -c is not set");
            assertTrue(cl.hasOption("b"), "Confirm -b is set");
            assertTrue(cl.getOptionValue("b").equals("toast"), "Confirm arg of -b");
            assertTrue(cl.getArgList().size() == 1, "Confirm  1 extra arg: " + cl.getArgList().size());
            assertTrue(cl.getArgList().get(0).equals("foobar"),
                            "Confirm  value of extra arg: " + cl.getArgList().get(0));
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    public void testMultipleWithLong()
    {
        String[] args = new String[] { "--copt",
                                       "foobar",
                                       "--bfile", "toast" };

        try
        {
            CommandLine cl = _parser.parse(_options,args,
                                            true);
            assertTrue(cl.hasOption("c"), "Confirm -c is set");
            assertTrue(cl.getArgList().size() == 3, "Confirm  3 extra args: " + cl.getArgList().size());

            cl = _parser.parse(_options, cl.getArgs() );

            assertTrue(!cl.hasOption("c"), "Confirm -c is not set");
            assertTrue(cl.hasOption("b"), "Confirm -b is set");
            assertTrue(cl.getOptionValue("b").equals("toast"), "Confirm arg of -b");
            assertTrue(cl.getArgList().size() == 1, "Confirm  1 extra arg: " + cl.getArgList().size());
            assertTrue(cl.getArgList().get(0).equals("foobar"),
                            "Confirm  value of extra arg: " + cl.getArgList().get(0));
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    public void testDoubleDash()
    {
        String[] args = new String[] { "--copt",
                                       "--",
                                       "-b", "toast" };

        try
        {
            CommandLine cl = _parser.parse(_options, args);

            assertTrue(cl.hasOption("c"), "Confirm -c is set");
            assertTrue(!cl.hasOption("b"), "Confirm -b is not set");
            assertTrue(cl.getArgList().size() == 2, "Confirm 2 extra args: " + cl.getArgList().size());

        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    public void testSingleDash()
    {
        String[] args = new String[] { "--copt",
                                       "-b", "-",
                                       "-a",
                                       "-" };

        try
        {
            CommandLine cl = _parser.parse(_options, args);

            assertTrue(cl.hasOption("a"), "Confirm -a is set");
            assertTrue(cl.hasOption("b"), "Confirm -b is set");
            assertTrue(cl.getOptionValue("b").equals("-"), "Confirm arg of -b");
            assertTrue(cl.getArgList().size() == 1, "Confirm 1 extra arg: " + cl.getArgList().size());
            assertTrue(cl.getArgList().get(0).equals("-"), "Confirm value of extra arg: " + cl.getArgList().get(0));
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
        
    }
}
