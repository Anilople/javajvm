package com.github.anilople.javajvm.runtimedataarea;


import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalVariablesTest {

    @Test
    public void LocalVariablesTest() {
        LocalVariables localVariables = new LocalVariables(100);

        int index = 0;

        localVariables.setBooleanValue(index, true);
        Assertions.assertEquals(true, localVariables.getBooleanValue(index));
        index += 1;

        localVariables.setByteValue(index, (byte) 255);
        Assertions.assertEquals((byte) (-1), localVariables.getByteValue(index));
        index += 1;

        localVariables.setCharValue(index, '3');
        Assertions.assertEquals('3', localVariables.getCharValue(index));
        index += 1;

        localVariables.setIntValue(index, -234);
        Assertions.assertEquals(-234, localVariables.getIntValue(index));
        index += 1;

        localVariables.setLongValue(index, -100000000000L);
        Assertions.assertEquals(-100000000000L, localVariables.getLongValue(index));
        index += 1;

        localVariables.setFloatValue(index, -0.123234F);
        Assertions.assertEquals(-0.123234F, localVariables.getFloatValue(index));
        index += 1;

        localVariables.setDoubleValue(index, -0.12323434234234324D);
        Assertions.assertEquals(-0.12323434234234324D, localVariables.getDoubleValue(index));
        index += 1;

        Reference reference = new NullReference();
        localVariables.setReference(index, reference);
        Assertions.assertEquals(reference, localVariables.getReference(index));
    }
}
