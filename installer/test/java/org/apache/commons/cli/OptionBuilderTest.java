package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class OptionBuilderTest  {

    @Test
    void testCompleteOption( ) {
        OptionBuilder.withLongOpt( "simple option");
        OptionBuilder.hasArg( );
        OptionBuilder.isRequired( );
        OptionBuilder.hasArgs( );
        OptionBuilder.withType( new BigDecimal( "10" ) );
        OptionBuilder.withDescription( "this is a simple option" );
        Option simple = OptionBuilder.create( 's' );

        assertEquals( "s", simple.getOpt() );
        assertEquals( "simple option", simple.getLongOpt() );
        assertEquals( "this is a simple option", simple.getDescription() );
        assertEquals( simple.getType().getClass(), BigDecimal.class );
        assertTrue( simple.hasArg() );
        assertTrue( simple.isRequired() );
        assertTrue( simple.hasArgs() );
    }

    @Test
    void testTwoCompleteOptions( ) {
        OptionBuilder.withLongOpt( "simple option");
        OptionBuilder.hasArg( );
        OptionBuilder.isRequired( );
        OptionBuilder.hasArgs( );
        OptionBuilder.withType( new BigDecimal( "10" ) );
        OptionBuilder.withDescription( "this is a simple option" );
        Option simple = OptionBuilder.create( 's' );

        assertEquals( "s", simple.getOpt() );
        assertEquals( "simple option", simple.getLongOpt() );
        assertEquals( "this is a simple option", simple.getDescription() );
        assertEquals( simple.getType().getClass(), BigDecimal.class );
        assertTrue( simple.hasArg() );
        assertTrue( simple.isRequired() );
        assertTrue( simple.hasArgs() );

        OptionBuilder.withLongOpt( "dimple option");
        OptionBuilder.hasArg( );
        OptionBuilder.withDescription( "this is a dimple option" );
        simple = OptionBuilder.create( 'd' );

        assertEquals( "d", simple.getOpt() );
        assertEquals( "dimple option", simple.getLongOpt() );
        assertEquals( "this is a dimple option", simple.getDescription() );
        assertNull( simple.getType() );
        assertTrue( simple.hasArg() );
        assertTrue( !simple.isRequired() );
        assertTrue( !simple.hasArgs() );
    }

    @Test
    void testBaseOptionCharOpt() {
        OptionBuilder.withDescription( "option description");
        Option base = OptionBuilder.create( 'o' );

        assertEquals( "o", base.getOpt() );
        assertEquals( "option description", base.getDescription() );
        assertTrue( !base.hasArg() );
    }

    @Test
    void testBaseOptionStringOpt() {
        OptionBuilder.withDescription( "option description");
        Option base = OptionBuilder.create( "o" );

        assertEquals( "o", base.getOpt() );
        assertEquals( "option description", base.getDescription() );
        assertTrue( !base.hasArg() );
    }

    @Test
    void testSpecialOptChars() {

        // '?'
        try {
            OptionBuilder.withDescription( "help options" );
            Option opt = OptionBuilder.create( '?' );
            assertEquals( "?", opt.getOpt() );
        }
        catch( IllegalArgumentException arg ) {
            fail( "IllegalArgumentException caught" );
        }

        // '@'
        try {
            OptionBuilder.withDescription( "read from stdin" );
            Option opt = OptionBuilder.create( '@' );
            assertEquals( "@", opt.getOpt() );
        }
        catch( IllegalArgumentException arg ) {
            fail( "IllegalArgumentException caught" );
        }
    }

    @Test
    void testOptionArgNumbers() {
        OptionBuilder.withDescription( "option description" );
        OptionBuilder.hasArgs( 2 );
        Option opt = OptionBuilder.create( 'o' );
        assertEquals( 2, opt.getArgs() );
    }

    @Test
    void testIllegalOptions() {
        // bad single character option
        try {
            OptionBuilder.withDescription( "option description" );
            OptionBuilder.create( '"' );
            fail( "IllegalArgumentException not caught" );
        }
        catch( IllegalArgumentException exp ) {
            // success
        }

        // bad character in option string
        try {
            OptionBuilder.create( "opt`" );
            fail( "IllegalArgumentException not caught" );
        }
        catch( IllegalArgumentException exp ) {
            // success
        }

        // null option
        try {
            OptionBuilder.create( null );
            fail( "IllegalArgumentException not caught" );
        }
        catch( IllegalArgumentException exp ) {
            // success
        }

        // valid option 
        try {
            OptionBuilder.create( "opt" );
            // success
        }
        catch( IllegalArgumentException exp ) {
            fail( "IllegalArgumentException caught" );
        }
    }
}