package jp.chang.myclinic.backendtester;

import picocli.CommandLine;
import picocli.CommandLine.*;

@Command(name = "api-tool",
        subcommands = {TestMysql.class, TestSqlite.class}
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

