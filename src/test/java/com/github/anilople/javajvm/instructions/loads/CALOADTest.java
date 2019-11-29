package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.config.CommandConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CALOADTest {

    @Test
    public void mainTest() {
        String className = this.getClass().getName();
        JavaJvmApplication.main(CommandConfig.getArgs(className));
    }

    public static void main(String[] args) {
        char[] chars = new char[4];
        chars[3] = 'a';
        char temp = chars[3];
    }

}