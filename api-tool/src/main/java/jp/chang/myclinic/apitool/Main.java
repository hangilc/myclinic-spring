package jp.chang.myclinic.apitool;

import jp.chang.myclinic.apitool.pgsqltablebases.PgsqlTableBases;
import jp.chang.myclinic.apitool.pgsqltables.PgsqlTables;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "api-tool",
        subcommands = {PgsqlTableBases.class, PgsqlTables.class}
)
public class Main implements Runnable {

    public static void main(String[] args) {
        CommandLine.run(new Main(), args);
    }

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }
}
