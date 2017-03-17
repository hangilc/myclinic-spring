package jp.chang.myclinic;

import java.io.IOException;
import java.io.PrintWriter;

import jline.console.ConsoleReader;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	ConsoleReader reader = new ConsoleReader();
    	reader.getCursorBuffer().write("pqr");
    	String line = reader.readLine(">");
    	reader.println(line);
    	reader.flush();
    }
}
