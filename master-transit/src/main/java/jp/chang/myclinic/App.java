package jp.chang.myclinic;

import java.io.IOException;
import java.io.PrintWriter;

import jline.console.ConsoleReader;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	ConsoleReader reader = new ConsoleReader();
    	PrintWriter out = new PrintWriter(reader.getOutput());
    	out.println("hello, world");
    	out.flush();
    }
}
