package jp.chang.myclinic.pharma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PharmaConfig {

    public static PharmaConfig INSTANCE = new PharmaConfig();

    static {
        try {
            if( INSTANCE.configFileExists() ){
                INSTANCE.readFromConfigFile();
            }
        } catch(IOException ex){
            ex.printStackTrace();
            throw new UncheckedIOException(ex);
        }
    }

    private Path configPath = Paths.get(System.getProperty("user.home"), "myclinic-pharma.properties");

    private String prescPrinterSetting = "";
    private String drugbagPrinterSetting = "";
    private String techouPrinterSetting = "";

    private PharmaConfig(){

    }

    public boolean configFileExists(){
        return Files.exists(configPath);
    }

    public String getPrescPrinterSetting() {
        return prescPrinterSetting;
    }

    public void setPrescPrinterSetting(String prescPrinterSetting) {
        if( prescPrinterSetting == null ){
            prescPrinterSetting = "";
        }
        this.prescPrinterSetting = prescPrinterSetting;
    }

    public String getDrugbagPrinterSetting() {
        return drugbagPrinterSetting;
    }

    public void setDrugbagPrinterSetting(String drugbagPrinterSetting) {
        if( drugbagPrinterSetting == null ){
            drugbagPrinterSetting = "";
        }
        this.drugbagPrinterSetting = drugbagPrinterSetting;
    }

    public String getTechouPrinterSetting() {
        return techouPrinterSetting;
    }

    public void setTechouPrinterSetting(String techouPrinterSetting) {
        if( techouPrinterSetting == null ){
            techouPrinterSetting = "";
        }
        this.techouPrinterSetting = techouPrinterSetting;
    }

    public void readFromConfigFile() throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)){
            Properties props = new Properties();
            props.load(reader);
            setPrescPrinterSetting(props.getProperty("presc-printer-setting"));
            setDrugbagPrinterSetting((props.getProperty("drugbag-printer-setting")));
            setTechouPrinterSetting(props.getProperty("techou-printer-setting"));
        }
    }

    public void writeToConfigFile() throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)){
            Properties props = new Properties();
            props.setProperty("presc-printer-setting", getPrescPrinterSetting());
            props.setProperty("drugbag-printer-setting", getDrugbagPrinterSetting());
            props.setProperty("techou-printer-setting", getTechouPrinterSetting());
            props.store(writer, "");
        }
    }
}
