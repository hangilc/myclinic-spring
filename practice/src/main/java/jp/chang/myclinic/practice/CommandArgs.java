package jp.chang.myclinic.practice;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class CommandArgs {

    private static final Logger logger = LoggerFactory.getLogger(CommandArgs.class);
    private Path workingDirectory;
    private String serverUrl;

    CommandArgs(String[] args){
        Options options = new Options();
        options.addOption("d", "workdir", true, "Working directory");
        options.addOption("h", "help", false, "print this help");
        CommandLineParser commandParser = new DefaultParser();
        try {
            CommandLine commandLine = commandParser.parse(options, args);
            if( commandLine.hasOption("h") ){
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("practice", options);
                System.exit(0);
            }
            if( commandLine.hasOption("d") ){
                workingDirectory = Paths.get(commandLine.getOptionValue("d"));
            } else {
                workingDirectory = Paths.get(System.getProperty("user.dir"));
            }
            if( !(Files.exists(workingDirectory) && Files.isDirectory(workingDirectory)) ){
                logger.error("Invalid working directory: " + workingDirectory);
                System.exit(1);
            }
            String[] rem = commandLine.getArgs();
            if( rem.length == 0 ){
                logger.error("サーバーの URL が設定されていません。");
                System.exit(1);
            } else if( rem.length == 1 ){
                String arg = rem[0];
                if( !arg.endsWith("/") ){
                    arg += "/";
                }
                serverUrl = arg;
            } else {
                logger.error("コマンドライン引数が多すぎます。");
                System.exit(1);
            }
        } catch (ParseException e) {
            logger.error("Failed parse command line.", e);
            System.exit(1);
        }
    }

    public Path getWorkingDirectory(){
        return workingDirectory;
    }

    public String getServerUrl(){
        return serverUrl;
    }

    @Override
    public String toString() {
        return "CommandArgs{" +
                "workingDirectory=" + workingDirectory +
                '}';
    }
}
