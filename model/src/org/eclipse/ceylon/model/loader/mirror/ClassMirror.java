package org.eclipse.ceylon.model.loader.mirror;

import java.util.List;

import org.eclipse.ceylon.model.typechecker.model.Module;

/**
 * Represents a Java Class definition.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface ClassMirror extends AnnotatedMirror, AccessibleMirror {

    /**
     * Returns true if this is an interface
     */
    boolean isInterface();
    
    /**
     * Returns true if this is an {@code @interface}.
     */
    boolean isAnnotationType();
    
    /**
     * Returns true if this is an abstract class
     */
    boolean isAbstract();
    
    /**
     * Returns true if this is a static class
     */
    boolean isStatic();

    /**
     * Returns true if this class is an inner class
     */
    boolean isInnerClass();

    /**
     * Returns true if this class is a local class
     */
    boolean isLocalClass();

    /**
     * Returns true if this class is an anonymous class
     */
    boolean isAnonymous();
    
    /**
     * Returns true if this class is an enum class
     */
    boolean isEnum();

    /**
     * Returns the fully-qualified class name
     */
    String getQualifiedName();
    
    /**
     * Returns the fully-qualified class name in flat form: dots to separate package parts, and dollars
     * to separate inner members.
     */
    String getFlatName();
    
    /**
     * Returns this class's package
     */
    PackageMirror getPackage();
    
    /**
     * Returns the list of methods and constructors defined by this class. Does not include inherited
     * methods and constructors.
     */
    List<MethodMirror> getDirectMethods();

    /**
     * Returns the list of fields defined by this class. Does not include inherited
     * fields.
     */
    List<FieldMirror> getDirectFields();

    /**
     * Returns the list of type parameters for this class
     */
    List<TypeParameterMirror> getTypeParameters();

    /**
     * Returns the list of inner classes directly contained in this class. Does not include inherited
     * inner classes. 
     */
    List<ClassMirror> getDirectInnerClasses();

    /**
     * Returns this class's superclass, or null if it doesn't have any
     */
    TypeMirror getSuperclass();

    /**
     * Returns this class's containing class, if any
     */
    ClassMirror getEnclosingClass();

    /**
     * Returns this class's enclosing method, if any
     */
    MethodMirror getEnclosingMethod();

    /**
     * Returns the list of interfaces implemented by this class
     */
    List<TypeMirror> getInterfaces();

    /**
     * Returns true if this class represents a toplevel attribute
     */
    boolean isCeylonToplevelAttribute();

    /**
     * Returns true if this class represents a toplevel object
     */
    boolean isCeylonToplevelObject();

    /**
     * Returns true if this class represents a toplevel method
     */
    boolean isCeylonToplevelMethod();
    
    /**
     * Returns true if this class was loaded from source, false if it was loaded from a compiled form
     */
    boolean isLoadedFromSource();

    /**
     * Returns true if this class was loaded from a Java source file, false if it came from a ceylon
     * source file or from a class file
     */
    boolean isJavaSource();

    /**
     * Returns true if this class is final
     */
    boolean isFinal();

    String getCacheKey(Module module);
}
