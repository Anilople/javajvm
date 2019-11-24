package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantFieldrefInfo;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * reference to a field in a class
 */
public class JvmConstantFieldref extends JvmConstant {

    private static final Logger logger = LoggerFactory.getLogger(JvmConstantFieldref.class);

    private ConstantFieldrefInfo constantFieldrefInfo;

    private JvmConstantFieldref() {

    }

    public JvmConstantFieldref(JvmClass jvmClass, ConstantFieldrefInfo constantFieldrefInfo) {
        super(jvmClass);
        this.constantFieldrefInfo = constantFieldrefInfo;
    }

    /**
     * @return which class this field belong to
     */
    public String getClassName() {
        int classIndex = constantFieldrefInfo.getClassIndex();
        JvmConstantClass jvmConstantClass = (JvmConstantClass) this.getJvmClass().getJvmConstantPool().getJvmConstant(classIndex);
        return jvmConstantClass.getName();
    }

    /**
     * @return field's name
     */
    public String getFieldName() {
        int nameAndTypeIndex = constantFieldrefInfo.getNameAndTypeIndex();
        JvmConstantNameAndType jvmConstantNameAndType
                = (JvmConstantNameAndType) this.getJvmClass().getJvmConstantPool().getJvmConstant(nameAndTypeIndex);
        return jvmConstantNameAndType.getName();
    }

    /**
     * @return field's type
     */
    public String getFieldDescriptor() {
        int nameAndTypeIndex = constantFieldrefInfo.getNameAndTypeIndex();
        JvmConstantNameAndType jvmConstantNameAndType
                = (JvmConstantNameAndType) this.getJvmClass().getJvmConstantPool().getJvmConstant(nameAndTypeIndex);
        return jvmConstantNameAndType.getDescriptor();
    }

    /**
     * calculate that actual JvmField this field referenced
     * @return
     */
    public JvmField resolveJvmField() {
        String className = this.getClassName();
        JvmClass jvmClass = this.getJvmClass().getLoader().loadClass(className);
        for(JvmField jvmField : jvmClass.getJvmFields()) {
            if(this.getFieldName().equals(jvmField.getName())
            && this.getFieldDescriptor().equals(jvmField.getDescriptor())) {
                return jvmField;
            }
        }
        // may throw some error here ?
        // but class have been verifying
        logger.error("{} not exist in class {}", this, jvmClass);
        throw new RuntimeException();
    }
}
