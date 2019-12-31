package com.github.anilople.javajvm.instructions.extended;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class MULTIANEWARRAYTest {

    public static void main(String[] args) {
        int[][][] ints = new int[3][4][5];
        ints[1] = new int[2][3];
        ints[2][3][4] = 666;

        MULTIANEWARRAYTest[][][][] multianewarrayTests = new MULTIANEWARRAYTest[4][5][6][7];
        multianewarrayTests[0][0] = null;
        multianewarrayTests[3][4][5] = new MULTIANEWARRAYTest[100];

        multianewarrayTests[2][3][4] = multianewarrayTests[3][4][5];
    }

    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.run();
    }
}