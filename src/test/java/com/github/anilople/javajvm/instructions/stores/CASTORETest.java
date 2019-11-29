package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.config.CommandConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CASTORETest {

    @Test
    public void mainTest() {
        String className = this.getClass().getName();
        JavaJvmApplication.main(CommandConfig.getArgs(className));
    }

    public static void main(String[] args) {
        char[] chars = new char[20];
        int len = chars.length;
        chars[0] = '6';
        chars[chars.length - 1] = 'k';
    }
}