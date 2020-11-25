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

文档地址 https://anilople.github.io/javajvm/

# 我的开发环境

Windows 10 1903

IntelliJ IDEA

Apache Maven 3.6.1

java version "1.8.0_231"

# 引用

[java specification](https://docs.oracle.com/javase/specs/)

[自己动手写Java虚拟机](https://book.douban.com/subject/26802084/)


