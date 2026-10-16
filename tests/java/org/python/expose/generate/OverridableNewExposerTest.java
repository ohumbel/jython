package org.python.expose.generate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.python.core.BytecodeLoader;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyNewWrapper;
import org.python.core.PyObject;
import org.python.core.PyTuple;
import org.python.core.PyType;
import org.python.expose.ExposedNew;
import org.python.expose.ExposedType;

public class OverridableNewExposerTest extends InterpTestCase implements PyTypes, Opcodes {

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        ne = new OverridableNewExposer(Type.getType(Instantiable.class),
                                Type.getType(Instantiable.class),
                                ACC_PUBLIC,
                                "creator",
                                Type.getMethodDescriptor(VOID, new Type[] {APYOBJ, ASTRING}),
                                new String[] {});
        Class<?> descriptor = ne.load(new BytecodeLoader.Loader());
        instance = (PyNewWrapper)descriptor.getDeclaredConstructor().newInstance();
        type = PyType.fromClass(Instantiable.class);
        instance.setWrappedType(type);
    }

    @Test
    public void testSimple() throws Exception {
        assertEquals("org/python/expose/generate/OverridableNewExposerTest$Instantiable$exposed___new__",
                     ne.getInternalName());
        assertEquals("org.python.expose.generate.OverridableNewExposerTest$Instantiable$exposed___new__",
                     ne.getClassName());
        assertSame("__new__", instance.__getattr__("__name__").toString());
        Instantiable created = (Instantiable)instance.__call__(type);
        assertEquals(0, created.timesCalled, "Just calling the actual new doesn't call its init");
        created = (Instantiable)instance.new_impl(true, type, Py.EmptyObjects, Py.NoKeywords);
        assertEquals(1, created.timesCalled, "Passing true to new_init should get the init method called");
        assertEquals(type, created.forType, "the regular type is passed in for normal instantiation");
    }

    @Test
    public void testSubtype() throws Exception {
        PyType sub = (PyType)PyType.newType(new PyNewWrapper() {

            public PyObject new_impl(boolean init,
                                     PyType subtype,
                                     PyObject[] args,
                                     String[] keywords) {
                return new Instantiable(subtype);
            }
        }, PyType.TYPE, "subinst", new PyTuple(new PyObject[] {type}), new PyDictionary());
        Instantiable created = (Instantiable)instance.new_impl(true,
                                                               sub,
                                                               Py.EmptyObjects,
                                                               Py.NoKeywords);
        assertEquals(0, created.timesCalled, "new's init isn't called when a subtype comes in");
        assertSame(sub, created.forType, "the subtype is created when a subtype is passed in");
    }

    @ExposedType()
    public static class Instantiable extends PyObject {

        public Instantiable(PyType forType) {
            this.forType = forType;
        }

        @ExposedNew
        public void creator(PyObject[] args, String[] keywords) {
            timesCalled++;
        }

        private PyType forType;

        private int timesCalled;
    }

    private OverridableNewExposer ne;

    private PyNewWrapper instance;

    private PyType type;
}
