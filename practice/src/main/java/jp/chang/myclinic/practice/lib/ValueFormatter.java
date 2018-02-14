package jp.chang.myclinic.practice.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

public class ValueFormatter {

    private static Logger logger = LoggerFactory.getLogger(ValueFormatter.class);

    private ValueFormatter() { }

    private static DecimalFormat doubleFormatter = new DecimalFormat("###.##");

    public static String formatDouble(double value){
        return doubleFormatter.format(value);
    }
}
