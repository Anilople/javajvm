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
    ObjectReference(JvmClass jvmClass) {
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

    /**
     * Why not use constructor method?
     * Because the special class {@code java.lang.Class},
     * it must use {@code ClassObjectReference} to represent.
     * @see java.lang.Class
     * @param jvmClass
     * @return
     */
    public static ObjectReference makeObjectReference(JvmClass jvmClass) {
        if(jvmClass.isSameName(java.lang.Class.class)) {
            return ClassObjectReference.getInstance(jvmClass);
        } else {
            return new ObjectReference(jvmClass);
        }
    }

    /**
     * give a field name, find a reference which name is same as given name
     * @param fieldName
     * @throws RuntimeException if there is no field
     * @return reference named by given name
     */
    public Reference getReference(String fieldName) {
        final JvmClass jvmClass = this.getJvmClass();
        if(jvmClass.existsFieldIncludeAncestors(fieldName)) {
            JvmField jvmField = jvmClass.getJvmFieldByNameIncludeAncestors(fieldName);
            if(jvmField.isStatic()) {
                int staticFieldOffset = jvmField.getStaticFieldOffset();
                return jvmField.getJvmClass().getStaticFieldsValue().getReference(staticFieldOffset);
            } else {
                int index = jvmField.calculateNonStaticFieldOffset();
                return this.getReference(index);
            }
        } else {
            throw new RuntimeException(fieldName + " not in class " + jvmClass);
        }
    }

    /**
     * give a field name and a reference,
     * set the reference to the field
     * @param fieldName
     * @param reference
     * @throws RuntimeException if there is no field
     */
    public void setReference(String fieldName, Reference reference) {
        final JvmClass jvmClass = this.getJvmClass();
        if(jvmClass.existsFieldIncludeAncestors(fieldName)) {
            JvmField jvmField = jvmClass.getJvmFieldByNameIncludeAncestors(fieldName);
            if(jvmField.isStatic()) {
                int staticFieldOffset = jvmField.getStaticFieldOffset();
                JvmClass classFieldBelongTo = jvmField.getJvmClass();
                classFieldBelongTo.getStaticFieldsValue().setReference(staticFieldOffset, reference);
            } else {
                int index = jvmField.calculateNonStaticFieldOffset();
                this.setReference(index, reference);
            }
        } else {
            throw new RuntimeException(fieldName + " not in class " + jvmClass);
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
