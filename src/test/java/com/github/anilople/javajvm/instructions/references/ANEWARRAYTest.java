package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.config.CommandConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ANEWARRAYTest {

    @Test
    public void mainTest() {
        String className = this.getClass().getName();
        JavaJvmApplication.main(CommandConfig.getArgs(className));
    }

    public static void main(String[] args) {
        ANEWARRAYTest[] anewarrayTests = new ANEWARRAYTest[6];
        anewarrayTests[1] = new ANEWARRAYTest();
        ANEWARRAYTest anewarrayTest = anewarrayTests[1];
    }

}