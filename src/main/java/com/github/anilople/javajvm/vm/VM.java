package com.github.anilople.javajvm.vm;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmField;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ReferenceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * JVM will initial implicitly,
 * use this class to do those things.
 * @see sun.misc.VM
 */
public class VM {

    private static final Logger logger = LoggerFactory.getLogger(VM.class);

    public static void initial(JvmClassLoader jvmClassLoader) {
        logger.info("boot vm. initialize");

        Properties properties = new Properties();
        // add something
        properties.setProperty("nothing", "nothing");

        final Reference propertiesReference;
        try {
            propertiesReference = ReferenceUtils.object2Reference(jvmClassLoader, properties);
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }

        JvmClass vm = jvmClassLoader.loadClass(sun.misc.VM.class);
        // private static final Properties savedProps;
        JvmField savedProps = vm.getJvmFieldByNameIncludeAncestors("savedProps");
        int staticFieldOffset = savedProps.getStaticFieldOffset();
        vm.getStaticFieldsValue().setReference(staticFieldOffset, propertiesReference);
    }

}
