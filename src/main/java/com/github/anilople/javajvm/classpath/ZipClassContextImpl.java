package com.github.anilople.javajvm.classpath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.anilople.javajvm.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * read class from a compressed file
 * .jar file
 */
class ZipClassContextImpl implements ClassContext {

    private static final Logger logger = LoggerFactory.getLogger(ZipClassContextImpl.class);

    private ZipFile zipFile;

    private ZipClassContextImpl() {
    }

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
        if (null != zipEntry) {
            logger.debug("class {} is in zipfile {}", className, zipFile.getName());
            try {
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                // read all bytes(not consider large memory)
                return IOUtils.readNBytes(inputStream, Integer.MAX_VALUE);
            } catch (IOException e) {
                logger.debug("cannot read {} from {}", className, zipFile.getName());
                throw new RuntimeException(e);
            }
        } else {
//            logger.trace("class {} is not in zipfile {}", className, zipFile.getName());
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
