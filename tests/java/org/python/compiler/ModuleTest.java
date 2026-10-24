package org.python.compiler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

class ModuleTest {

    /**
     * In order to get testCopysign() in test_math.py passing, PyFloatConstant needs to distinguish
     * between 0.0 and -0.0:
     * 
     * <pre>
     *   # copysign should let us distinguish signs of zeros
     * </pre>
     */
    @Test
    void testPyFloatConstant_Zero() {
        PyFloatConstant positiveZero = new PyFloatConstant(0.0);
        PyFloatConstant negativeZero = new PyFloatConstant(-0.0);
        assertNotSame(positiveZero, negativeZero);
        assertFalse(positiveZero.equals(negativeZero));
    }
}
