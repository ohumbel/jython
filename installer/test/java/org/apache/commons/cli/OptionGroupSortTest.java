package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OptionGroupSortTest  {

    private OptionGroup _optionGroup;

    @BeforeEach
    protected void setUp() {
        _optionGroup = new OptionGroup();
        _optionGroup.addOption(new Option("f", "first", false, "first"));
        _optionGroup.addOption(new Option("s", "second", false, "second"));
        _optionGroup.addOption(new Option("t", "third", false, "third"));
    }

    @Test
    public void testSortNames() {
        Collection<?> names = _optionGroup.getNames();
        Iterator<?> namesIterator = names.iterator();
        assertTrue(namesIterator.hasNext());
        assertEquals("-f", (String) namesIterator.next());
        assertTrue(namesIterator.hasNext());
        assertEquals("-s", (String) namesIterator.next());
        assertTrue(namesIterator.hasNext());
        assertEquals("-t", (String) namesIterator.next());
        assertFalse(namesIterator.hasNext());
    }

    @Test
    public void testSortOptions() {
        Collection<?> options = _optionGroup.getOptions();
        Iterator<?> optionIterator = options.iterator();
        assertTrue(optionIterator.hasNext());
        assertEquals("first", ((Option) optionIterator.next()).getLongOpt());
        assertTrue(optionIterator.hasNext());
        assertEquals("second", ((Option) optionIterator.next()).getLongOpt());
        assertTrue(optionIterator.hasNext());
        assertEquals("third", ((Option) optionIterator.next()).getLongOpt());
        assertFalse(optionIterator.hasNext());
    }

}
