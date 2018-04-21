package jp.chang.myclinic.pharma;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jp.chang.myclinic.pharma.javafx.lib.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

    private static Logger logger = LoggerFactory.getLogger(Config.class);

    private String prescContentPrinterSetting;
    private String drugBagPrinterSetting;
    private String techouPrinterSetting;
    private static Path configPath = Paths.get(System.getProperty("user.home"), "myclinic-env", "pharma.yml");
    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper(new YAMLFactory());
    }
    private static Pattern blankPattern = Pattern.compile("^\\s*$");


    private Config() {

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

    public static Optional<Config> load() {
        Path path = configPath;
        try {
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                Matcher matcher = blankPattern.matcher(content);
                if (matcher.matches()) {
                    return Optional.of(new Config());
                } else {
                    return Optional.of(objectMapper.readValue(content, Config.class));
                }
            } else {
                return Optional.of(new Config());
            }
        } catch(Exception ex){
            GuiUtil.alertException("設定ファイルの読み込みに失敗しました。", ex);
            return Optional.empty();
        }
    }

    public void save(){
        try {
            objectMapper.writeValue(configPath.toFile(), this);
        } catch(Exception ex){
            GuiUtil.alertException("設定ファイルの保存に失敗しました。", ex);
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
