package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantDoubleInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantLongInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantPoolInfo;
import com.github.anilople.javajvm.heap.constant.JvmConstant;
import com.github.anilople.javajvm.heap.constant.JvmConstantUtf8;
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
            logger.trace("constant pool {} info : {}", i, constantPool[i]);
            jvmConstants[i] = JvmConstant.generateJvmConstant(jvmClass, constantPool[i]);
            if(constantPool[i] instanceof ConstantDoubleInfo) {
                logger.trace("inc i, double constant pool info {}", constantPool[i]);
                i += 1;
            } else if(constantPool[i] instanceof ConstantLongInfo) {
                logger.trace("inc i, long constant pool info {}", constantPool[i]);
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

    /**
     * suppose JvmConstant in position index is a JvmConstantUtf8
     * @param index
     * @return String content
     */
    public String getUtf8String(int index) {
        JvmConstant jvmConstant = jvmConstants[index];
        JvmConstantUtf8 jvmConstantUtf8 = (JvmConstantUtf8) jvmConstant;
        return jvmConstantUtf8.toString();
    }
}
