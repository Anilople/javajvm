package com.github.anilople.javajvm.cachepool;

import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * use string pool to save string have been appear
 */
public class StringPool {

    private static final Logger logger = LoggerFactory.getLogger(StringPool.class);

    private static final Map<String, ObjectReference> pool = new ConcurrentHashMap<>();

    public static boolean exists(String string) {
        return pool.containsKey(string);
    }

    public static void add(String string, ObjectReference objectReference) {
        if(!exists(string)) {
            logger.debug("add string [{}] to pool. object reference: {}", string, objectReference);
            pool.put(string, objectReference);
        }
    }

    /**
     *
     * @param string
     * @throws RuntimeException
     * @return the cached one
     */
    public static ObjectReference get(String string) {
        if(!exists(string)) {
            throw new RuntimeException(string + " does not exists in the pool");
        }
        return pool.get(string);
    }

}
