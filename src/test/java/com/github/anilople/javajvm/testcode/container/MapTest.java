package com.github.anilople.javajvm.testcode.container;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * test container map in self jvm
 * @see java.util.Map
 */
public class MapTest {

    public static void main(String[] args) {
        hashMapPut();
        reverseKeyValue();
    }

    private static void hashMapPut() {
        // name -> age
        Map<String, Integer> name2age = new HashMap<>();
        name2age.put("C", 3);
        name2age.put("A", 1);
        name2age.put("B", 2);
        name2age.put("D", 4);
        name2age.put("DD", 4);
        System.out.println(name2age);
    }

    @Test
    void hashMapPutTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "hashMapPut",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }

    private static void treeMapPut() {
        Map<String, Integer> name2age = new TreeMap<>();
        name2age.put("C", 3);
        name2age.put("A", 1);
        name2age.put("DD", 4);
        System.out.println(name2age);
    }

    @Test
    void treeMapPutTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "treeMapPut",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }

    private static void stringValueOfMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        String s = String.valueOf(map);
        System.out.println(s);
    }

    @Test
    void stringValueOfMapTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "stringValueOfMap",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }

    private static void hashMapToStringImplicitly() {
        // name -> age
        Map<String, Integer> name2age = new HashMap<>();
        name2age.put("A", 1);
        System.out.println("before reverse: " + name2age.toString());
    }

    @Test
    void hashMapToStringImplicitlyTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "hashMapToStringImplicitly",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }

    private static void reverseKeyValue() {
        // name -> age
        Map<String, Integer> name2age = new HashMap<>();
        name2age.put("C", 3);
        name2age.put("A", 1);
        name2age.put("B", 2);
        name2age.put("D", 4);
        name2age.put("DD", 4);
        System.out.println("before reverse: " + name2age.toString());

        // age -> name
        // will lose some record
        Map<Integer, String> age2name = new TreeMap<>();
        for(Map.Entry<String, Integer> entry : name2age.entrySet()) {
            String name = entry.getKey();
            Integer age = entry.getValue();
            age2name.put(age, name);
        }

        System.out.println("after reverse: " + age2name.toString());
    }

    @Test
    void reverseKeyValueTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "reverseKeyValue",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }
}
