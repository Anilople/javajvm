package com.github.anilople.javajvm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * utils for handling something about ClassContext
 */
public class ClassPathUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassPathUtils.class);

    /**
     * cache the results to forbid
     * {@code
     *      java.nio.file.FileSystemException: /usr/lib/jvm/java-8-openjdk-amd64/jre/lib: Too many open files
     * }
     */
    private static final Map<String, Collection<Path>> pathname2PathCollection = new ConcurrentHashMap<>();

    /**
     * @param pathname one path or multiple path split by path separator
     * @return all path under pathname
     */
    public static Collection<Path> getAllPathNested(String pathname) {
        if(!pathname2PathCollection.containsKey(pathname)) {
            if (pathname.contains(File.pathSeparator)) {
                // multiple paths
                Collection<Path> pathCollection = Arrays.stream(pathname.split(File.pathSeparator))
                        .flatMap(s -> getAllPathNested(s).stream())
                        .collect(Collectors.toSet());
                pathname2PathCollection.put(pathname, pathCollection);
            } else if (pathname.contains("*")) {
                // wildcard *
                Collection<Path> pathCollection = getAllPathNested(pathname.substring(0, pathname.length() - 1));
                pathname2PathCollection.put(pathname, pathCollection);
            } else {
                // make sure that there is no path separator here
                Path path = Paths.get(pathname);
                pathname2PathCollection.put(pathname, getAllPathNested(path));
            }
        }

        return pathname2PathCollection.get(pathname);
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
            return new HashSet<>();
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
