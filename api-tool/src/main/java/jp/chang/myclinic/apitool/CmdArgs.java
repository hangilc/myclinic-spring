package jp.chang.myclinic.apitool;

import picocli.CommandLine;

import java.io.PrintStream;

import static picocli.CommandLine.*;

@Command(name = "api-tool")
class CmdArgs {

    @Option(names = "--dry-run")
    boolean dryRun;

    @Option(names = "--help")
    boolean help;

    void usage(PrintStream out){
        CommandLine.usage(this, out);
    }

    static CmdArgs parse(String[] args){
        return CommandLine.populateCommand(new CmdArgs(), args);
    }
}
