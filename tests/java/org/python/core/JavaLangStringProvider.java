package org.python.core;

public final class JavaLangStringProvider {

    private static final String SMALL_O_UMLAUT = "\u00F6";
    private static final String RIGHT_SINGLE_QUOTATION_MARK = "\u2019";

    private static final String BEAUTIFUL = "sch" + SMALL_O_UMLAUT + "n";
    private static final String START_OF_BEAUTIFUL = "sch" + SMALL_O_UMLAUT;
    private static final String END_OF_BEAUTIFUL = SMALL_O_UMLAUT + "n";

    private static final String JEANNE_DARC = "Jeanne d" + RIGHT_SINGLE_QUOTATION_MARK + "Arc";
    private static final String START_OF_JEANNE_DARC = "Jeanne d" + RIGHT_SINGLE_QUOTATION_MARK + "A";
    private static final String END_OF_JEANNE_DARC = "d" + RIGHT_SINGLE_QUOTATION_MARK + "Arc";

    private static final String BEAUTIFUL_JEANNE_DARC = BEAUTIFUL + "e" + JEANNE_DARC;

    /**
     * Provides a single small o umlaut
     */
    public static final String getSmallOUmlaut() {
        return SMALL_O_UMLAUT;
    }

    /**
     * Provides the word 'beautiful' in German, using a small o umlaut
     */
    public static final String getBeautiful() {
        return BEAUTIFUL;
    }

    /**
     * Provides the start of 'beautiful' in German, using a small o umlaut
     */
    public static final String getStartOfBeautiful() {
        return START_OF_BEAUTIFUL;
    }

    /**
     * Provides the end of 'beautiful' in German, using a small o umlaut
     */
    public static final String getEndOfBeautiful() {
        return END_OF_BEAUTIFUL;
    }

    /**
     * Provides the word 'more beautiful' in German, using a small o umlaut
     */
    public static final String getMoreBeautiful() {
        return BEAUTIFUL + "er";
    }

    /**
     * Provides the right single quotation mark
     *
     * @see "https://www.compart.com/en/unicode/U+2019"
     */
    public static final String getRightSingleQuotationMark() {
        return RIGHT_SINGLE_QUOTATION_MARK;
    }

    /**
     * Provides the name of Jeanne d'Arc, but using a right single quotation mark as apostrophe
     */
    public static final String getJeanneDArc() {
        return JEANNE_DARC;
    }

    /**
     * Provides the start of Jeanne d'Arc, including a right single quotation mark as apostrophe
     */
    public static final String getStartOfJeanneDArc() {
        return START_OF_JEANNE_DARC;
    }

    /**
     * Provides the end of Jeanne d'Arc, including a right single quotation mark as apostrophe
     */
    public static final String getEndOfJeanneDArc() {
        return END_OF_JEANNE_DARC;
    }

    /**
     * Provides beautiful Jeanne d'Arc, a mixture of Umlaut and Unicode
     */
    public static final String getBeautifulJeanneDArc() {
        return BEAUTIFUL_JEANNE_DARC;
    }

}
