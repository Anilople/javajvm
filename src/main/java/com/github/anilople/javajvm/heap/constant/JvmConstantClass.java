package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantClassInfo;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;

public class JvmConstantClass extends JvmConstant {

    private ConstantClassInfo constantClassInfo;

    private JvmConstantClass() {

    }

    public JvmConstantClass(JvmClass jvmClass, ConstantClassInfo constantClassInfo) {
        super(jvmClass);
        this.constantClassInfo = constantClassInfo;
    }

    /**
     *
     * @return jvm class name represent of this constant
     */
    public String getName() {
        return ConstantPoolUtils.getUtf8(
                constantClassInfo.getClassFile().getConstantPool(),
                constantClassInfo.getNameIndex()
        );
    }

    /**
     * the jvm class which this constant belong to,
     * may not be the real jvm class this constant represent!!!
     * so we need to get the class name of this constant,
     * then get real class by class loader through class name.
     * @return What jvm class this constant represent
     */
    public JvmClass resolveJvmClass() {
        String jvmClassName = this.getName();
        // remember that use "super" not "this"
        return super.getJvmClass().getLoader().loadClass(jvmClassName);
    }

    /**
     * same as {@link this#resolveJvmClass()}
     * @see this#resolveJvmClass()
     */
    @Override
    public JvmClass getJvmClass() {
        return this.resolveJvmClass();
    }
}
