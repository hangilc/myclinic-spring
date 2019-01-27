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
import java.util.function.BiConsumer;
import java.util.function.Function;

public class AppProperties {
    private static Logger logger = LoggerFactory.getLogger(AppProperties.class);

    public String receiptPrinterSetting;
    Integer defaultKoukikoureiHokenshaBangou;
    String defaultKoukikoureiValidFrom;
    String defaultKoukikoureiValidUpto;

    private Prop[] props = new Prop[]{
            new Prop("printer.setting.receipt",
                    (app, val) -> app.receiptPrinterSetting = val,
                    app -> app.receiptPrinterSetting),
            new Prop("koukikourei.default.hokensha_bangou",
                    (app, val) -> app.defaultKoukikoureiHokenshaBangou = loadInteger(val),
                    app -> saveInteger(app.defaultKoukikoureiHokenshaBangou)),
            new Prop("koukikourei.default.valid_from",
                    (app, val) -> app.defaultKoukikoureiValidFrom = val,
                    app -> app.defaultKoukikoureiValidFrom),
            new Prop("koukikourei.default.valid_upto",
                    (app, val) -> app.defaultKoukikoureiValidUpto = val,
                    app -> app.defaultKoukikoureiValidUpto),
    };

    void load(Properties properties) {
        String oldPrinterSetting = properties.getProperty("receipt-printer-setting");
        if( oldPrinterSetting != null ){
            this.receiptPrinterSetting = oldPrinterSetting;
        }
        for (Prop prop : props) {
            String value = properties.getProperty(prop.key);
            if (value != null) {
                prop.loader.accept(this, value);
            }
        }
    }

    Properties toProperties() {
        Properties properties = new Properties();
        for (Prop prop : props) {
            String value = prop.saver.apply(this);
            if (value != null) {
                properties.setProperty(prop.key, value);
            }
        }
        return properties;
    }

    private class Prop {
        private String key;
        private BiConsumer<AppProperties, String> loader;
        private Function<AppProperties, String> saver;

        Prop(String key, BiConsumer<AppProperties, String> loader,
             Function<AppProperties, String> saver) {
            this.key = key;
            this.loader = loader;
            this.saver = saver;
        }
    }

    private static Integer loadInteger(String value){
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException ex){
            logger.warn("Invalid integer {}", value, ex);
            return null;
        }
    }

    private static String saveInteger(Integer value){
        if( value == null ){
            return null;
        } else {
            return value.toString();
        }
    }
}
