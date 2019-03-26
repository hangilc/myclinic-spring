package jp.chang.myclinic.practice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "practice")
class CmdOpts {

    @CommandLine.Parameters(paramLabel = "server-url", arity = "0..1",
            description = "Server URL (default is env:MYCLINIC_SERVICE).")
    private String serverUrl = System.getenv("MYCLINIC_SERVICE");

    @CommandLine.Option(names = {"--help"}, usageHelp = true, description = "Prints usage help.")
    private boolean help;

    @CommandLine.Option(names = {"--mock-sqlite"}, description = "Uses temporay SQLite database. " +
            "Database file is specified by server url parameter.")
    private boolean mockSqlite;

    public String getServerUrl() {
        return serverUrl;
    }

    public boolean getMockSqlite() {
        return mockSqlite;
    }

}
