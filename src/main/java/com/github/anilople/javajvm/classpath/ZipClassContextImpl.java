package com.github.anilople.javajvm.classpath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * read class from a compressed file
 */
public class ZipClassContextImpl implements ClassContext {

    private static final Logger logger = LoggerFactory.getLogger(ZipClassContextImpl.class);

    private ZipFile zipFile;

    private ZipClassContextImpl() {}

    public ZipClassContextImpl(Path path) {
        try {
            String pathname = path.toAbsolutePath().toString();
            zipFile =
                    pathname.endsWith(".zip") || pathname.endsWith(".ZIP") ? new ZipFile(pathname) : new JarFile(pathname);
        } catch (IOException e) {
            logger.error("{} cannot convert to a zip file", path);
            e.printStackTrace();
        }
    }

    @Override
    public byte[] readClass(String className) {
        // entry maybe null
        ZipEntry zipEntry = zipFile.getEntry(className);
        if(null != zipEntry) {
            return zipEntry.getExtra();
        } else {
            logger.error("class {} is not in zipfile {}", className, zipFile.getName());
            return null;
        }
    }

    @Override
    public String toString() {
        return "ZipClassContextImpl{" +
                "zipFile=" + zipFile +
                '}';
    }
}
