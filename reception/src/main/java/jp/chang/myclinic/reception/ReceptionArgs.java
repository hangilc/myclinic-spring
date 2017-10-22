package jp.chang.myclinic.reception;

import org.apache.commons.cli.*;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class ReceptionArgs {

    public Path configFile;
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
            System.out.println("Command line parsing failed.");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("-c: " + cmd.getOptionValue('c'));
        if( cmd.hasOption('c') ){
            try {
                receptionArgs.configFile = Paths.get(cmd.getOptionValue('c'));
            } catch(InvalidPathException ex){
                System.out.println("-c (--config) の値が不適切です。");
                System.exit(1);
            }
        }
        if( cmd.hasOption('p') ){
            try {
                receptionArgs.printerSettingsDir = Paths.get(cmd.getOptionValue('p'));
            } catch(InvalidPathException ex){
                System.out.println("-p (--printer-settings-dir) の値が不適切です。");
                System.exit(1);
            }
        }
        String[] rem = cmd.getArgs();
        if( rem.length == 0 ){
            System.out.println("サーバーの URL が設定されていません。");
            System.exit(1);
        } else if( rem.length == 1 ){
            receptionArgs.serverUrl = rem[0];
        } else {
            System.out.println("コマンドライン引数が多すぎます。");
            System.exit(1);
        }
        return receptionArgs;
    }

    @Override
    public String toString() {
        return "ReceptionArgs{" +
                "configFile=" + configFile +
                ", printerSettingsDir=" + printerSettingsDir +
                ", serverUrl='" + serverUrl + '\'' +
                '}';
    }
}
