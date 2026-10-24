package org.python.compiler.custom_proxymaker;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.python.util.ProxyCompiler;

/*
 * Tests support for various combinations of method signatures
 */
class MethodSignatureTest {

    Class<?> proxy;

    @BeforeEach
    void setUp() throws Exception {
        ProxyCompiler.compile("tests/python/custom_proxymaker/method_signatures.py",
                "build/testclasses");
        proxy = Class.forName("custom_proxymaker.tests.MethodSignatures");
    }

    @Test
    void methodThrows() throws Exception {
        Method method = proxy.getMethod("throwsException");
        assertArrayEquals(new Class<?>[] {RuntimeException.class}, method.getExceptionTypes());
    }

    @Test
    void returnsVoid() throws Exception {
        Method method = proxy.getMethod("throwsException");
        assertEquals(Void.TYPE, method.getReturnType());
    }

    @Test
    void returnsLong() throws Exception {
        Method method = proxy.getMethod("returnsLong");
        assertEquals(Long.TYPE, method.getReturnType());
    }

    @Test
    void returnsObject() throws Exception {
        Method method = proxy.getMethod("returnsObject");
        assertEquals(Object.class, method.getReturnType());
    }

    @Test
    void returnsArray() throws Exception {
        Method method = proxy.getMethod("returnsArray");
        Object compareType = Array.newInstance(Long.TYPE, 0);
        assertEquals(compareType.getClass(), method.getReturnType());
    }

    @Test
    void returnsArrayObj() throws Exception {
        Method method = proxy.getMethod("returnsArrayObj");
        Object compareType = Array.newInstance(Object.class, 0);
        assertEquals(compareType.getClass(), method.getReturnType());
    }

    @Test
    @SuppressWarnings("unused")
    void acceptsString() throws Exception {
        Class<?>[] partypes = new Class[] {String.class};
        Method method = proxy.getMethod("acceptsString", partypes);
    }

    @Test
    @SuppressWarnings("unused")
    void acceptsArray() throws Exception {
        Object compareType = Array.newInstance(Long.TYPE, 0);
        Class<?>[] partypes = new Class[] {compareType.getClass()};
        Method method = proxy.getMethod("acceptsArray", partypes);
    }
}
