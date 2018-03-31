package jp.chang.myclinic.medicalcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

class Config {

    private static Logger logger = LoggerFactory.getLogger(Config.class);

    String doctorName = "";
    String clinicName = "";
    String clinicAddr = "";
    String clinicPhone = "";
    String clinicFax = "";

    void load() throws Exception {
        Path path = Paths.get("config", "application.yml");
        Yaml yaml = new Yaml();
        try (InputStream ins = new FileInputStream(path.toFile())) {
            Map map = yaml.load(ins);
            Map myclinicMap = (Map) map.get("myclinic");
            Map clinicMap = (Map) myclinicMap.get("clinic");
            this.doctorName = (String) clinicMap.get("doctor-name");
            this.clinicName = (String) clinicMap.get("name");
            String postalCode = (String) clinicMap.get("postal-code");
            String address = (String) clinicMap.get("address");
            this.clinicAddr = String.join(" ", postalCode, address);
            this.clinicPhone = (String) clinicMap.get("tel");
            this.clinicFax = (String) clinicMap.get("fax");
        }
    }
}
