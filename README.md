# Java JVM

[![Build Status](https://travis-ci.com/Anilople/javajvm.svg?branch=master)](https://travis-ci.com/Anilople/javajvm)

使用`Java 8`来实现一个纯解释执行的Java虚拟机。

代码偏向于可读性，忽略性能。

项目的目的在于更加深入的学习和理解Java虚拟机。

# 怎么跑起来？

## hello, world

从[release](https://github.com/Anilople/javajvm/releases)中下载最新的版本的压缩包，解压后，你会看到2个文件

* `HelloWorld.java`：未编译的Java代码，里面的内容即将运行在实现的JVM上
* `javajvm-xxx.jar`：JVM的实现，本质上是一个jar包，里面放着编译好的Java代码。

`xxx`代表版本号，不要在Shell中真的输入`xxx`，请将`javajvm-xxx.jar`自行换成对应的文件。

在`javajvm-xxx.jar`所在的目录下，命令行中执行

```shell
java -jar javajvm-xxx.jar
```

运行上面的命令后，你会看到一些提示信息。

现在，将`HelloWorld.java`编译成`HelloWorld.class`，然后用JVM运行。

```shell
javac HelloWorld.java # 编译，会生成文件 HelloWorld.java
java -jar javajvm-xxx.jar HelloWorld # 运行刚刚编译出来的文件
```

## 如何用这个`JVM`跑自己的代码？

修改上述中的文件`HelloWorld.java`，加入自己写的代码，再重复一次上述的流程即可。

# 这个JVM是如何实现的？

以下为实现对应的具体代码，目前暂无blog介绍

## 准备阶段

[解析命令行](src/main/java/com/github/anilople/javajvm/command/Command.java)

[获取jre路径](src/main/java/com/github/anilople/javajvm/command/Options.java)

[创建虚拟机](src/main/java/com/github/anilople/javajvm/JavaJvmApplication.java)

## 运行阶段

### 解析原始的二进制数据

[获取class的原始信息（二进制数据）](src/main/java/com/github/anilople/javajvm/classpath/Classpath.java)

[解析class文件的数据结构](src/main/java/com/github/anilople/javajvm/classfile/ClassFile.java)

### 解析后产生的数据结构进一步抽象

[常量 - constant](src/main/java/com/github/anilople/javajvm/heap/constant)

[常量池 - constant pool](src/main/java/com/github/anilople/javajvm/heap/JvmConstantPool.java)

[类加载器 - class loader](src/main/java/com/github/anilople/javajvm/heap/JvmClassLoader.java)

[类 - class](src/main/java/com/github/anilople/javajvm/heap/JvmClass.java)

[成员 - field](src/main/java/com/github/anilople/javajvm/heap/JvmField.java)

[方法 - method](src/main/java/com/github/anilople/javajvm/heap/JvmMethod.java)

### 运行时

[线程 - thread](src/main/java/com/github/anilople/javajvm/runtimedataarea/JvmThread.java)

[栈帧 - frame](src/main/java/com/github/anilople/javajvm/runtimedataarea/Frame.java)

[本地变量 - local variables](src/main/java/com/github/anilople/javajvm/runtimedataarea/LocalVariables.java)

[操作数栈 - operand stack](src/main/java/com/github/anilople/javajvm/runtimedataarea/OperandStacks.java)

### 引用的表示

[引用 - reference](src/main/java/com/github/anilople/javajvm/runtimedataarea/Reference.java)

[null的表示 - the represent of null reference](src/main/java/com/github/anilople/javajvm/runtimedataarea/reference/NullReference.java)

[对象 - object](src/main/java/com/github/anilople/javajvm/runtimedataarea/reference/ObjectReference.java)

[数组 -  array](src/main/java/com/github/anilople/javajvm/runtimedataarea/reference/ArrayReference.java)

[java.lang.Class](src/main/java/com/github/anilople/javajvm/runtimedataarea/reference/ClassObjectReference.java)

### 字节码

[所有字节码](src/main/java/com/github/anilople/javajvm/instructions)

[比较](src/main/java/com/github/anilople/javajvm/instructions/comparisons)

[常量操作](src/main/java/com/github/anilople/javajvm/instructions/constants)

[分支控制](src/main/java/com/github/anilople/javajvm/instructions/control)

[数据类型转换](src/main/java/com/github/anilople/javajvm/instructions/conversions)

[扩展字节码](src/main/java/com/github/anilople/javajvm/instructions/extended)

[加载操作数](src/main/java/com/github/anilople/javajvm/instructions/loads)

[数学运算](src/main/java/com/github/anilople/javajvm/instructions/math)

[引用](src/main/java/com/github/anilople/javajvm/instructions/references)：

* [异常抛出](src/main/java/com/github/anilople/javajvm/instructions/references/ATHROW.java)
* [方法调用](src/main/java/com/github/anilople/javajvm/instructions/references/INVOKEVIRTUAL.java)
* [new对象](src/main/java/com/github/anilople/javajvm/instructions/references/NEW.java)
* [new数组](src/main/java/com/github/anilople/javajvm/instructions/references/NEWARRAY.java)

[栈操作](src/main/java/com/github/anilople/javajvm/instructions/stack)

[存储操作数到本地变量表](src/main/java/com/github/anilople/javajvm/instructions/stores)

# 我的开发环境

Windows 10 1903

IntelliJ IDEA

Apache Maven 3.6.1

java version "1.8.0_231"

# 引用

[java specification](https://docs.oracle.com/javase/specs/)

[自己动手写Java虚拟机](https://book.douban.com/subject/26802084/)


