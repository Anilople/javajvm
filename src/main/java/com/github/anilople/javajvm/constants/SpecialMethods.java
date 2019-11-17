package com.github.anilople.javajvm.constants;

/**
 * 2.9 Special Methods
 *
 * At the level of the Java Virtual Machine, every constructor written in the Java
 * programming language (JLS §8.8) appears as an instance initialization method
 * that has the special name <init> . This name is supplied by a compiler. Because
 * the name <init> is not a valid identifier, it cannot be used directly in a program
 * written in the Java programming language. Instance initialization methods may
 * be invoked only within the Java Virtual Machine by the invokespecial instruction
 * (§invokespecial), and they may be invoked only on uninitialized class instances.
 * An instance initialization method takes on the access permissions (JLS §6.6) of the
 * constructor from which it was derived.
 */
public class SpecialMethods {

    public static final String INIT = "<init>";

    /**
     * A class or interface has at most one class or interface initialization method and is
     * initialized (§5.5) by invoking that method. The initialization method of a class or
     * interface has the special name <clinit> , takes no arguments, and is void (§4.3.3).
     *
     * Other methods named <clinit> in a class file are of no consequence. They are not class
     * or interface initialization methods. They cannot be invoked by any Java Virtual Machine
     * instruction and are never invoked by the Java Virtual Machine itself.
     *
     * The name <clinit> is supplied by a compiler. Because the name <clinit> is
     * not a valid identifier, it cannot be used directly in a program written in the Java
     * programming language.
     *
     * Class and interface initialization methods are invoked
     * implicitly by the Java Virtual Machine;
     * they are never invoked directly from any
     * Java Virtual Machine instruction, but are invoked only indirectly as part of the class
     * initialization process.
     */
    public static final String CLINIT = "<clinit>";

}
