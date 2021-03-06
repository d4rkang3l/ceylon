package org.eclipse.ceylon.compiler.java.test.structure;

import java.util.Arrays;

import org.eclipse.ceylon.compiler.java.test.CompilerError;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.junit.Test;

public class StaticTests extends CompilerTests {
    @Override
    protected String transformDestDir(String name) {
        return name + "-static";
    }
    
    @Test
    public void testStaticAttribute() {
        compareWithJavaSource("attribute/StaticAttribute");
    }
    
    @Test
    public void testStaticAttributeGeneric() {
        compareWithJavaSource("attribute/StaticAttributeGeneric");
    }
    
    @Test
    public void testStaticMethod() {
        compareWithJavaSource("method/StaticMethod");
    }
    
    @Test
    public void testStaticClass() {
        compareWithJavaSource("klass/StaticClass");
    }
    
    @Test
    public void testStaticMemberClass() {
        compareWithJavaSource("klass/StaticMemberClass");
    }
    
    @Test
    public void testStaticObject() {
        compareWithJavaSource("klass/StaticObject");
    }
    
    @Test
    public void testStaticAlias() {
        compareWithJavaSource("alias/StaticAlias");
    }
    
    @Test
    public void testStaticLate() {
        compareWithJavaSource("attribute/StaticLate");
    }

    @Test
    public void testStaticMembers() {
        compareWithJavaSource(Arrays.asList("-target", "8", "-source", "8"), "attribute/StaticMembers.src", "attribute/StaticMembers.ceylon");
    }

    @Test
    public void testStaticInterfaceMethods() {
        assertErrors("klass/StaticInterfaceMethods",
                Arrays.asList("-target", "7", "-source", "7"),
                null,
                    new CompilerError(23, "declaration of static interface member is not allowed unless you set the -target flag to 8: --javac=-target=8"),
                    new CompilerError(25, "declaration of static interface member is not allowed unless you set the -target flag to 8: --javac=-target=8"),
                    new CompilerError(29, "declaration of static interface member is not allowed unless you set the -target flag to 8: --javac=-target=8"),
                    new CompilerError(31, "declaration of static interface member is not allowed unless you set the -target flag to 8: --javac=-target=8"),
                    new CompilerError(34, "declaration of static interface member is not allowed unless you set the -target flag to 8: --javac=-target=8"));
        compile(Arrays.asList("-target", "8", "-source", "8"), "klass/StaticInterfaceMethods.ceylon");
    }
}
