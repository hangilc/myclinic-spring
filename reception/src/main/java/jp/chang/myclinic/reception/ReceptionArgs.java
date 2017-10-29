package jp.chang.myclinic.reception;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

class ReceptionArgs {

    private static final Logger logger = LoggerFactory.getLogger(ReceptionArgs.class);

    public Path configFilePath;
    public Path printerSettingsDir;
    public String serverUrl;

    static ReceptionArgs parseArgs(String[] args){
        ReceptionArgs receptionArgs = new ReceptionArgs();
        Options options = new Options();
        options.addOption("c", "config", true, "configuration file");
        options.addOption("p", "printer-settings-dir", true, "directory containing printer settings");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error("Command line parsing failed.", e);
            System.exit(1);
        }
        if( cmd.hasOption('c') ){
            try {
                receptionArgs.configFilePath = Paths.get(cmd.getOptionValue('c'));
            } catch(InvalidPathException ex){
                logger.error("-c (--config) の値が不適切です。", ex);
                System.exit(1);
            }
        }
        if( cmd.hasOption('p') ){
            try {
                receptionArgs.printerSettingsDir = Paths.get(cmd.getOptionValue('p'));
            } catch(InvalidPathException ex){
                logger.error("-p (--printer-settings-dir) の値が不適切です。", ex);
                System.exit(1);
            }
        }
        String[] rem = cmd.getArgs();
        if( rem.length == 0 ){
            logger.error("サーバーの URL が設定されていません。");
            System.exit(1);
        } else if( rem.length == 1 ){
            String arg = rem[0];
            if( !arg.endsWith("/") ){
                arg += "/";
            }
            receptionArgs.serverUrl = arg;
        } else {
            logger.error("コマンドライン引数が多すぎます。");
            System.exit(1);
        }
        return receptionArgs;
    }

    @Override
    public String toString() {
        return "ReceptionArgs{" +
                "configFilePath=" + configFilePath +
                ", printerSettingsDir=" + printerSettingsDir +
                ", serverUrl='" + serverUrl + '\'' +
                '}';
    }
}
