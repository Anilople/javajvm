package com.github.anilople.javajvm.constants;

/**
 * 4.3 Descriptors
 *
 * A descriptor is a string representing the type of a field or method. Descriptors are
 * represented in the class file format using modified UTF-8 strings (§4.4.7) and thus
 * may be drawn, where not further constrained, from the entire Unicode codespace.
 */
public class Descriptors {

    /**
     * base type
     */
    public static class BaseType {

        /**
         * byte
         * signed byte
         */
        public static final String BYTE = "B";

        /**
         * char
         * Unicode character code point in the Basic
         * Multilingual Plane, encoded with UTF-16
         */
        public static final String CHAR = "C";

        /**
         * double
         * double-precision floating-point value
         */
        public static final String DOUBLE = "D";

        /**
         * float
         * single-precision floating-point value
         */
        public static final String FLOAT = "F";

        /**
         * int
         * integer
         */
        public static final String INT = "I";

        /**
         * long
         * long integer
         */
        public static final String LONG = "J";

        /**
         * short
         * signed short
         */
        public static final String SHORT = "S";

        /**
         * boolean
         * true or false
         */
        public static final String BOOLEAN = "Z";

    }

}
