package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.classfile.FieldInfo;
import com.github.anilople.javajvm.classfile.attributes.AttributeInfo;
import com.github.anilople.javajvm.constants.AccessFlags;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;

public class JvmField extends JvmClassMember {

    public JvmField(JvmClass jvmClass, FieldInfo fieldInfo) {
        super(
                jvmClass,
                fieldInfo.getAccessFlags(),
                ConstantPoolUtils.getUtf8(fieldInfo.getClassFile().getConstantPool(), fieldInfo.getNameIndex()),
                ConstantPoolUtils.getUtf8(fieldInfo.getClassFile().getConstantPool(), fieldInfo.getDescriptorIndex()),
                fieldInfo.getAttributes()
        );
    }

    public static JvmField[] generateJvmFields(JvmClass jvmClass, FieldInfo[] fieldInfos) {
        JvmField[] jvmFields = new JvmField[fieldInfos.length];
        for(int i = 0; i < jvmFields.length; i++) {
            jvmFields[i] = new JvmField(jvmClass, fieldInfos[i]);
        }
        return jvmFields;
    }
}
