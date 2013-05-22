package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.ArrayList;
import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.metamodel.Annotated$impl;
import ceylon.language.metamodel.AppliedType;
import ceylon.language.metamodel.ClassOrInterface;
import ceylon.language.metamodel.nothingType_;
import ceylon.language.metamodel.untyped.Declaration;
import ceylon.language.metamodel.untyped.Package$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreePackage implements ceylon.language.metamodel.untyped.Package, 
        ceylon.language.metamodel.Annotated, AnnotationBearing,
        ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreePackage.class);
    
    private com.redhat.ceylon.compiler.typechecker.model.Package declaration;

    private FreeModule module;

    private Sequential<FreeDeclaration> members;

    public FreePackage(com.redhat.ceylon.compiler.typechecker.model.Package declaration){
        this.declaration = declaration;
    }
    
    @Override
    @Ignore
    public Package$impl $ceylon$language$metamodel$untyped$Package$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @Ignore
    public Annotated$impl $ceylon$language$metamodel$Annotated$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations() {
        Class<?> javaClass = Metamodel.getJavaClass(declaration);
        return javaClass != null ? javaClass.getAnnotations() : AnnotationBearing.NONE;
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getName() {
        return declaration.getNameAsString();
    }

    @Override
    public ceylon.language.metamodel.untyped.Module getContainer() {
        // this does not need to be thread-safe as Metamodel.getOrCreateMetamodel is thread-safe so if we
        // assign module twice we get the same result
        if(module == null){
            module = Metamodel.getOrCreateMetamodel(declaration.getModule());
        }
        return module;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel.untyped::Declaration"))
    public <Kind extends ceylon.language.metamodel.untyped.Declaration> Sequential<? extends Kind> members(@Ignore TypeDescriptor $reifiedKind) {
        
        if($reifiedKind instanceof TypeDescriptor.Class){
            List<com.redhat.ceylon.compiler.typechecker.model.Declaration> modelMembers = declaration.getMembers();
            Class<?> declarationClass = ((TypeDescriptor.Class) $reifiedKind).getKlass();
            List<Kind> members = new ArrayList<Kind>(modelMembers.size());
            for(com.redhat.ceylon.compiler.typechecker.model.Declaration modelDecl : modelMembers){
                if(memberMatches(declarationClass, modelDecl))
                    members.add((Kind)Metamodel.getOrCreateMetamodel(modelDecl));
            }
            return (Sequential) Util.sequentialInstance($reifiedKind, members.toArray());
        }
        throw new UnsupportedOperationException("Kind not supported yet: "+$reifiedKind);
    }
    
    private boolean memberMatches(Class<?> declarationClass,
            com.redhat.ceylon.compiler.typechecker.model.Declaration modelDecl) {
        return (declarationClass == ceylon.language.metamodel.untyped.Value.class 
                && modelDecl instanceof com.redhat.ceylon.compiler.typechecker.model.Value)
           || (declarationClass == ceylon.language.metamodel.untyped.Variable.class 
                && modelDecl instanceof com.redhat.ceylon.compiler.typechecker.model.Value
                && ((com.redhat.ceylon.compiler.typechecker.model.Value)modelDecl).isVariable())
           || (declarationClass == ceylon.language.metamodel.untyped.Function.class 
                && modelDecl instanceof com.redhat.ceylon.compiler.typechecker.model.Method)
           || (declarationClass == ceylon.language.metamodel.untyped.Class.class 
                && modelDecl instanceof com.redhat.ceylon.compiler.typechecker.model.Class)
           || (declarationClass == ceylon.language.metamodel.untyped.Interface.class 
                && modelDecl instanceof com.redhat.ceylon.compiler.typechecker.model.Interface)
           || (declarationClass == ceylon.language.metamodel.untyped.ClassOrInterface.class 
                && modelDecl instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)
           || declarationClass == ceylon.language.metamodel.untyped.Declaration.class;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.untyped::Value|ceylon.language::Null")
    public ceylon.language.metamodel.untyped.Value getAttribute(String name) {
        com.redhat.ceylon.compiler.typechecker.model.Declaration toplevel = declaration.getMember(name, null, false);
        if(toplevel instanceof com.redhat.ceylon.compiler.typechecker.model.Value == false)
            return null;
        com.redhat.ceylon.compiler.typechecker.model.Value decl = (com.redhat.ceylon.compiler.typechecker.model.Value) toplevel;
        return (FreeValue) Metamodel.getOrCreateMetamodel(decl);
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.untyped::Function|ceylon.language::Null")
    public ceylon.language.metamodel.untyped.Function getFunction(String name) {
        com.redhat.ceylon.compiler.typechecker.model.Declaration toplevel = declaration.getMember(name, null, false);
        if(toplevel instanceof com.redhat.ceylon.compiler.typechecker.model.Method == false)
            return null;
        com.redhat.ceylon.compiler.typechecker.model.Method decl = (com.redhat.ceylon.compiler.typechecker.model.Method) toplevel;
        return (FreeFunction) Metamodel.getOrCreateMetamodel(decl);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel.untyped::Declaration"), 
        @TypeParameter(value = "Annotation") 
    })
    public <Kind extends Declaration, Annotation> Sequential<? extends Kind> annotatedMembers(@Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        if($reifiedKind instanceof TypeDescriptor.Class){
            List<com.redhat.ceylon.compiler.typechecker.model.Declaration> modelMembers = declaration.getMembers();
            Class<?> declarationClass = ((TypeDescriptor.Class) $reifiedKind).getKlass();
            List<Kind> members = new ArrayList<Kind>(modelMembers.size());
            for(com.redhat.ceylon.compiler.typechecker.model.Declaration modelDecl : modelMembers){
                if(memberMatches(declarationClass, modelDecl)) {
                    Kind member = (Kind)Metamodel.getOrCreateMetamodel(modelDecl);
                    AppliedType at = Metamodel.getAppliedMetamodel($reifiedAnnotation);
                    if (at instanceof nothingType_) {
                        return (Sequential)empty_.getEmpty$();
                    }
                    Sequential<Annotation> annotations = Metamodel.<Annotation>annotations($reifiedAnnotation, (ClassOrInterface<Annotation>)at, member);
                    if (!annotations.getEmpty()) {
                        members.add(member);
                    }
                }
            }
            return (Sequential) Util.sequentialInstance($reifiedKind, members.toArray());
        }
        throw new UnsupportedOperationException("Kind not supported yet: "+$reifiedKind);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

}
