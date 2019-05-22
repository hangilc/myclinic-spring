package jp.chang.myclinic.practice;

import picocli.CommandLine.*;

@Command(name = "practice")
class CmdOpts {

    @Parameters(paramLabel = "server-url", arity = "0..1",
            description = "Server URL (default is env:MYCLINIC_SERVICE). sqlite-temp:... means temporary " +
                    "sqlite database.")
    String serverUrl = System.getenv("MYCLINIC_SERVICE");

    @Option(names = {"--help"}, usageHelp = true, description = "Prints usage help.")
    boolean help;

    @Option(names = "--component-test", description = "Runs component test. Requires no frontend.")
    boolean componentTest;

    @Option(names = {"--component-test-one", "--c1"}, description = "Runs single component test.",
            split = ":")
    String[] componentTestOne;

    @Option(names = {"--gui-test"}, description = "Runs GUI tests.")
    boolean guiTest;

    @Option(names = {"--gui-test-selected"}, description = "Runs selected GUI tests.")
    String guiTestSelected;

    @Option(names = {"--main-test"}, description = "Runs MainPane tests.")
    boolean mainTest;

    @Option(names = {"--main-test-single"}, description = "Runs single MainPane test.")
    String mainTestSingle;

}
