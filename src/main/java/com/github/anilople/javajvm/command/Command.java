package com.github.anilople.javajvm.command;

import com.github.anilople.javajvm.utils.CommandUtils;

import java.util.List;

/**
 * common pattern in
 *  java [-options] class [args...]
 * and
 *  java [-options] -jar jarfile [args...]
 */
public class Command {

    private Options options;

    private String className;

    private String jarfileName;

    private List<String> args;

    private Command() {}

    private Command(List<String> optionArgs, List<String> args) {
        this.options = Options.parse(optionArgs);
        this.args = args;
    }

    /**
     * parse from args
     * @param args
     * @return
     */
    public static Command parse(String[] args) {
        List<List<String>> threeParts = CommandUtils.splitToThreePart(args);
        Command command = new Command(threeParts.get(0), threeParts.get(2));
        if(threeParts.get(1).size() <= 0) {
            // there no class no jarfile
        } else {
            if(CommandUtils.isClassCommand(args)) {
                command.className = threeParts.get(1).get(0);
            } else {
                command.jarfileName = threeParts.get(1).get(0);
            }
        }
        return command;
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

    public List<String> getArgs() {
        return args;
    }
}
