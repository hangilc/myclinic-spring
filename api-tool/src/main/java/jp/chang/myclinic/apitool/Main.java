package jp.chang.myclinic.apitool;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "api-tool",
        subcommands = {Tables.class, SqliteSchema.class, UpdateFrontend.class, SearchMaster.class,
                ListPrescExample.class}
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
