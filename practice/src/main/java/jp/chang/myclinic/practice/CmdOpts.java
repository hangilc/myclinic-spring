package jp.chang.myclinic.practice;

import picocli.CommandLine;
import picocli.CommandLine.*;

@Command(name = "practice")
class CmdOpts {

    @Parameters(paramLabel = "server-url", arity = "0..1",
            description = "Server URL (default is env:MYCLINIC_SERVICE).")
    String serverUrl = System.getenv("MYCLINIC_SERVICE");

    @Option(names = {"--help"}, usageHelp = true, description = "Prints usage help.")
    boolean help;

    @Option(names = {"--sqlite-temp"}, description = "Uses temporay SQLite database." +
            "Database file is specified by server url parameter.")
    String sqliteTemp;

    @Option(names = "--component-test", description = "Runs component test. Requires no frontend.")
    boolean componentTest;

    @Option(names = {"--component-test-one", "--c1"}, description = "Runs single component test.",
            split = ":")
    String[] componentTestOne;

}
