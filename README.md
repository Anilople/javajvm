# Java JVM

[![Build Status](https://travis-ci.com/Anilople/javajvm.svg?branch=master)](https://travis-ci.com/Anilople/javajvm)

使用`Java 8`来实现一个纯解释执行的Java虚拟机。

代码偏向于可读性，忽略性能。

项目的目的在于更加深入的学习和理解Java虚拟机。

# 怎么跑起来？

## 第一次使用

从[release](https://github.com/Anilople/javajvm/releases)中下载最新的版本`javajvm-xxx.jar`。

在`javajvm-xxx.jar`所在的目录下，命令行中执行

```shell
java -jar javajvm-xxx.jar
```

没错，这个`JVM`本质上是一个jar包，里面放着编译好的Java代码。

运行上面的命令后，你会看到一些提示信息。

## 如何用这个`JVM`跑自己的代码？

假设你写了个`HelloWorld.java`，然后把它通过`javac`编译成了`HelloWorld.class`，将`HelloWorld.class`与`javajvm-xxx.jar`放至相同的目录下，然后在那个目录下，执行

```shell
java -jar javajvm-xxx.jar HelloWorld
```

即可使用。

# 我的开发环境

Windows 10 1903

IntelliJ IDEA

Apache Maven 3.6.1

java version "1.8.0_231"

# 引用

[java specification](https://docs.oracle.com/javase/specs/)

[自己动手写Java虚拟机](https://book.douban.com/subject/26802084/)


