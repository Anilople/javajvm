package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantDoubleInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantLongInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantPoolInfo;
import com.github.anilople.javajvm.heap.constant.JvmConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * run-time constant pool
 */
public class JvmConstantPool {

    private static final Logger logger = LoggerFactory.getLogger(JvmConstantPool.class);

    private JvmClass jvmClass;

    private JvmConstant[] jvmConstants;

    private JvmConstantPool() {

    }

    public JvmConstantPool(JvmClass jvmClass, ConstantPoolInfo[] constantPool) {
        this.jvmClass = jvmClass;
        jvmConstants = new JvmConstant[constantPool.length];

        // remember that from index 1
        for(int i = 1; i < jvmConstants.length; i++) {
            logger.debug("constant pool info : {}", constantPool[i]);
            jvmConstants[i] = JvmConstant.generateJvmConstant(jvmClass, constantPool[i]);
            if(constantPool[i] instanceof ConstantDoubleInfo) {
                logger.debug("inc i, double constant pool info {}", constantPool[i]);
                i += 1;
            } else if(constantPool[i] instanceof ConstantLongInfo) {
                logger.debug("inc i, long constant pool info {}", constantPool[i]);
                i += 1;
            }
        }
    }

    public JvmConstant getJvmConstant(int index) {
        JvmConstant jvmConstant = jvmConstants[index];
        if(null != jvmConstant) {
            return jvmConstant;
        }
        throw new RuntimeException("No constants at index " + index);
    }
}
