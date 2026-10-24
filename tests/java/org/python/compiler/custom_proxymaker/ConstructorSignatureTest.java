package org.python.compiler.custom_proxymaker;

import java.awt.Container;
import java.lang.reflect.Constructor;

import javax.swing.BoxLayout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.python.util.ProxyCompiler;

class ConstructorSignatureTest {

    Class<?> proxy;

    @BeforeEach
    void setUp() throws Exception {
        ProxyCompiler.compile("tests/python/custom_proxymaker/constructor_signatures.py",
                "build/testclasses");
        proxy = Class.forName("custom_proxymaker.tests.ConstructorSignatures");
    }

    @Disabled // Constructor signatures are not working yet
    @Test
    void returnsVoid() throws Exception {
        Constructor<?> constructor =
                proxy.getConstructor(new Class<?>[] {Container.class, Integer.TYPE});
        constructor.newInstance(new Container(), BoxLayout.X_AXIS);
    }
}
