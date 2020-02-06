package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.AccessFlags;
import com.github.anilople.javajvm.constants.SpecialMethods;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.utils.ClassNameConverterUtils;
import com.github.anilople.javajvm.utils.DescriptorUtils;
import com.github.anilople.javajvm.utils.JvmClassUtils;
import com.github.anilople.javajvm.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * a class in runtime,
 * generate from .class file or user define
 *
 */
public class JvmClass {

    private static final Logger logger = LoggerFactory.getLogger(JvmClass.class);

    private short accessFlags;

    private String name;

    private String superClassName;

    private String[] interfaceNames;

    private JvmConstantPool jvmConstantPool;

    /**
     * whatever static or not,
     * field's message save here
     */
    private JvmField[] jvmFields;

    /**
     * methods in this class
     */
    private JvmMethod[] jvmMethods;

    /**
     * which class loader load this class
     */
    private JvmClassLoader loader;

    private JvmClass superClass;

    private JvmClass[] interfaces;

    /**
     * static fields cache
     * to save current class's static fields's value
     */
    private LocalVariables staticFieldsValue;

    /**
     * just new a instance, not initial it
     * @param jvmClassLoader
     */
    public JvmClass(JvmClassLoader jvmClassLoader) {
        this.loader = jvmClassLoader;
    }

    /**
     * initial the jvm class from raw data,
     * for the class loader using.
     * Why delay the initialize?
     * We need to forbid the circle in graph,
     * which will cause the dead loop
     * @see ClassFile
     * @param classFile class's structure
     */
    void initial(ClassFile classFile) {
        this.accessFlags = classFile.getAccessFlags();
        this.name = classFile.getClassName();
        this.superClassName = classFile.existSuperClass() ? classFile.getSuperClassName() : null;
        this.interfaceNames = classFile.getInterfaceNames();
        this.jvmConstantPool = new JvmConstantPool(this, classFile.getConstantPool());
        this.jvmFields = JvmField.generateJvmFields(this, classFile.getFields());
        this.jvmMethods = JvmMethod.generateJvmMethods(this, classFile.getMethods());

        // load super class
        if(null != this.superClassName) {
            logger.debug("load super class : {}", this.superClassName);
            this.superClass = this.getLoader().loadClass(classFile.getSuperClassName());
        } else {
            logger.debug("class {} have no super class", this.name);
            this.superClass = null;
        }

        // interfaces from class loader and interface names
        interfaces = new JvmClass[this.interfaceNames.length];
        for(int i = 0; i < this.interfaceNames.length; i++) {
            interfaces[i] = this.getLoader().loadClass(this.interfaceNames[i]);
        }

        // static fields value
        this.staticFieldsValue = new LocalVariables(JvmClassUtils.getStaticFieldsSize(this));
    }

    /**
     * for the array class, base type's class
     * @param jvmClassLoader
     */
    public JvmClass(JvmClassLoader jvmClassLoader, String className) {
        this.accessFlags = -1;
        this.name = className;
        this.superClassName = null;
        this.interfaceNames = new String[0];
        this.jvmConstantPool = null;
        this.jvmFields = new JvmField[0];
        this.jvmMethods = new JvmMethod[0];
        this.loader = jvmClassLoader;
        this.superClass = null;
        this.interfaces = new JvmClass[0];
        this.staticFieldsValue = null;
    }

    /**
     * judge method exist in this class or not
     * @param name
     * @param descriptor
     * @return
     */
    public boolean existMethod(String name, String descriptor) {
        return JvmClassUtils.existMethod(this, name, descriptor);
    }

    /**
     *
     * @param fieldName
     * @return exist field in current class or not (not include super class's fields!)
     */
    private boolean existsField(String fieldName) {
        for(JvmField jvmField : this.getJvmFields()) {
            if(jvmField.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param fieldName
     * @return exist field in current class or not (include super class's fields)
     */
    public boolean existsFieldIncludeAncestors(String fieldName) {
        for(JvmClass now = this; null != now; now = now.getSuperClass()) {
            if(now.existsField(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * find the field by name given
     * @param fieldName
     * @throws RuntimeException if there are no fields match
     * @return
     */
    public JvmField getJvmFieldByNameIncludeAncestors(String fieldName) {
        for(JvmClass now = this; null != now; now = now.getSuperClass()) {
            for(JvmField jvmField : now.getJvmFields()) {
                if(jvmField.getName().equals(fieldName)) {
                    return jvmField;
                }
            }
        }
        throw new RuntimeException("field " + fieldName + " not in class " + this);
    }

    /**
     * get method in this class
     * @param name
     * @param descriptor
     * @return
     */
    public JvmMethod getMethod(String name, String descriptor) {
        return JvmClassUtils.getMethod(this, name, descriptor);
    }

    /**
     *
     * @return all field from ancestor to this class in order
     */
    public List<JvmField> getNonStaticJvmFieldsFromAncestorsInOrder() {
        List<JvmField> nonStaticJvmFieldsFromAncestors = new ArrayList<>();
        for(JvmClass jvmClass : JvmClassUtils.getInheritedClassesChain(this)) {
            nonStaticJvmFieldsFromAncestors.addAll(jvmClass.getNonStaticFields());
        }
        return nonStaticJvmFieldsFromAncestors;
    }

    /**
     *
     * @return now static fields occupy size in this class and its super classes
     */
    public int getNonStaticFieldsSize() {
        List<JvmClass> jvmClasses = JvmClassUtils.getInheritedClassesChain(this);

        int number = 0;
        for(JvmClass jvmClass : jvmClasses) {
            number += JvmClassUtils.getNonStaticFieldsSize(jvmClass);
        }

        return number;
    }

    /**
     *
     * @return the non static fields of this class
     */
    public List<JvmField> getNonStaticFields() {
        List<JvmField> nonStaticFields = new ArrayList<>();
        for(JvmField jvmField : this.getJvmFields()) {
            if(!jvmField.isStatic()) {
                nonStaticFields.add(jvmField);
            }
        }
        return nonStaticFields;
    }

    /**
     *
     * @return static fields occupy size in this class and its super classes
     */
    public int getStaticFieldsSize() {
        List<JvmClass> jvmClasses = JvmClassUtils.getInheritedClassesChain(this);

        int number = 0;
        for(JvmClass jvmClass : jvmClasses) {
            number += JvmClassUtils.getStaticFieldsSize(jvmClass);
        }

        return number;
    }

    /**
     * if this class exists field, return true;
     * else return false.
     * @param jvmField
     * @return
     */
    public boolean exists(JvmField jvmField) {
        return JvmClassUtils.exists(this, jvmField);
    }

    /**
     * @return true if this class exist super class
     */
    public boolean existsSuperClass() {
        return null != this.superClass;
    }

    /**
     * check this class exist "<clinit>" method or not
     * @return
     */
    public boolean existsStaticInitialMethod() {
        for(JvmMethod jvmMethod : this.getJvmMethods()) {
            if(SpecialMethods.CLINIT.equals(jvmMethod.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @throws RuntimeException if there is no "<clinit>" method
     * @return "<clinit>" method
     */
    public JvmMethod getStaticInitialMethod() {
        for(JvmMethod jvmMethod : this.getJvmMethods()) {
            if(SpecialMethods.CLINIT.equals(jvmMethod.getName())) {
                return jvmMethod;
            }
        }
        throw new RuntimeException(SpecialMethods.CLINIT + " doesn't exists in class " + this.getName());
    }

    /**
     * When evaluate jvmClass.getName(), get string like
     * com/github/anilople/javajvm/instructions/references/NEWTest
     * but
     * When evaluate NEWTest.class.getName(), get string like
     * com.github.anilople.javajvm.instructions.references.NEWTest
     *
     * But they are same class, so we need a method to
     * judge that
     * @param clazz
     * @return
     */
    public boolean isSameName(Class<?> clazz) {
        return this.getName().equals(JvmClassUtils.getStandardRuntimeClassName(clazz));
    }

    public Class<?> getRealClassInJvm() {
        // maybe "char" etc..
        final String javaClassName = ClassNameConverterUtils.jvm2java(this.getName());
        try {
            // but Class.forName("char") not work, must use Class.getPrimitiveType("char")
            if(this.isPrimitiveType()) {
                return ReflectionUtils.getPrimitiveClass(javaClassName);
            } else {
                return Class.forName(javaClassName);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * We know that int.class void.class boolean.class ... are primitive class
     * java.lang.Object, java.lang.Integer, ... are not primitive class
     * @return this jvm class is primitive class or not
     */
    public boolean isPrimitiveType() {
        switch (this.getName()) {
            case "boolean":
            case "byte":
            case "short":
            case "char":
            case "int":
            case "long":
            case "float":
            case "double":
            case "void":
                return true;
            default:
                return false;
        }
    }

    /**
     * if class is an ordinary (nonarray) class,
     * return true
     * @return
     */
    public boolean isOrdinary() {
        return !DescriptorUtils.isArrayType(this.getName());
    }

    /**
     * primitive data, like int, void,
     * have their own class name,
     * the the class type like {@code java.lang.Object}
     * have their own class name too.
     * But the class name between them are different
     * @return true if this class is a class type
     */
    public boolean isClassType() {
        if(this.isArrayType() || this.isInterface()) {
            return false;
        }
        // non array type and first char of name is
        if(this.existsSuperClass()) {
            // exists super class mean it is inherits from other class type
            // so it is class type too
            return true;
        } else {
            // note that java/lang/Object has no super class,
            // but it is class type too
            return this.isSameName(Object.class);
        }
    }

    /**
     * array type mean name start with '['
     * @return
     */
    public boolean isArrayType() {
        return DescriptorUtils.isArrayType(this.getName());
    }

    /**
     * the jvm class given is the ancestor of this class or not
     * When the given class is equal with this class, return false !!!
     * this method also can use between interface's inheritance
     * @param jvmClass
     * @return
     */
    public boolean isInheritFrom(JvmClass jvmClass) {
        JvmClass now = this;
        while(now.existsSuperClass()) {
            JvmClass superClass = now.getSuperClass();
            if(superClass.equals(jvmClass)) {
                return true;
            } else {
                now = superClass;
            }
        }
        return false;
    }

    /**
     *
     * @param clazz
     * @return this class in inherits from the class given or not
     */
    public boolean isSubClassOf(Class<?> clazz) {
        JvmClass jvmClass = this.getLoader().loadClass(clazz);
        return isInheritFrom(jvmClass);
    }

    public boolean isSubClassOf(JvmClass jvmClass) {
        return this.isInheritFrom(jvmClass);
    }

    /**
     * this class is implement then interface class given or not
     * @param interfaceClass
     * @return
     */
    public boolean isImplementInterface(JvmClass interfaceClass) {
        // traversal to all ancestors
        for(JvmClass now = this; null != now; now = now.getSuperClass()) {
            JvmClass[] nowInterfaces = now.getInterfaces();
            // traversal all interfaces of now class
            for(JvmClass nowInterface : nowInterfaces) {
                if(nowInterface.equals(interfaceClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * wrapper array of this class
     * java.lang.Object -> [Ljava.lang.Object;
     * int              -> [I
     * [I               -> [[I
     * @return
     */
    public JvmClass wrapperArray() {
        Class<?> thisClass = this.getRealClassInJvm();
        Class<?> arrayClass = ReflectionUtils.wrapperArrayOf(thisClass);
        return this.getLoader().loadClass(arrayClass);
    }

    public boolean isPublic() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_PUBLIC);
    }

    public boolean isFinal() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_FINAL);
    }

    public boolean isSuper() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_SUPER);
    }

    public boolean isInterface() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_INTERFACE);
    }

    public boolean isAbstract() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_ABSTRACT);
    }

    public boolean isSynthetic() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_SYNTHETIC);
    }

    public boolean isAnnotation() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_ANNOTATION);
    }

    public boolean isEnum() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_ENUM);
    }

    @Override
    public String toString() {
        return "JvmClass{" +
                "name='" + name + '\'' +
                ", superClassName='" + superClassName + '\'' +
                ", interfaceNames=" + Arrays.toString(interfaceNames) +
                '}';
    }

    /**
     * @see #getJavaLevelClassName()
     * @return Class's name in jvm level
     */
    public String getName() {
        return name;
    }

    /**
     * Class's name is different between java level and jvm level.
     * In java level, {@code java.lang.Integer.class.getName()} is "java.lang.Integer",
     * But in jvm level, it will be "java/lang/Integer".
     * @return Class's name in java level
     */
    public String getJavaLevelClassName() {
        String jvmLevelClassName = this.getName();
        return ClassNameConverterUtils.jvm2java(jvmLevelClassName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JvmClass jvmClass = (JvmClass) o;
        if(this.getName().equals(jvmClass.getName())) {
            // same name? They should be identical
            throw new IllegalStateException("same type but not same object address, may exist some problems with class loader");
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(accessFlags, getName(), getSuperClassName(), getJvmConstantPool(), getLoader(), getSuperClass(), getStaticFieldsValue());
        result = 31 * result + Arrays.hashCode(getInterfaceNames());
        result = 31 * result + Arrays.hashCode(getJvmFields());
        result = 31 * result + Arrays.hashCode(getJvmMethods());
        result = 31 * result + Arrays.hashCode(getInterfaces());
        return result;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public String[] getInterfaceNames() {
        return interfaceNames;
    }

    public JvmConstantPool getJvmConstantPool() {
        return jvmConstantPool;
    }

    public JvmField[] getJvmFields() {
        return jvmFields;
    }

    public JvmMethod[] getJvmMethods() {
        return jvmMethods;
    }

    public JvmClassLoader getLoader() {
        return loader;
    }

    public JvmClass getSuperClass() {
        return superClass;
    }

    public JvmClass[] getInterfaces() {
        return interfaces;
    }

    public LocalVariables getStaticFieldsValue() {
        return staticFieldsValue;
    }
}
