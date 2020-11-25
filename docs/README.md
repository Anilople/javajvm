# 这个JVM是如何实现的？

以下为实现对应的具体代码，目前暂无blog介绍

## 准备阶段

[解析命令行](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/command/Command.java)

[获取jre路径](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/command/Options.java)

[创建虚拟机](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/JavaJvmApplication.java)

## 运行阶段

### 解析原始的二进制数据

[获取class的原始信息（二进制数据）](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/classpath/Classpath.java)

[解析class文件的数据结构](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/classfile/ClassFile.java)

### 解析后产生的数据结构进一步抽象

[常量 - constant](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/heap/constant)

[常量池 - constant pool](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/heap/JvmConstantPool.java)

[类加载器 - class loader](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/heap/JvmClassLoader.java)

[类 - class](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/heap/JvmClass.java)

[成员 - field](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/heap/JvmField.java)

[方法 - method](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/heap/JvmMethod.java)

### 运行时

[线程 - thread](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/runtimedataarea/JvmThread.java)

[栈帧 - frame](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/runtimedataarea/Frame.java)

[本地变量 - local variables](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/runtimedataarea/LocalVariables.java)

[操作数栈 - operand stack](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/runtimedataarea/OperandStacks.java)

### 引用的表示

[引用 - reference](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/runtimedataarea/Reference.java)

[null的表示 - the represent of null reference](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/runtimedataarea/reference/NullReference.java)

[对象 - object](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/runtimedataarea/reference/ObjectReference.java)

[数组 -  array](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/runtimedataarea/reference/ArrayReference.java)

[java.lang.Class](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/runtimedataarea/reference/ClassObjectReference.java)

### 字节码

[所有字节码](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions)

[比较](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/comparisons)

[常量操作](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/constants)

[分支控制](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/control)

[数据类型转换](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/conversions)

[扩展字节码](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/extended)

[加载操作数](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/loads)

[数学运算](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/math)

[引用](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/references)：

* [异常抛出](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/references/ATHROW.java)
* [方法调用](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/references/INVOKEVIRTUAL.java)
* [new对象](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/references/NEW.java)
* [new数组](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/references/NEWARRAY.java)

[栈操作](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/stack)

[存储操作数到本地变量表](https://github.com/Anilople/javajvm/blob/master/src/main/java/com/github/anilople/javajvm/instructions/stores)

# 引用

[java specification](https://docs.oracle.com/javase/specs/)

[自己动手写Java虚拟机](https://book.douban.com/subject/26802084/)


