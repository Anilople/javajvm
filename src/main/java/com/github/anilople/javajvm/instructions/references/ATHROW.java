package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ReferenceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 * Throw exception or error
 *
 * Operand ..., objectref →
 * Stack objectref
 *
 * Description
 * The objectref must be of type reference and must refer to an
 * object that is an instance of class Throwable or of a subclass of
 * Throwable . It is popped from the operand stack. The objectref is
 * then thrown by searching the current method (§2.6) for the first
 * exception handler that matches the class of objectref, as given by
 * the algorithm in §2.10.
 *
 *
 */
public class ATHROW implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(ATHROW.class);

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        final Reference objectref = frame.getOperandStacks().popReference();
        if(Reference.isNull(objectref)) {
            throw new NullPointerException();
        }
        final ObjectReference throwableObjectReference = (ObjectReference) objectref;
        if(!throwableObjectReference.getJvmClass().isSubClassOf(Throwable.class)) {
            throw new RuntimeException(throwableObjectReference + " is not the subclass of " + Throwable.class);
        }
        final JvmClass exceptionClass = throwableObjectReference.getJvmClass();
        final JvmThread jvmThread = frame.getJvmThread();
        while(jvmThread.existFrame() && !jvmThread.currentFrame().getJvmMethod().existsExceptionHandler(exceptionClass)) {
            jvmThread.popFrame();
        }

        if(jvmThread.existFrame()) {
            final Frame currentFrame = jvmThread.currentFrame();
            final JvmMethod jvmMethod = currentFrame.getJvmMethod();
            // find it
            logger.debug("find exception handler in method [{}] to handle exception [{}]", jvmMethod, exceptionClass);
            JvmMethod.ExceptionHandler exceptionHandler = jvmMethod.getExceptionHandler(exceptionClass);
            // clear the operand stack
            currentFrame.getOperandStacks().clear();
            // push the exception object reference
            currentFrame.getOperandStacks().pushReference(throwableObjectReference);
            // set pc to handler pc
            final int handlerPc = exceptionHandler.getHandlerPc();
            jvmThread.currentFrame().setNextPc(handlerPc);
        } else {
            // jvm stack frame is empty
            // it mean that jvm find no exception handler for this exception
            handleUncaughtException(jvmThread, throwableObjectReference);
        }
    }

    /**
     * sometimes jvm cannot find a method to handle the exception,
     * so jvm need to handle this situation,
     * the method is use to do it.
     * @param jvmThread
     * @param throwableObjectReference
     */
    private void handleUncaughtException(JvmThread jvmThread, ObjectReference throwableObjectReference) {
        jvmThread.clearStack();

        // print exception
        final String detailMessage;
        try {
            detailMessage = (String) ReferenceUtils.reference2Object(
                    throwableObjectReference.getReference("detailMessage")
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if(detailMessage.length() > 0) {
            System.err.println("Exception in thread \"main\" " + throwableObjectReference.getJvmClass().getName() + ": " + detailMessage);
        } else {
            System.err.println("Exception in thread \"main\" " + throwableObjectReference.getJvmClass().getName());
        }

        // print stack trace
        final StackTraceElement[] stackTraceElements;
        try {
            stackTraceElements = (StackTraceElement[]) ReferenceUtils.reference2Object(
                    throwableObjectReference.getReference("stackTrace")
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for(StackTraceElement stackTraceElement : stackTraceElements) {
            System.err.println("\tat " + stackTraceElement);
        }

        try {
            Object realExceptionObject = ReferenceUtils.reference2Object(throwableObjectReference);
            throw (RuntimeException) realExceptionObject;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {
        return 1;
    }

}
