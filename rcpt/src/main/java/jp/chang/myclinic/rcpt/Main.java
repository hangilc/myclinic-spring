package jp.chang.myclinic.rcpt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main  {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args )
    {
        if( args.length < 1 ){
            System.err.println("usage: rcpt command ...");
            System.exit(1);
        }
        String command = args[0];
        switch(command){
            case "check": cmdCheck(args); break;
            default: {
                System.err.println("Unknown command: " + command);
                System.exit(1);
            }
        }
    }

    // check: rcpt check [-f] year month
    private static void cmdCheck(String[] args){
        boolean fixit = false;
        int year = 0;
        int month = 0;
        if( args.length == 4 ){
            if( args[1].equals("-f") ){
                fixit = true;
            } else {
                System.err.println("invalid arg : " + args[1]);
                System.exit(1);
            }
            year = Integer.parseInt(args[2]);
            month = Integer.parseInt(args[3]);
        } else if( args.length == 3 ){
            year = Integer.parseInt(args[1]);
            month = Integer.parseInt(args[2]);
        } else {
            System.err.println("usage: rcpt check [-f] year month");
            System.exit(1);
        }
        System.out.printf("%d %d%n", year, month);

    }

}

