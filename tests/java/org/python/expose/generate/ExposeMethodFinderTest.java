package org.python.expose.generate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class ExposeMethodFinderTest extends InterpTestCase implements Opcodes, PyTypes {

    private ExposedMethodFinder makeFinder(int access, String descriptor, String methodName) {
        return new ExposedMethodFinder("simpleexposed",
                                       Type.getType(SimpleExposed.class),
                                       access,
                                       methodName,
                                       descriptor,
                                       null,
                                       new MethodVisitor(Opcodes.ASM7) {}) {

            @Override
            public void handleResult(InstanceMethodExposer exposer) {
                resultantMethExp = exposer;
            }

            @Override
            public void handleNewExposer(Exposer exposer) {
                resultantNewExp = exposer;
            }

            @Override
            public void exposeAsDeleteDescriptor(String descName) {
                deleteName = descName;
            }

            @Override
            public void exposeAsGetDescriptor(String descName, String doc) {
                getName = descName;
            }

            @Override
            public void exposeAsSetDescriptor(String descName) {
                setName = descName;
            }

            @Override
            public void handleResult(ClassMethodExposer exposer) {}
        };
    }

    private void checkSet(String typeThatShouldBeSet) {
        checkSet(METHOD, typeThatShouldBeSet, resultantMethExp);
        checkSet(NEW, typeThatShouldBeSet, resultantNewExp);
        checkSet(GET, typeThatShouldBeSet, getName);
        checkSet(SET, typeThatShouldBeSet, setName);
        checkSet(DELETE, typeThatShouldBeSet, deleteName);
    }

    private void checkSet(String type, String typeThatShouldBeSet, Object actualValue) {
        if(type.equals(typeThatShouldBeSet)) {
            assertNotNull(actualValue, type + " should've been set after these operations");
        } else {
            assertNull(actualValue, type + " shouldn't have been set by these operations");
        }
    }

    @Test
    void testExposedNew() {
        ExposedMethodFinder visitor = makeFinder(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC,
                                                 NewExposer.NEW_DESCRIPTOR, "new_");
        visitor.visitAnnotation(EXPOSED_NEW.getDescriptor(), true);
        visitor.visitEnd();
        checkSet(NEW);
    }

    @Test
    void testSimpleExposedMethod() {
        makeAndVisit(EXPOSED_METHOD, METHOD, null);
        assertEquals(1, resultantMethExp.getNames().length);
        assertEquals("simpleMethod", resultantMethExp.getNames()[0]);
    }

    private void makeAndVisit(Type annotationToVisit,
                              String typeThatShouldBeSet,
                              String arrayToVisit,
                              String... vals) {
        ExposedMethodFinder visitor = makeFinder(Opcodes.ACC_PRIVATE, "()V", "simpleMethod");
        AnnotationVisitor methVisitor = visitor.visitAnnotation(annotationToVisit.getDescriptor(),
                                                                true);
        if(arrayToVisit != null) {
            AnnotationVisitor arrayVisitor = methVisitor.visitArray(arrayToVisit);
            for(String val : vals) {
                arrayVisitor.visit("doesn't matter in asm", val);
            }
            arrayVisitor.visitEnd();
            methVisitor.visitEnd();
        }
        visitor.visitEnd();
        checkSet(typeThatShouldBeSet);
    }

    @Test
    void testNamesExposedMethod() {
        makeAndVisit(EXPOSED_METHOD, METHOD, "names", "first", "second");
        assertEquals(0, resultantMethExp.defaults.length);
        assertEquals(2, resultantMethExp.getNames().length);
        assertEquals("first", resultantMethExp.getNames()[0]);
        assertEquals("second", resultantMethExp.getNames()[1]);
    }

    @Test
    void testDefaultsExposedMethod() {
        makeAndVisit(EXPOSED_METHOD, METHOD, "defaults", "Py.None");
        assertEquals(1, resultantMethExp.defaults.length);
        assertEquals("Py.None", resultantMethExp.defaults[0]);
    }

    @Test
    void testDelDescriptor() {
        makeAndVisitDescr("simpleMethod", EXPOSED_DELETE, DELETE);
        assertEquals("simpleMethod", deleteName);
    }

    @Test
    void testSetDescriptor() {
        makeAndVisitDescr("simpleMethod", EXPOSED_SET, SET);
        assertEquals("simpleMethod", setName);
    }

    @Test
    void testGetDescriptor() {
        makeAndVisitDescr("getVal", EXPOSED_GET, GET);
        assertEquals("getVal", getName);
    }

    @Test
    void testNamedGetDescriptor() {
        makeAndVisitDescr("getVal", EXPOSED_GET, GET, "val");
        assertEquals("val", getName);
    }

    @Test
    void testNamedSetDescriptor() {
        makeAndVisitDescr("setVal", EXPOSED_SET, SET, "val");
        assertEquals("val", setName);
    }

    @Test
    void testNamedDelDescriptor() {
        makeAndVisitDescr("delVal", EXPOSED_DELETE, DELETE, "val");
        assertEquals("val", deleteName);
    }

    private void makeAndVisitDescr(String methodName, Type annotationToVisit, String typeThatShouldBeSet) {
        makeAndVisitDescr(methodName, annotationToVisit, typeThatShouldBeSet, null);
    }
    
    private void makeAndVisitDescr(String methodName, Type annotationToVisit, String typeThatShouldBeSet, String name) {
        ExposedMethodFinder visitor = makeFinder(Opcodes.ACC_PRIVATE, "()V", methodName);
        AnnotationVisitor methVisitor = visitor.visitAnnotation(annotationToVisit.getDescriptor(),
                                                                true);
        if(name != null) {
            methVisitor.visit("name", name);
        }
        methVisitor.visitEnd();
        visitor.visitEnd();
        checkSet(typeThatShouldBeSet);
        
    }

    private static final String METHOD = "method";

    private static final String NEW = "new";

    private static final String GET = "get";

    private static final String SET = "set";

    private static final String DELETE = "del";

    private InstanceMethodExposer resultantMethExp;

    private Exposer resultantNewExp;

    private String deleteName, getName, setName;
}
