package com.github.anilople.javajvm.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

/**
 * common pattern in
 * java [-options] class [args...]
 * and
 * java [-options] -jar jarfile [args...]
 */
public class Command {

    private static final Logger logger = LoggerFactory.getLogger(Command.class);

    private Options options;

    private String className;

    private String jarfileName;

    private String[] args;

    public Command(Options options, String className, String jarfileName, String[] args) {
        this.options = options;
        this.className = className;
        this.jarfileName = jarfileName;
        this.args = args;
    }

    /**
     * parse from args
     *
     * @param args
     * @return
     */
    public static Command parse(String[] args) {
        int start = 0;

        // parse options
        Options options = new Options();
        start = Options.parse(options, args, start);

        // parse -jar jarfile or class
        String className = null;
        String jarfileName = null;
        if (start < args.length - 1 && "-jar".equals(args[start])) {
            jarfileName = args[start + 1];
            start += 2;
        } else if (start < args.length) {
            className = args[start];
            start += 1;
        } else {
            logger.debug("there are no class or jarfile");
        }

        return new Command(
                options,
                className,
                jarfileName,
                Arrays.copyOfRange(args, start, args.length)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return getOptions().equals(command.getOptions()) &&
                getClassName().equals(command.getClassName()) &&
                getJarfileName().equals(command.getJarfileName()) &&
                Arrays.equals(getArgs(), command.getArgs());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getOptions(), getClassName(), getJarfileName());
        result = 31 * result + Arrays.hashCode(getArgs());
        return result;
    }

    public Options getOptions() {
        return options;
    }

    public String getClassName() {
        return className;
    }

    public String getJarfileName() {
        return jarfileName;
    }

    public String[] getArgs() {
        return args.clone();
    }
}
