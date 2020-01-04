package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmField;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.utils.DescriptorUtils;

import java.util.List;

import static com.github.anilople.javajvm.constants.Descriptors.BaseType.*;
import static com.github.anilople.javajvm.constants.Descriptors.BaseType.BOOLEAN;

public class ObjectReference extends LocalVariables implements Reference {

    private JvmClass jvmClass;

    /**
     * Initialize an object reference,
     * The default value of type,
     * jls8, 4.12.5 Initial Values of Variables, Page 87
     * @param jvmClass object ref's class
     */
    public ObjectReference(JvmClass jvmClass) {
        // an Object in run-time exist own fields (non static fields)
        super(jvmClass.getNonStaticFieldsSize());
        this.jvmClass = jvmClass;

        // initial fields with default value
        List<JvmField> jvmFields = jvmClass.getNonStaticJvmFieldsFromAncestorsInOrder();
        int fieldOffset = 0;
        for(JvmField jvmField : jvmFields) {
            // initial default value of this field
            String descriptor = jvmField.getDescriptor();
            if(DescriptorUtils.isBaseType(descriptor)) {
                switch (descriptor) {
                    case BYTE:
                        this.setByteValue(fieldOffset, (byte) 0);
                        break;
                    case CHAR:
                        this.setCharValue(fieldOffset, '\u0000');
                        break;
                    case DOUBLE:
                        this.setDoubleValue(fieldOffset, 0.0d);
                        break;
                    case FLOAT:
                        this.setFloatValue(fieldOffset, 0.0f);
                        break;
                    case INT:
                        this.setIntValue(fieldOffset, 0);
                        break;
                    case LONG:
                        this.setLongValue(fieldOffset, 0L);
                        break;
                    case SHORT:
                        this.setShortValue(fieldOffset, (short) 0);
                        break;
                    case BOOLEAN:
                        this.setBooleanValue(fieldOffset, false);
                        break;
                }
            } else {
                this.setReference(fieldOffset, Reference.NULL);
            }

            // add the offset
            fieldOffset += jvmField.getSize();
        }
    }

    @Override
    public String toString() {
        return "ObjectReference{" +
                "jvmClass=" + jvmClass +
                // circle reference due to stack over flow
//                ", super=" + super.toString() +
                '}';
    }

    public JvmClass getJvmClass() {
        return jvmClass;
    }
}
