package com.github.anilople.javajvm.command;

import com.github.anilople.javajvm.constants.JVMConstants;

import java.util.List;

/**
 * [-options]
 */
public class Options {

    private boolean versionFlag;

    private boolean helpFlag;

    /**
     * parse [-options]
     * @return
     */
    public static Options parse(List<String> optionArgs) {
        Options options = new Options();
        for(String arg : optionArgs) {
            if(!options.parseOption(arg)) {
                // parse options finished
                break;
            }
        }
        return options;
    }


    /**
     * parse one option
     * @param arg
     * @return if parse success, return true.
     */
    private boolean parseOption(String arg) {
        if(!arg.startsWith("-")) {
            return false;
        }

        if(arg.startsWith("-version")) {
            this.versionFlag = true;
        }

        if(arg.startsWith("-?") || arg.startsWith("-help")) {
            this.helpFlag = true;
        }

        return true;
    }

    public boolean isVersionFlag() {
        return versionFlag;
    }

    public boolean isHelpFlag() {
        return helpFlag;
    }
}
