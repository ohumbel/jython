package org.python.util.install;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class UnicodeSequencesTest  {

    private static Set<String> _latin1Encodings;

    @Test
    void testUmlaute() {
        String fileEncoding = System.getProperty("file.encoding", "unknown");
        if (getLatin1Encodings().contains(fileEncoding)) {
            assertEquals("�", UnicodeSequences.a2);
            assertEquals("�", UnicodeSequences.A2);
            assertEquals("�", UnicodeSequences.o2);
            assertEquals("�", UnicodeSequences.O2);
            assertEquals("�", UnicodeSequences.u2);
            assertEquals("�", UnicodeSequences.U2);
        }
    }

    private static Set<String> getLatin1Encodings() {
        if (_latin1Encodings == null) {
            _latin1Encodings = new HashSet<>(3);
            _latin1Encodings.add("ISO-LATIN-1");
            _latin1Encodings.add("ISO-8859-1");
            _latin1Encodings.add("Cp1252");
        }
        return _latin1Encodings;
    }

}
