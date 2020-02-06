package com.github.anilople.javajvm.classpath;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ClassContextFactory {

    /**
     * Path -> ClassContext
     * Because ClassContext do not modify and read only,
     * so we can cache it to forbid {@code java.nio.file.FileSystemException: .: Too many open files}
     */
    private static final Map<Path, ClassContext> path2ClassContextCaches = new ConcurrentHashMap<>();

    static ClassContext getInstance(Path path) {
        if(!path2ClassContextCaches.containsKey(path)) {
            String pathname = path.toString();
            if (pathname.endsWith(".class")) {
                path2ClassContextCaches.put(path, new ClassFileClassContextImpl(path));
            } else if (
                    pathname.endsWith(".jar") || pathname.endsWith(".JAR")
                            || pathname.endsWith(".zip") || pathname.endsWith(".ZIP")) {
                path2ClassContextCaches.put(path, new ZipClassContextImpl(path));
            } else {
//            logger.trace("{} cannot be recognized", path);
            }
        }
        return path2ClassContextCaches.get(path);
    }

}
