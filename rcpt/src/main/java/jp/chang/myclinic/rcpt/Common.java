package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.mastermap.ResolvedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

public class Common {

    private static Logger logger = LoggerFactory.getLogger(Common.class);

    private Common() { }

    public static ResolvedMap getResolvedMap(LocalDate at){
        try(InputStream ins = new FileInputStream("./config/application.yml")){
            Yaml yaml = new Yaml();
            Map<String,Object> map = yaml.load(ins);
            @SuppressWarnings("unchecked")
            Map<String,String> myclinic = (Map<String,String>)map.get("myclinic");
            String masterMapFile = myclinic.get("master-map-file");
            String nameMapFile = myclinic.get("name-map-file");
            MasterMap masterMap = MasterMap.loadMap(nameMapFile,masterMapFile);
            return masterMap.getResolvedMap(at);
        } catch(Exception ex){
            logger.error("Failed to load master map.", ex);
            System.exit(1);
            return null;
        }
    }
}
