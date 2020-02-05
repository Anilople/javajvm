package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import org.junit.jupiter.api.Test;

/**
 * @see Object#toString()
 */
public class ToStringTest {
    
    public static void main(String[] args) {
        runParentToString();
        runChildToString();
        runAbstractParentChildToString();
        runParentToStringImplicitly();
    }

    private static void runParentToString() {
        Parent parent = new Parent();
        String parentString = parent.toString();
        System.out.println(parentString);
    }

    @Test
    void runParentToStringTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "runParentToString",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }

    private static void runChildToString() {
        Child child = new Child();
        String childString = child.toString();
        System.out.println(childString);
    }

    @Test
    void runChildToStringTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "runChildToString",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }

    private static void runAbstractParentChildToString() {
        AbstractParentChild abstractParentChild = new AbstractParentChild();
        String abstractParentChildString = abstractParentChild.toString();
        System.out.println(abstractParentChildString);
    }

    @Test
    void runAbstractParentChildToStringTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "runAbstractParentChildToString",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }

    private static void runParentToStringImplicitly() {
        Parent parent = new Parent();
        String s = "p = " + parent;
        System.out.println(s);
    }

    @Test
    void runParentToStringImplicitlyTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "runParentToStringImplicitly",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }
}

class Parent {
    @Override
    public String toString() {
        return "I'm Parent";
    }
}

class Child extends Parent {
    @Override
    public String toString() {
        return "I'm Child";
    }
}

abstract class AbstractParent {
    @Override
    public String toString() {
        return "I'm abstract Parent";
    }
}

class AbstractParentChild extends AbstractParent {
    @Override
    public String toString() {
        return "I'm abstract Parent's child";
    }
}
