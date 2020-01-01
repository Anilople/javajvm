package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.AccessFlags;
import com.github.anilople.javajvm.constants.SpecialMethods;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.utils.DescriptorUtils;
import com.github.anilople.javajvm.utils.JvmClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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


    private JvmClass() {

    }

    public JvmClass(JvmClassLoader jvmClassLoader, ClassFile classFile) {
        this.accessFlags = classFile.getAccessFlags();
        this.name = classFile.getClassName();
        this.superClassName = classFile.existSuperClass() ? classFile.getSuperClassName() : null;
        this.interfaceNames = classFile.getInterfaceNames();
        this.jvmConstantPool = new JvmConstantPool(this, classFile.getConstantPool());
        this.jvmFields = JvmField.generateJvmFields(this, classFile.getFields());
        this.jvmMethods = JvmMethod.generateJvmMethods(this, classFile.getMethods());
        this.loader = jvmClassLoader;

        // load super class
        if(null != this.superClassName) {
            logger.debug("load super class : {}", this.superClassName);
            this.superClass = jvmClassLoader.loadClass(classFile.getSuperClassName());
        } else {
            logger.debug("class {} have no super class", this.name);
            this.superClass = null;
        }

        // interfaces from class loader and interface names
        interfaces = new JvmClass[this.interfaceNames.length];
        for(int i = 0; i < this.interfaceNames.length; i++) {
            interfaces[i] = jvmClassLoader.loadClass(this.interfaceNames[i]);
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

    /**
     * we know int.class void.class boolean.class ...
     * return true if this class is primitive class
     * @return
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
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + this.getName());
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
        if(this.isArrayType()) {
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

    public String getName() {
        return name;
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
