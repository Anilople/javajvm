package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

/**
 * The BootstrapMethods attribute
 * records bootstrap method specifiers referenced by invokedynamic instructions
 * (§invokedynamic).
 */
public class BootstrapMethodsAttribute extends AttributeInfo {

//    private short numBootstrapMethods;

    private BootstrapMethod[] bootstrapMethods;

    public BootstrapMethodsAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.bootstrapMethods = BootstrapMethod.parseBootstrapMethods(new ClassFile.ClassReader(info));
    }

    public static class BootstrapMethod {

        private short bootstrapMethodRef;

//        private short numBootstrapArguments;

        private short[] bootstrapArguments;

        private BootstrapMethod() {
        }

        public static BootstrapMethod parseBootstrapMethod(ClassFile.ClassReader classReader) {
            BootstrapMethod bootstrapMethod = new BootstrapMethod();
            bootstrapMethod.bootstrapMethodRef = classReader.readU2();
            short numBootstrapArguments = classReader.readU2();
            bootstrapMethod.bootstrapArguments = classReader.readShorts(numBootstrapArguments);
            return bootstrapMethod;
        }

        public static BootstrapMethod[] parseBootstrapMethods(ClassFile.ClassReader classReader) {
            short numBootstrapMethods = classReader.readU2();
            BootstrapMethod[] bootstrapMethods = new BootstrapMethod[numBootstrapMethods];
            for (short i = 0; i < numBootstrapMethods; i++) {
                bootstrapMethods[i] = parseBootstrapMethod(classReader);
            }
            return bootstrapMethods;
        }

    }
}
