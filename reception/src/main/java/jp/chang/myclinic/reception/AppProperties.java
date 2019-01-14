package jp.chang.myclinic.reception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Properties;

public class AppProperties {

    private static final String receiptPrinterSettingKey = "receipt-printer-setting";

    public String receiptPrinterSetting;

    void load(Properties props){
        this.receiptPrinterSetting = props.getProperty(receiptPrinterSettingKey);
    }

    Properties toProperties(){
        Properties props = new Properties();
        if( receiptPrinterSetting != null ){
            props.setProperty(receiptPrinterSettingKey, receiptPrinterSetting);
        }
        return props;
    }

}
