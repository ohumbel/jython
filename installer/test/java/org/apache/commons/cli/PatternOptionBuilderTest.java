/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 * 
 * $Id: PatternOptionBuilderTest.java 3134 2007-03-02 07:20:08Z otmarhumbel $
 */
package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

/** 
 * Test case for the PatternOptionBuilder class 
 *
 * @author Henri Yandell
 **/
class PatternOptionBuilderTest {

    @SuppressWarnings("rawtypes")
    @Test
    void testSimplePattern() {
        try {
            Options options = PatternOptionBuilder.parsePattern("a:b@cde>f+n%t/");
            String[] args = new String[] { "-c", "-a", "foo", "-b", "java.util.Vector", "-e", "build.xml", "-f",
                    "java.util.Calendar", "-n", "4.5", "-t", "http://jakarta.apache.org/" };

            CommandLineParser parser = new PosixParser();
            CommandLine line = parser.parse(options, args);

            // tests the char methods of CommandLine that delegate to
            // the String methods
            assertEquals("foo", line.getOptionValue("a"), "flag a");
            assertEquals("foo", line.getOptionValue('a'), "flag a");
            assertEquals("foo", line.getOptionObject("a"), "string flag a");
            assertEquals("foo", line.getOptionObject('a'), "string flag a");
            assertEquals(new java.util.Vector(), line.getOptionObject("b"), "object flag b");
            assertEquals(new java.util.Vector(), line.getOptionObject('b'), "object flag b");
            assertEquals(true, line.hasOption("c"), "boolean true flag c");
            assertEquals(true, line.hasOption('c'), "boolean true flag c");
            assertEquals(false, line.hasOption("d"), "boolean false flag d");
            assertEquals(false, line.hasOption('d'), "boolean false flag d");
            assertEquals(new java.io.File("build.xml"), line.getOptionObject("e"), "file flag e");
            assertEquals(new java.io.File("build.xml"), line.getOptionObject('e'), "file flag e");
            assertEquals(java.util.Calendar.class, line.getOptionObject("f"), "class flag f");
            assertEquals(java.util.Calendar.class, line.getOptionObject('f'), "class flag f");
            assertEquals(new BigDecimal("4.5"), line.getOptionObject("n"), "number flag n");
            assertEquals(new BigDecimal("4.5"), line.getOptionObject('n'), "number flag n");
            assertEquals(java.net.URL.of(new java.net.URI("http://jakarta.apache.org/"), null),
                            line.getOptionObject("t"), "url flag t");
            assertEquals(java.net.URL.of(new java.net.URI("http://jakarta.apache.org/"), null),
                            line.getOptionObject('t'), "url flag t");
            /// DATES NOT SUPPORTED YET.
            // assertEquals("number flag t", new java.util.Date(1023400137276L), line.getOptionObject('z'));
            // input is: "Thu Jun 06 17:48:57 EDT 2002"
        } catch (ParseException exp) {
            fail(exp.getMessage());
        } catch (java.net.MalformedURLException exp) {
            fail(exp.getMessage());
        } catch (URISyntaxException exp) {
            fail(exp.getMessage());
        }
    }

}
