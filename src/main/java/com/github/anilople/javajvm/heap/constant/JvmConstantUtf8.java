package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantUtf8Info;
import com.github.anilople.javajvm.heap.JvmClass;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JvmConstantUtf8 extends JvmConstant {

    private ConstantUtf8Info constantUtf8Info;

    /**
     * string content for this utf8 constant
     */
    private String content;

    private JvmConstantUtf8() {

    }

    public JvmConstantUtf8(JvmClass jvmClass, ConstantUtf8Info constantUtf8Info) {
        super(jvmClass);
        this.constantUtf8Info = constantUtf8Info;
    }

    /**
     * bytes to string (UTF-8)
     * whenever how many times use this method,
     * the string returning always identity!
     * @return
     */
    @Override
    public String toString() {
        if(null == this.content) {
            this.content = new String(constantUtf8Info.getBytes(), StandardCharsets.UTF_8);
        }
        return this.content;
    }
}
