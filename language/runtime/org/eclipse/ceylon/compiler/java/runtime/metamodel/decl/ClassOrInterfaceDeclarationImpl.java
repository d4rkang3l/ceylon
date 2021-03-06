package org.eclipse.ceylon.compiler.java.runtime.metamodel.decl;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.language.ObjectArrayIterable;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.Sequenced;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.AnnotationBearing;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Predicates;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Predicates.Predicate;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.MemberClassImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.MemberInterfaceImpl;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Value;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassOrInterfaceDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.FunctionDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.NestableDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ValueDeclarationImpl;

import ceylon.language.Anything;
import ceylon.language.Empty;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.model.ClassOrInterface;
import ceylon.language.meta.model.Member;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public abstract class ClassOrInterfaceDeclarationImpl
    extends NestableDeclarationImpl
    implements ceylon.language.meta.declaration.ClassOrInterfaceDeclaration, AnnotationBearing {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ClassOrInterfaceDeclarationImpl.class);
    
    @Ignore
    private static final TypeDescriptor $FunctionTypeDescriptor = TypeDescriptor.klass(ceylon.language.meta.declaration.FunctionDeclaration.class, Anything.$TypeDescriptor$, Empty.$TypeDescriptor$);
    @Ignore
    private static final TypeDescriptor $AttributeTypeDescriptor = TypeDescriptor.klass(ceylon.language.meta.declaration.ValueDeclaration.class, Anything.$TypeDescriptor$);
    @Ignore
    private static final TypeDescriptor $ClassOrInterfaceTypeDescriptor = TypeDescriptor.klass(ceylon.language.meta.declaration.ClassOrInterfaceDeclaration.class, Anything.$TypeDescriptor$);
    
    private volatile boolean initialised = false;
    private ceylon.language.meta.declaration.OpenClassType superclass;
    private Sequential<ceylon.language.meta.declaration.OpenInterfaceType> interfaces;
    private Sequential<? extends ceylon.language.meta.declaration.TypeParameter> typeParameters;

    private List<ceylon.language.meta.declaration.NestableDeclaration> declaredDeclarations;
    private List<ceylon.language.meta.declaration.NestableDeclaration> declarations;

    private Sequential<? extends ceylon.language.meta.declaration.OpenType> caseTypes;

    public ClassOrInterfaceDeclarationImpl(org.eclipse.ceylon.model.typechecker.model.ClassOrInterface declaration) {
        super(declaration);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void init(){
        org.eclipse.ceylon.model.typechecker.model.ClassOrInterface declaration = (org.eclipse.ceylon.model.typechecker.model.ClassOrInterface) this.declaration;
        
        Type superType = declaration.getExtendedType();
        if(superType != null)
            this.superclass = (ceylon.language.meta.declaration.OpenClassType) Metamodel.getMetamodel(superType);
        
        List<Type> satisfiedTypes = declaration.getSatisfiedTypes();
        ceylon.language.meta.declaration.OpenInterfaceType[] interfaces = new ceylon.language.meta.declaration.OpenInterfaceType[satisfiedTypes.size()];
        int i=0;
        for(Type pt : satisfiedTypes){
            interfaces[i++] = (ceylon.language.meta.declaration.OpenInterfaceType) Metamodel.getMetamodel(pt);
        }
        this.interfaces = Util.sequentialWrapper(ceylon.language.meta.declaration.OpenInterfaceType.$TypeDescriptor$, interfaces);

        if(declaration.getCaseTypes() != null)
            this.caseTypes = Metamodel.getMetamodelSequential(declaration.getCaseTypes());
        else
            this.caseTypes = (Sequential<? extends ceylon.language.meta.declaration.OpenType>)(Sequential)empty_.get_();

        this.typeParameters = Metamodel.getTypeParameters(declaration);
        
        List<org.eclipse.ceylon.model.typechecker.model.Declaration> memberModelDeclarations = declaration.getMembers();
        this.declaredDeclarations = new LinkedList<ceylon.language.meta.declaration.NestableDeclaration>();
        for(org.eclipse.ceylon.model.typechecker.model.Declaration memberModelDeclaration : memberModelDeclarations){
            addDeclarationTo(memberModelDeclaration, declaredDeclarations);
        }

        Collection<org.eclipse.ceylon.model.typechecker.model.Declaration> inheritedModelDeclarations = 
                collectMembers(declaration);
        this.declarations = new LinkedList<ceylon.language.meta.declaration.NestableDeclaration>();
        for(org.eclipse.ceylon.model.typechecker.model.Declaration memberModelDeclaration : inheritedModelDeclarations){
            addDeclarationTo(memberModelDeclaration, declarations);
        }
    }

    protected static void addDeclarationTo(
            org.eclipse.ceylon.model.typechecker.model.Declaration memberModelDeclaration,
            List<ceylon.language.meta.declaration.NestableDeclaration> declaredDeclarations) {
        if(isSupportedType(memberModelDeclaration))
            declaredDeclarations.add(Metamodel.<ceylon.language.meta.declaration.NestableDeclaration>getOrCreateMetamodel(memberModelDeclaration));
        if (memberModelDeclaration instanceof Value) {
            Setter setter = ((Value)memberModelDeclaration).getSetter();
            if (setter != null) {
                declaredDeclarations.add(Metamodel.<ceylon.language.meta.declaration.NestableDeclaration>getOrCreateMetamodel(setter));
            }
        }
    }

    private static boolean isSupportedType(Declaration memberModelDeclaration) {
        return memberModelDeclaration instanceof org.eclipse.ceylon.model.typechecker.model.Value
                || (memberModelDeclaration instanceof org.eclipse.ceylon.model.typechecker.model.Function
                        && !((org.eclipse.ceylon.model.typechecker.model.Function)memberModelDeclaration).isAbstraction())
                || memberModelDeclaration instanceof org.eclipse.ceylon.model.typechecker.model.TypeAlias
                || memberModelDeclaration instanceof org.eclipse.ceylon.model.typechecker.model.Interface
                || (memberModelDeclaration instanceof org.eclipse.ceylon.model.typechecker.model.Class
                        && !((org.eclipse.ceylon.model.typechecker.model.Class)memberModelDeclaration).isAbstraction());
    }

    private Collection<org.eclipse.ceylon.model.typechecker.model.Declaration> collectMembers(org.eclipse.ceylon.model.typechecker.model.TypeDeclaration base){
        Map<String, org.eclipse.ceylon.model.typechecker.model.Declaration> byName = 
                new HashMap<String, org.eclipse.ceylon.model.typechecker.model.Declaration>();
        collectMembers(base, byName);
        return byName.values();
    }
    
    private void collectMembers(org.eclipse.ceylon.model.typechecker.model.TypeDeclaration base, Map<String, Declaration> byName) {
        for(org.eclipse.ceylon.model.typechecker.model.Declaration decl : base.getMembers()){
            if(decl.isShared() && org.eclipse.ceylon.model.typechecker.model.ModelUtil.isResolvable(decl)){
                Declaration otherDeclaration = byName.get(decl.getName());
                if(otherDeclaration == null || decl.refines(otherDeclaration))
                    byName.put(decl.getName(), decl);
            }
        }
        org.eclipse.ceylon.model.typechecker.model.Type et = base.getExtendedType();
        if(et != null) {
            collectMembers(et.getDeclaration(), byName);
        }
        for(org.eclipse.ceylon.model.typechecker.model.Type st : base.getSatisfiedTypes()){
            if(st != null) {
                collectMembers(st.getDeclaration(), byName);
            }
        }
    }

    protected final void checkInit(){
        if(!initialised){
            synchronized(Metamodel.getLock()){
                if(!initialised){
                    init();
                    initialised = true;
                }
            }
        }
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"))
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration> Sequential<? extends Kind> 
    memberDeclarations(@Ignore TypeDescriptor $reifiedKind) {
        
        Predicates.Predicate<?> predicate = Predicates.isDeclarationOfKind($reifiedKind);
        
        return filteredMembers($reifiedKind, predicate);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"))
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration> Sequential<? extends Kind> 
    declaredMemberDeclarations(@Ignore TypeDescriptor $reifiedKind) {
        
        Predicates.Predicate<?> predicate = Predicates.isDeclarationOfKind($reifiedKind);
        
        return filteredDeclaredMembers($reifiedKind, predicate);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters({
            @TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"),
            @TypeParameter(value = "Annotation", satisfies = "ceylon.language::Annotation")
    })
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration, Annotation extends java.lang.annotation.Annotation> Sequential<? extends Kind> 
    annotatedMemberDeclarations(@Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        
        Predicates.Predicate<?> predicate = Predicates.and(
                Predicates.isDeclarationOfKind($reifiedKind),
                Predicates.isDeclarationAnnotatedWith($reifiedAnnotation));
        
        return filteredMembers($reifiedKind, predicate);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters({
            @TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"),
            @TypeParameter(value = "Annotation", satisfies = "ceylon.language::Annotation")
    })
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration, Annotation extends java.lang.annotation.Annotation> Sequential<? extends Kind> 
    annotatedDeclaredMemberDeclarations(@Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        
        Predicates.Predicate<?> predicate = Predicates.and(
                Predicates.isDeclarationOfKind($reifiedKind),
                Predicates.isDeclarationAnnotatedWith($reifiedAnnotation));
        
        return filteredDeclaredMembers($reifiedKind, predicate);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <Kind> Sequential<? extends Kind> filteredMembers(
            @Ignore TypeDescriptor $reifiedKind,
            Predicates.Predicate predicate) {
        if (predicate == Predicates.false_()) {
            return (Sequential<? extends Kind>)empty_.get_();
        }
        checkInit();
        ArrayList<Kind> members = new ArrayList<Kind>(declarations.size());
        for(ceylon.language.meta.declaration.NestableDeclaration decl : declarations){
            Declaration declaration2 = ((NestableDeclarationImpl)decl).declaration;
            if (!declaration2.isNativeHeader() && predicate.accept(declaration2)) {
                members.add((Kind) decl);
            }
        }
        java.lang.Object[] array = members.toArray(new java.lang.Object[0]);
		ObjectArrayIterable<Kind> iterable = 
				new ObjectArrayIterable<Kind>($reifiedKind, (Kind[]) array);
		return (ceylon.language.Sequential) iterable.sequence();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <Kind> Sequential<? extends Kind> filteredDeclaredMembers(
            @Ignore TypeDescriptor $reifiedKind,
            Predicates.Predicate predicate) {
        if (predicate == Predicates.false_()) {
            return (Sequential<? extends Kind>)empty_.get_();
        }
        checkInit();
        ArrayList<Kind> members = new ArrayList<Kind>(declarations.size());
        for(ceylon.language.meta.declaration.NestableDeclaration decl : declaredDeclarations){
            Declaration declaration2 = ((NestableDeclarationImpl)decl).declaration;
            if (!declaration2.isNativeHeader() && predicate.accept(declaration2)) {
                members.add((Kind) decl);
            }
        }
        java.lang.Object[] array = members.toArray(new java.lang.Object[0]);
		ObjectArrayIterable<Kind> iterable = 
				new ObjectArrayIterable<Kind>($reifiedKind, (Kind[]) array);
		return (ceylon.language.Sequential) iterable.sequence();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <Kind> Kind filteredMember(
            @Ignore TypeDescriptor $reifiedKind,
            Predicates.Predicate predicate) {
        if (predicate == Predicates.false_()) {
            return null;
        }
        checkInit();
        for(ceylon.language.meta.declaration.NestableDeclaration decl : declarations){
            Declaration declaration2 = ((NestableDeclarationImpl)decl).declaration;
            if (!declaration2.isNativeHeader() && predicate.accept(declaration2)) {
                return (Kind)decl;
            }
        }
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <Kind> Kind filteredDeclaredMember(
            @Ignore TypeDescriptor $reifiedKind,
            Predicates.Predicate predicate) {
        if (predicate == Predicates.false_()) {
            return null;
        }
        checkInit();
        for(ceylon.language.meta.declaration.NestableDeclaration decl : declaredDeclarations){
            Declaration declaration2 = ((NestableDeclarationImpl)decl).declaration;
            if (!declaration2.isNativeHeader() && predicate.accept(declaration2)) {
                return (Kind)decl;
            }
        }
        return null;
    }

    @Override
    @TypeInfo("Kind")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"))
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration> Kind 
    getMemberDeclaration(@Ignore TypeDescriptor $reifiedKind, @Name("name") String name) {
        
        Predicates.Predicate<?> predicate = Predicates.and(
                Predicates.isDeclarationNamed(name),
                Predicates.isDeclarationOfKind($reifiedKind)
        );
        
        return filteredMember($reifiedKind, predicate);
    }

    @Override
    @TypeInfo("Kind")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"))
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration> Kind 
    getDeclaredMemberDeclaration(@Ignore TypeDescriptor $reifiedKind, @Name("name") String name) {
        
        Predicates.Predicate<?> predicate = Predicates.and(
                Predicates.isDeclarationNamed(name),
                Predicates.isDeclarationOfKind($reifiedKind)
        );
        
        return filteredDeclaredMember($reifiedKind, predicate);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::OpenInterfaceType>")
    public Sequential<? extends ceylon.language.meta.declaration.OpenInterfaceType> getSatisfiedTypes() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::OpenClassType|ceylon.language::Null")
    public ceylon.language.meta.declaration.OpenClassType getExtendedType() {
        checkInit();
        return superclass;
    }


    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::OpenType>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.declaration.OpenType> getCaseTypes(){
        checkInit();
        return caseTypes;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::TypeParameter>")
    public Sequential<? extends ceylon.language.meta.declaration.TypeParameter> getTypeParameterDeclarations() {
        checkInit();
        return typeParameters;
    }

    @Override
    public boolean getIsAlias(){
        return ((org.eclipse.ceylon.model.typechecker.model.ClassOrInterface)declaration).isAlias();
    }

    @Override
    public OpenType getOpenType() {
        return Metamodel.getMetamodel(((org.eclipse.ceylon.model.typechecker.model.ClassOrInterface)declaration).getType());
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::TypeParameter|ceylon.language::Null")
    public ceylon.language.meta.declaration.TypeParameter getTypeParameterDeclaration(@Name("name") String name) {
        return Metamodel.findDeclarationByName(getTypeParameterDeclarations(), name);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public <Type> ceylon.language.meta.model.ClassOrInterface<Type> apply(@Ignore TypeDescriptor $reifiedType){
        return apply($reifiedType, (Sequential)empty_.get_());
    }

    @SuppressWarnings("unchecked")
    @Override
    @TypeInfo("ceylon.language.meta.model::ClassOrInterface<Type>")
    @TypeParameters({
        @TypeParameter("Type"),
    })
    public <Type extends Object> ceylon.language.meta.model.ClassOrInterface<Type> apply(@Ignore TypeDescriptor $reifiedType,
            @Name("typeArguments") @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        if(!getToplevel())
            throw new ceylon.language.meta.model.TypeApplicationException("Cannot apply a member declaration with no container type: use memberApply");
        List<org.eclipse.ceylon.model.typechecker.model.Type> producedTypes = Metamodel.getProducedTypes(typeArguments);
        Metamodel.checkTypeArguments(null, declaration, producedTypes);
        org.eclipse.ceylon.model.typechecker.model.Reference appliedType = declaration.appliedReference(null, producedTypes);
        Metamodel.checkReifiedTypeArgument("apply", "ClassOrInterface<$1>", Variance.OUT, appliedType.getType(), $reifiedType);
        return (ClassOrInterface<Type>) Metamodel.getAppliedMetamodel(appliedType.getType());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Ignore
    @Override
    public <Container, Type extends Object>
        java.lang.Object memberApply(TypeDescriptor $reifiedContainer,
                                     TypeDescriptor $reifiedType,
                                     ceylon.language.meta.model.Type<? extends Object> containerType){
        
        return this.<Container, Type>memberApply($reifiedContainer,
                                                 $reifiedType,
                                                 containerType,
                                                 (Sequential)empty_.get_());
    }

    @SuppressWarnings("rawtypes")
    @TypeInfo("ceylon.language.meta.model::Member<Container,ceylon.language.meta.model::ClassOrInterface<Type>>&ceylon.language.meta.model::ClassOrInterface<Type>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Type"),
    })
    @Override
    public <Container, Type extends Object>
        java.lang.Object memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedType,
                @Name("containerType") ceylon.language.meta.model.Type<? extends Object> containerType,
                @Name("typeArguments") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        if(getToplevel())
            throw new ceylon.language.meta.model.TypeApplicationException("Cannot apply a toplevel declaration to a container type: use apply");

        ceylon.language.meta.model.Member<? extends Container, ceylon.language.meta.model.ClassOrInterface<?>> member 
            = getAppliedClassOrInterface(null, null, typeArguments, containerType);
        
        // This is all very ugly but we're trying to make it cheaper and friendlier than just checking the full type and showing
        // implementation types to the user, such as AppliedMemberClass
        TypeDescriptor actualReifiedContainer;
        if(member instanceof MemberClassImpl)
            actualReifiedContainer = ((MemberClassImpl)member).$reifiedContainer;
        else
            actualReifiedContainer = ((MemberInterfaceImpl)member).$reifiedContainer;
        org.eclipse.ceylon.model.typechecker.model.Type actualType = Metamodel.getModel((ceylon.language.meta.model.Type<?>) member);
        Metamodel.checkReifiedTypeArgument("memberApply", "Member<$1,ClassOrInterface<$2>>&ClassOrInterface<$2>", 
                Variance.IN, Metamodel.getProducedType(actualReifiedContainer), $reifiedContainer, 
                Variance.OUT, actualType, $reifiedType);
        return member;
    }

    @SuppressWarnings("unchecked")
    public <Container, Kind extends ceylon.language.meta.model.ClassOrInterface<? extends Object>>
    ceylon.language.meta.model.Member<Container, Kind> getAppliedClassOrInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                        @Ignore TypeDescriptor $reifiedKind, 
                                                                        Sequential<? extends ceylon.language.meta.model.Type<?>> types,
                                                                        ceylon.language.meta.model.Type<? extends Object> container){
        List<org.eclipse.ceylon.model.typechecker.model.Type> producedTypes = Metamodel.getProducedTypes(types);
        Type qualifyingType = Metamodel.getModel(container);
        if (getStatic()) {
            producedTypes.addAll(0, qualifyingType.getTypeArgumentList());
        }
        Metamodel.checkQualifyingType(qualifyingType, declaration);
        Metamodel.checkTypeArguments(qualifyingType, declaration, producedTypes);
        // find the proper qualifying type
        Type memberQualifyingType = qualifyingType.getSupertype((TypeDeclaration) declaration.getContainer());
        Reference producedReference = declaration.appliedReference(memberQualifyingType, producedTypes);
        final Type appliedType = producedReference.getType();
        return (Member<Container, Kind>) Metamodel.getAppliedMetamodel(appliedType);
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }

    public FunctionDeclarationImpl findMethod(String name) {
        NestableDeclarationImpl decl = this.findDeclaration(null, name);
        return decl instanceof FunctionDeclarationImpl ? (FunctionDeclarationImpl)decl : null;
    }

    public FunctionDeclarationImpl findDeclaredMethod(String name) {
        NestableDeclarationImpl decl = this.findDeclaredDeclaration(null, name);
        return decl instanceof FunctionDeclarationImpl ? (FunctionDeclarationImpl)decl : null;
    }

    public ValueDeclarationImpl findValue(String name) {
        NestableDeclarationImpl decl = this.findDeclaration(null, name);
        return decl instanceof ValueDeclarationImpl ? (ValueDeclarationImpl)decl : null;
    }

    public ValueDeclarationImpl findDeclaredValue(String name) {
        NestableDeclarationImpl decl = this.findDeclaredDeclaration(null, name);
        return decl instanceof ValueDeclarationImpl ? (ValueDeclarationImpl)decl : null;
    }

    public ClassOrInterfaceDeclarationImpl findType(String name) {
        NestableDeclarationImpl decl = this.findDeclaration(null, name);
        return decl instanceof ClassOrInterfaceDeclarationImpl ? (ClassOrInterfaceDeclarationImpl)decl : null;
    }

    public ClassOrInterfaceDeclarationImpl findDeclaredType(String name) {
        NestableDeclarationImpl decl = this.findDeclaredDeclaration(null, name);
        return decl instanceof ClassOrInterfaceDeclarationImpl ? (ClassOrInterfaceDeclarationImpl)decl : null;
    }

    public <T extends NestableDeclarationImpl> T findDeclaration(@Ignore TypeDescriptor $reifiedT, String name) {
        checkInit();
        return findDeclaration($reifiedT, name, declarations);
    }

    public <T extends NestableDeclarationImpl> T findDeclaredDeclaration(@Ignore TypeDescriptor $reifiedT, String name) {
        checkInit();
        return findDeclaration($reifiedT, name, declaredDeclarations);
    }

    @SuppressWarnings("unchecked")
    <T extends NestableDeclarationImpl> T findDeclaration(@Ignore TypeDescriptor $reifiedT, String name,
            List<ceylon.language.meta.declaration.NestableDeclaration> declarations) {
        for(ceylon.language.meta.declaration.NestableDeclaration decl : declarations){
            // skip anonymous types which can't be looked up by name
            if(decl instanceof ceylon.language.meta.declaration.ClassDeclaration
                    && ((ceylon.language.meta.declaration.ClassDeclaration) decl).getAnonymous())
                continue;
            // in theory we can't have several members with the same name so no need to check the type
            // FIXME: interop and overloading
            if(name.equals(decl.getName()))
                return (T) decl;
        }
        return null;
    }
    
    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations$() {
        return Metamodel.getJavaClass(declaration).getAnnotations();
    }
    
    @Override
    @Ignore
    public boolean $isAnnotated$(java.lang.Class<? extends java.lang.annotation.Annotation> annotationType) {
        final AnnotatedElement element = Metamodel.getJavaClass(declaration);
        return element != null ? element.isAnnotationPresent(annotationType) : false;
    }
    
    @Override
    public <AnnotationType extends java.lang.annotation.Annotation> boolean annotated(TypeDescriptor reifed$AnnotationType) {
        return Metamodel.isAnnotated(reifed$AnnotationType, this);
    }
    
    /**
     * Used by the SDK's ceylon.interop.java
     */
    public java.lang.Class<?> getJavaClass(){
        return Metamodel.getJavaClass(declaration);
    }
}
