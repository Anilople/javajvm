package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.AccessFlags;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
        this.staticFieldsValue = new LocalVariables(JvmClass.getStaticFieldsSize(this));
    }



    /**
     * fields not only in now class,
     * but also in super classes
     *
     * @return all fields in spuer classes and now class with offset order
     */
    public static List<JvmField> getAllJvmFields(JvmClass nowClass) {
        // inherit chain
        List<JvmClass> jvmClasses = JvmClass.getInheritedClassesChain(nowClass);

        // all fields save here
        List<JvmField> allFields = new ArrayList<>();

        // Traversing from ancestor to now class
        for(JvmClass jvmClass : jvmClasses) {
            // traversing fields in this class
            for(JvmField jvmField : jvmClass.getJvmFields()) {
                allFields.add(jvmField);
            }
        }

        return allFields;
    }

    /**
     * there is a path from Object class to now class
     *
     * @return the path in order (from ancestor to now)
     */
    public static List<JvmClass> getInheritedClassesChain(JvmClass jvmClass) {
        List<JvmClass> chain = new ArrayList<>(4);

        // add class, from child to ancestor
        for(JvmClass nowClass = jvmClass; null != nowClass; nowClass = nowClass.getSuperClass()) {
            chain.add(nowClass);
        }

        // reverse, make order with ancestor to child
        Collections.reverse(chain);

        return chain;
    }

    /**
     * how many non static fields occupy size in now class ?
     * (just in now class, not in super class)
     *
     * It can optimize use cache
     *
     * @return non static fields occupy size just in now class
     */
    public static int getNonStaticFieldsSize(JvmClass nowClass) {
        int number = 0;

        // remember that long and double occupies 2 * 4 bytes
        for(JvmField jvmField : nowClass.getJvmFields()) {
            if(!jvmField.isStatic()) {
                number += jvmField.getSize();
            }
        }

        return number;
    }

    /**
     * how many static fields occupy size in now class ?
     * (just in now class, not in super class)
     *
     * It can optimize use cache
     *
     * @return static fields occupy size just in now class
     */
    public static int getStaticFieldsSize(JvmClass nowClass) {
        List<JvmField> allFields = JvmClass.getAllJvmFields(nowClass);

        int number = 0;

        // remember that long and double occupies 2 * 4 bytes
        for(JvmField jvmField : allFields) {
            if(jvmField.isStatic()) {
                number += jvmField.getSize();
            }
        }

        return number;
    }

    /**
     *
     * @param jvmClass a class
     * @param accessFlag method's access flag
     * @return methods with access flag
     */
    private static List<JvmMethod> getMethodsWith(JvmClass jvmClass, short accessFlag) {
        List<JvmMethod> methods = new ArrayList<>();
        for(JvmMethod jvmMethod : jvmClass.getJvmMethods()) {
            if(0 != (jvmMethod.getAccessFlags() & accessFlag)) {
                // match this access flag
                methods.add(jvmMethod);
            }
        }
        return methods;
    }

    /**
     *
     * @param jvmClass
     * @return static methods
     */
    public static List<JvmMethod> getStaticMethods(JvmClass jvmClass) {
        return JvmClass.getMethodsWith(jvmClass, AccessFlags.MethodFlags.ACC_STATIC);
    }

    /**
     *
     * @param jvmClass
     * @return non static methods
     */
    public static List<JvmMethod> getNonStaticMethods(JvmClass jvmClass) {
        List<JvmMethod> nonStaticMethods = new ArrayList<>();
        for(JvmMethod jvmMethod : jvmClass.getJvmMethods()) {
            if(!jvmMethod.isStatic()) {
                // non static method
                nonStaticMethods.add(jvmMethod);
            }
        }
        return nonStaticMethods;
    }

    /**
     * get a method from class(include super classes) whether it is static or not
     * @param jvmClass
     * @param name
     * @param descriptor
     * @return null if method does not existed
     */
    public static JvmMethod getMethod(JvmClass jvmClass, String name, String descriptor) {
        for(JvmClass nowClass = jvmClass; null != nowClass; nowClass = nowClass.getSuperClass()) {
            for(JvmMethod jvmMethod : nowClass.getJvmMethods()) {
                if(jvmMethod.getName().equals(name) && jvmMethod.getDescriptor().equals(descriptor)) {
                    return jvmMethod;
                }
            }
        }
        return null;
    }

    /**
     * judge method exist in a class(include super classes) or not
     * @param jvmClass
     * @param name
     * @param descriptor
     * @return
     */
    public static boolean existMethod(JvmClass jvmClass, String name, String descriptor) {
        for(JvmClass nowClass = jvmClass; null != nowClass; nowClass = nowClass.getSuperClass()) {
            for(JvmMethod jvmMethod : nowClass.getJvmMethods()) {
                if(jvmMethod.getName().equals(name) && jvmMethod.getDescriptor().equals(descriptor)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * judge method exist in this class or not
     * @param name
     * @param descriptor
     * @return
     */
    public boolean existMethod(String name, String descriptor) {
        return JvmClass.existMethod(this, name, descriptor);
    }

    /**
     * get method in this class
     * @param name
     * @param descriptor
     * @return
     */
    public JvmMethod getMethod(String name, String descriptor) {
        return JvmClass.getMethod(this, name, descriptor);
    }

    /**
     *
     * @return now static fields occupy size in this class and its super classes
     */
    public int getNonStaticFieldsSize() {
        List<JvmClass> jvmClasses = JvmClass.getInheritedClassesChain(this);

        int number = 0;
        for(JvmClass jvmClass : jvmClasses) {
            number += JvmClass.getNonStaticFieldsSize(jvmClass);
        }

        return number;
    }

    /**
     *
     * @return static fields occupy size in this class and its super classes
     */
    public int getStaticFieldsSize() {
        List<JvmClass> jvmClasses = JvmClass.getInheritedClassesChain(this);

        int number = 0;
        for(JvmClass jvmClass : jvmClasses) {
            number += JvmClass.getStaticFieldsSize(jvmClass);
        }

        return number;
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
