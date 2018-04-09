package jp.chang.myclinic.pharma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    private static Logger logger = LoggerFactory.getLogger(Config.class);

    private String prescContentPrinterSetting;
    private String drugBagPrinterSetting;
    private String techouPrinterSetting;
    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper(new YAMLFactory());
    }


    public Config() {

    }

    @JsonProperty("presc-content-printer-setting")
    public String getPrescContentPrinterSetting() {
        return prescContentPrinterSetting;
    }

    public void setPrescContentPrinterSetting(String prescContentPrinterSetting) {
        this.prescContentPrinterSetting = prescContentPrinterSetting;
    }

    @JsonProperty("drug-bag-printer-setting")
    public String getDrugBagPrinterSetting() {
        return drugBagPrinterSetting;
    }

    public void setDrugBagPrinterSetting(String drugBagPrinterSetting) {
        this.drugBagPrinterSetting = drugBagPrinterSetting;
    }

    @JsonProperty("techou-printer-setting")
    public String getTechouPrinterSetting() {
        return techouPrinterSetting;
    }

    public void setTechouPrinterSetting(String techouPrinterSetting) {
        this.techouPrinterSetting = techouPrinterSetting;
    }

    public static Config load() throws IOException {
        Path path = Paths.get(System.getProperty("user.home"), "myclinic-env", "pharma.yml");
        if( Files.exists(path) ){
            return objectMapper.readValue("presc-content-printer-setting: presc", Config.class);
        } else {
            return new Config();
        }
    }

    @Override
    public String toString() {
        return "Config{" +
                "prescContentPrinterSetting='" + prescContentPrinterSetting + '\'' +
                ", drugBagPrinterSetting='" + drugBagPrinterSetting + '\'' +
                ", techouPrinterSetting='" + techouPrinterSetting + '\'' +
                '}';
    }
}
