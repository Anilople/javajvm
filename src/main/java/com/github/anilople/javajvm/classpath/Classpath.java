package com.github.anilople.javajvm.classpath;

import com.github.anilople.javajvm.command.Command;
import com.github.anilople.javajvm.utils.ClassContextUtils;
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

    private Classpath() {}

    /**
     * initial a class context from command
     * @param command
     */
    public Classpath(Command command) {
        String jreDirectory = ClassContextUtils.getJreDirectory(command.getOptions().getXjre());

        // jre/lib/*
        String jrelibDirectory = String.join(File.separator, jreDirectory, "lib", "*");
        Collection<Path> jrelibPaths = ClassContextUtils.getAllPathNested(jrelibDirectory);
        this.bootList = jrelibPaths
                .stream()
                .map(ClassContextUtils::newClassContext)
                .filter(classContext -> null != classContext)
                .collect(Collectors.toList());

        // jre/lib/ext/*
        String jrelibextDirectory = String.join(File.separator, jreDirectory, "lib", "ext", "*");
        Collection<Path> jrelibextPaths = ClassContextUtils.getAllPathNested(jrelibextDirectory);
        this.extList = jrelibextPaths
                .stream()
                .map(ClassContextUtils::newClassContext)
                .filter(classContext -> null != classContext)
                .collect(Collectors.toList());

        // user class
        Collection<Path> userPaths = ClassContextUtils.getAllPathNested(".");
        this.userList = userPaths
                .stream()
                .map(ClassContextUtils::newClassContext)
                .filter(classContext -> null != classContext)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] readClass(String className) {
        // add ".class" suffix
        className = className + ".class";

        // from boot
        for(ClassContext classContext : bootList) {
            byte[] data = classContext.readClass(className);
            if(null != data) {
                return data;
            }
        }

        // from ext
        for(ClassContext classContext : extList) {
            byte[] data = classContext.readClass(className);
            if(null != data) {
                return data;
            }
        }

        // from user
        for(ClassContext classContext : userList) {
            byte[] data = classContext.readClass(className);
            if(null != data) {
                return data;
            }
        }

        logger.error("{} not exist", className);
        return null;
    }
}
