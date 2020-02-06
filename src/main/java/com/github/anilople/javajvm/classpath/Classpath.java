package com.github.anilople.javajvm.classpath;

import com.github.anilople.javajvm.utils.ClassPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * jvm classpath,
 * init it for jvm searching a class file's data
 */
public class Classpath implements ClassContext {

    private static final Logger logger = LoggerFactory.getLogger(Classpath.class);

    private static volatile Classpath INSTANCE = null;

    /**
     * class under
     * jre/lib/*
     */
    List<ClassContext> bootList;

    /**
     * class under
     * jre/lib/ext/*
     */
    List<ClassContext> extList;

    /**
     * user's classes
     * under "." directory
     */
    List<ClassContext> userList;

    /**
     * initialize {@link Classpath}
     * @throws IllegalStateException if initializes already
     */
    synchronized public static void initialize(String jreDirectory) {
        if(null != INSTANCE) {
            throw new IllegalStateException(INSTANCE + " initializes already.");
        }
        INSTANCE = new Classpath(jreDirectory);
    }

    /**
     * @return single case {@link Classpath} initializes or not
     */
    public static boolean isInitialized() {
        return null != INSTANCE;
    }

    /**
     *
     * @return single case of {@link Classpath}
     * @throws IllegalStateException if not initialize
     */
    public static Classpath getInstance() {
        if(null == INSTANCE) {
            throw new IllegalStateException("Not initializes yet.");
        }
        return INSTANCE;
    }

    /**
     * initial a class context from command
     *
     * @param jreDirectory jre path
     */
    private Classpath(String jreDirectory) {

        // jre/lib/*
        String jrelibDirectory = String.join(File.separator, jreDirectory, "lib", "*");
        Collection<Path> jrelibPaths = ClassPathUtils.getAllPathNested(jrelibDirectory);
        this.bootList = jrelibPaths
                .stream()
                .map(ClassContextFactory::getInstance)
                .filter(classContext -> null != classContext)
                .collect(Collectors.toList());

        // jre/lib/ext/*
        String jrelibextDirectory = String.join(File.separator, jreDirectory, "lib", "ext", "*");
        Collection<Path> jrelibextPaths = ClassPathUtils.getAllPathNested(jrelibextDirectory);
        this.extList = jrelibextPaths
                .stream()
                .map(ClassContextFactory::getInstance)
                .filter(classContext -> null != classContext)
                .collect(Collectors.toList());

        // user class
        Collection<Path> userPaths = ClassPathUtils.getAllPathNested(".");
        this.userList = userPaths
                .stream()
                .map(ClassContextFactory::getInstance)
                .filter(classContext -> null != classContext)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] readClass(String className) {
        // add ".class" suffix
        className = className + ".class";

        // from boot
        for (ClassContext classContext : bootList) {
            byte[] data = classContext.readClass(className);
            if (null != data) {
                return data;
            }
        }

        // from ext
        for (ClassContext classContext : extList) {
            byte[] data = classContext.readClass(className);
            if (null != data) {
                return data;
            }
        }

        // from user
        for (ClassContext classContext : userList) {
            byte[] data = classContext.readClass(className);
            if (null != data) {
                return data;
            }
        }

        logger.error("{} not exist", className);
        return null;
    }
}
