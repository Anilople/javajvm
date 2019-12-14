package com.github.anilople.javajvm.classpath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * class file warpper
 * class file mean that exist suffix ".class"
 */
public class ClassFileClassContextImpl implements ClassContext {

    private static final Logger logger = LoggerFactory.getLogger(ClassFileClassContextImpl.class);

    private Path classfile;

    private ClassFileClassContextImpl() {
    }

    public ClassFileClassContextImpl(Path classfile) {
        this.classfile = classfile;
    }

    @Override
    public byte[] readClass(String className) {
        // damn file separator, '\' in windows, but '/' in linux and class file
        String classfilePathName = classfile.toString();
        String windowPathName = classfilePathName.replace('/', '\\');
        String linuxPathName = classfilePathName.replace('\\', '/');
        if (windowPathName.endsWith(className) || linuxPathName.endsWith(className)) {
            try {
                logger.debug("{} is in {}", className, classfile);
                return Files.readAllBytes(classfile);
            } catch (IOException e) {
                logger.debug("cannot read content", e);
            }
        } else {

        }

//        logger.trace("class {} not match with class file {}", className, classfile);
        return null;
    }
}
