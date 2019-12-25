package com.github.anilople.javajvm.helper;

import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;

import java.util.*;
import java.util.function.Consumer;

/**
 * For the testing JVM
 * We know that a Java Virtual Machine interpreter.
 * in jvms8,
 * 2.11 Instruction Set Summary
 * Ignoring exceptions, the inner loop of a Java Virtual Machine interpreter is
 * effectively
 *      do {
 *          atomically calculate pc and fetch opcode at pc;
 *          if (operands) fetch operands;
 *          execute the action for the opcode;
 *      } while (there is more to do);
 *
 * For the test of JVM,
 * We can simply insert our code to it, like follow:
 *      do {
 *          atomically calculate pc and fetch opcode at pc;
 *          [before fetch listeners]
 *          if (operands) fetch operands;
 *          [after fetch listeners]
 *          [before execute instruction listeners]
 *          execute the action for the opcode;
 *          [after execute instruction listeners]
 *      } while (there is more to do);
 *
 * It seems like a debugger, and catch every moment of JVM,
 * When JVM do something, the listener will be trigger
 */
public class JvmThreadRunner {

    private final JvmThread jvmThread;

    private final List<Consumer<JvmThread>> startListeners = new ArrayList<>();

    private final List<Consumer<JvmThread>> endListeners = new ArrayList<>();

    /**
     * key is Class inherits from Instruction
     * @see com.github.anilople.javajvm.instructions.Instruction
     * value is a list of listener (so the listeners exist order)
     */
    private final Map<
            Class<? extends Instruction>, List<Consumer<JvmThread>>
            > beforeInstructionExecutionListeners = new HashMap<>();

    /**
     * key is Class inherits from Instruction
     * @see com.github.anilople.javajvm.instructions.Instruction
     * value is a list of listener (so the listeners exist order)
     */
    private final Map<
            Class<? extends Instruction>, List<Consumer<JvmThread>>
            > afterInstructionExecutionListeners = new HashMap<>();

    /**
     * Don't forget to push frame to the Java Virtual Machine stack of thread
     * before construction
     * @param jvmThread
     */
    public JvmThreadRunner(JvmThread jvmThread) {
        Objects.requireNonNull(jvmThread, "JVM Thread Runner cannot construct by null thread");
        this.jvmThread = jvmThread;
    }

    /**
     * add a listener before thread running
     * @param consumer
     */
    public void addStartListener(Consumer<JvmThread> consumer) {
        startListeners.add(consumer);
    }

    /**
     * add a listener only before specific Instruction's execution
     * @param instructionClass
     * @param consumer
     */
    public void  addBeforeInstructionExecutionListener(
            Class<? extends Instruction> instructionClass, Consumer<JvmThread> consumer
    ) {
        if(!beforeInstructionExecutionListeners.containsKey(instructionClass)) {
            beforeInstructionExecutionListeners.put(instructionClass, new ArrayList<>());
        }
        beforeInstructionExecutionListeners.get(instructionClass).add(consumer);
    }

    /**
     * add a listener only after specific Instruction's execution
     * @param instructionClass
     * @param consumer
     */
    public void addAfterInstructionExecutionListener(
            Class<? extends Instruction> instructionClass, Consumer<JvmThread> consumer
    ) {
        if(!afterInstructionExecutionListeners.containsKey(instructionClass)) {
            afterInstructionExecutionListeners.put(instructionClass, new ArrayList<>());
        }
        afterInstructionExecutionListeners.get(instructionClass).add(consumer);
    }

    /**
     * add a listener after thread running
     * @param consumer
     */
    public void addEndListener(Consumer<JvmThread> consumer) {
        endListeners.add(consumer);
    }

    /**
     * run the JvmThread and trigger the listener added
     */
    public void run() {

        // before thread running, trigger the listeners
        for(Consumer<JvmThread> consumer : startListeners) {
            consumer.accept(jvmThread);
        }

        while(jvmThread.existFrame()) {
            // read instruction
            Instruction instruction = jvmThread.currentFrame().readNextInstruction();

            // before this instruction's execution, trigger the listeners
            if(beforeInstructionExecutionListeners.containsKey(instruction.getClass())) {
                for(Consumer<JvmThread> consumer : beforeInstructionExecutionListeners.get(instruction.getClass())) {
                    consumer.accept(jvmThread);
                }
            }

            // execute instruction
            instruction.execute(jvmThread.currentFrame());

            // after this instruction's execution, trigger the listeners
            if(afterInstructionExecutionListeners.containsKey(instruction.getClass())) {
                for(Consumer<JvmThread> consumer : afterInstructionExecutionListeners.get(instruction.getClass())) {
                    consumer.accept(jvmThread);
                }
            }
        }

        // after thread running, trigger the listeners
        for(Consumer<JvmThread> consumer : endListeners) {
            consumer.accept(jvmThread);
        }
    }

}
