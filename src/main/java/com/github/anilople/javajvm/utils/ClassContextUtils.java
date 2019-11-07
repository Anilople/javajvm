package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.classpath.ClassContext;
import com.github.anilople.javajvm.classpath.ClassFileClassContextImpl;
import com.github.anilople.javajvm.classpath.ZipClassContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * utils for handling something about ClassContext
 */
public class ClassContextUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassContextUtils.class);

    /**
     * forbidden to pass a path of directory
     *
     * @param path a file, maybe endsWith ".class" or ".jar" or other
     * @return ClassContext
     */
    public static ClassContext newClassContext(Path path) {
        String pathname = path.toString();
        if (pathname.endsWith(".class")) {
            return new ClassFileClassContextImpl(path);
        } else if (
                pathname.endsWith(".jar") || pathname.endsWith(".JAR")
                        || pathname.endsWith(".zip") || pathname.endsWith(".ZIP")) {
            return new ZipClassContextImpl(path);
        } else {
            logger.debug("{} cannot be recognized", path);
            return null;
        }
    }

    public static byte[] readClass(Iterable<ClassContext> classContexts, String className) throws IOException {
        for (ClassContext classContext : classContexts) {
            byte[] data = classContext.readClass(className);
            if (null != data) {
                return data;
            }
        }
        logger.error("{} not in {}", className, classContexts);
        return null;
    }

    /**
     * @param pathname one path or multiple path split by path separator
     * @return all path under pathname
     */
    public static Collection<Path> getAllPathNested(String pathname) {

        // multiple paths
        if (pathname.contains(File.pathSeparator)) {
            return Arrays.asList(pathname.split(File.pathSeparator))
                    .stream()
                    .flatMap(s -> getAllPathNested(s).stream())
                    .collect(Collectors.toSet());
        }

        // wildcard *
        if (pathname.contains("*")) {
            return getAllPathNested(pathname.substring(0, pathname.length() - 1));
        }


        // make sure that there is no path separator here
        Path path = Paths.get(pathname);
        return getAllPathNested(path);
    }

    /**
     * @param path a file path or a directory path
     * @return all paths under path
     */
    private static Collection<Path> getAllPathNested(Path path) {
        if (Files.isDirectory(path)) {
            try {
                return Files.list(path)
                        .flatMap(p -> getAllPathNested(p).stream())
                        .collect(Collectors.toSet());
            } catch (IOException e) {
                logger.error("file list fail", e);
            }
            return Collections.EMPTY_SET;
        } else {
            // is a file
            return Collections.singleton(path);
        }
    }

    /**
     * get a java runtime environment directory
     *
     * @param Xjre
     * @return a jre directory
     */
    public static String getJreDirectory(String Xjre) {
        // jre has existed
        if (null != Xjre) {
            return Xjre;
        }

        // from local directory ./jre
        if (new File("jre").exists()) {
            return new File("jre").getPath();
        }

        // from environment variable
        String javaHome = System.getProperty("JAVA_HOME");
        if (null != javaHome) {
            return String.join(File.separator, javaHome, "jre");
        }
        logger.error("no jre");
        return null;
    }
}
