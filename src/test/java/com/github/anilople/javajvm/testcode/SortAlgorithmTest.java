package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.utils.ReflectionUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * write some sort algorithms,
 * use self JVM to run those algorithms,
 * check their correction.
 */
public class SortAlgorithmTest {

    private static List<Integer> add(List<Integer> list) {
        list.add(3);
        return list;
    }

    public static void main(String[] args) throws NoSuchFieldException {
        for(Field field : Throwable.class.getDeclaredFields()) {
            System.out.println(field);
        }
        System.out.println("##############");
        Field backtrace = Throwable.class.getDeclaredField("backtrace");
        System.out.println(backtrace);
//        System.out.println(ReflectionUtils.getNonStaticFieldsFromAncestor(IllegalAccessException.class));
//        System.out.println(Throwable.class.getDeclaredFields().length);
//        List<Integer> integers = new ArrayList<>();
//        integers.add(3);
//        for(Integer integer : integers) {
//            System.out.println(integer);
//        }
    }

    @Test
    void mainTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.run();
    }

}
