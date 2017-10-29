package jp.chang.myclinic.reception;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReceptionArgs {

    private static final Logger logger = LoggerFactory.getLogger(ReceptionArgs.class);
    public static String defaultConfigFileName = "myclinic-reception.properties";
    public static String defaultPrinterSettingsDirName = "printer-settings";

    public Path workingDirPath;
    public Path configFilePath;
    public Path printerSettingsDir;
    public String serverUrl;

    public static ReceptionArgs parseArgs(String[] args){
        ReceptionArgs receptionArgs = new ReceptionArgs();
        Options options = new Options();
        options.addOption("d", "workdir", true, "working directory (default: current directory)");
        options.addOption("c", "config", true, "configuration file (default: " + defaultConfigFileName + " in working directory)");
        options.addOption("p", "printer-settings-dir", true, "directory containing printer settings " +
                "(default: " + defaultPrinterSettingsDirName + " in working directory)");
        options.addOption("h", "help", false, "print this help");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.error("Command line parsing failed.", e);
            System.exit(1);
        }
        if( cmd.hasOption("h") ){
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("reception", options);
            System.exit(0);
        }
        if( cmd.hasOption("d") ){
            try {
                receptionArgs.workingDirPath = Paths.get(cmd.getOptionValue("d"));
            } catch(InvalidPathException ex){
                logger.error("-d (--workdir) の値が不適切です。", ex);
                System.exit(1);
            }
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
