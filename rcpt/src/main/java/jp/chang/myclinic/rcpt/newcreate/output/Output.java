package jp.chang.myclinic.rcpt.newcreate.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

public class Output {

    private static Logger logger = LoggerFactory.getLogger(Output.class);
    private PrintStream outStream;

    public Output(PrintStream outStream) {
        this.outStream = outStream;
    }

    public void print(String s){
        outStream.println(s);
    }

    public void printInt(String key, int value){
        outStream.printf("%s %d\n", key, value);
    }

    public void printStr(String key, String value){
        outStream.printf("%s %s\n", key, value);
    }

}
