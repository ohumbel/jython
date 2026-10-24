/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 * 
 * $Id: ParseRequiredTest.java 3134 2007-03-02 07:20:08Z otmarhumbel $
 */

package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author John Keyes (john at integralsource.com)
 * @version $Revision: 3134 $
 */
class ParseRequiredTest 
{
    private Options _options = null;
    private CommandLineParser parser = new PosixParser();

    @BeforeEach
    void setUp()
    {
        OptionBuilder.withLongOpt( "bfile" );
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        OptionBuilder.withDescription( "set the value of [b]" );
        Option opt2 = OptionBuilder.create( 'b' );
        _options = new Options()
            .addOption("a",
                       "enable-a",
                       false,
                       "turn [a] on or off")
            .addOption( opt2 );
    }

    @Test
    void testWithRequiredOption()
    {
        String[] args = new String[] {  "-b", "file" };

        try
        {
            CommandLine cl = parser.parse(_options,args);
            
            assertTrue(!cl.hasOption("a"), "Confirm -a is NOT set");
            assertTrue(cl.hasOption("b"), "Confirm -b is set");
            assertTrue(cl.getOptionValue("b").equals("file"), "Confirm arg of -b");
            assertTrue(cl.getArgList().size() == 0, "Confirm NO of extra args");
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    void testOptionAndRequiredOption()
    {
        String[] args = new String[] {  "-a", "-b", "file" };

        try
        {
            CommandLine cl = parser.parse(_options,args);

            assertTrue(cl.hasOption("a"), "Confirm -a is set");
            assertTrue(cl.hasOption("b"), "Confirm -b is set");
            assertTrue(cl.getOptionValue("b").equals("file"), "Confirm arg of -b");
            assertTrue(cl.getArgList().size() == 0, "Confirm NO of extra args");
        }
        catch (ParseException e)
        {
            fail( e.toString() );
        }
    }

    @Test
    void testMissingRequiredOption()
    {
        String[] args = new String[] { "-a" };

        CommandLine cl = null;
        try
        {
            cl = parser.parse(_options,args);
            fail( "exception should have been thrown" );
        }
        catch (ParseException e)
        {
            if( !( e instanceof MissingOptionException ) )
            {
                fail( "expected to catch MissingOptionException in " + cl );
            }
        }
    }

}
