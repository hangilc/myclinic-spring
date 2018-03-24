package jp.chang.myclinic.mastermanip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        if( args.length == 0 ){
            System.err.println("Usage: master command [args...]");
            System.exit(1);
        }
        String command = args[0];
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
        switch(command){
            case "cat": {
                Cat cat = new Cat();
                cat.run(commandArgs);
                break;
            }
            default: {
                System.err.println("Unknown command: " + command);
                System.exit(1);
            }
        }
    }
}
