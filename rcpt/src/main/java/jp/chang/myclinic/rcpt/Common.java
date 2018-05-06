package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.mastermap.ResolvedMap;
import jp.chang.myclinic.mastermap.ResolvedShinryouByoumei;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Common {

    private static Logger logger = LoggerFactory.getLogger(Common.class);

    private Common() { }

    public static class MasterMaps {
        public MasterMap masterMap;
        public ResolvedMap resolvedMap;
        public Map<Integer, List<ResolvedShinryouByoumei>> shinryouByoumeiMap;
    }

    public static MasterMaps getMasterMaps(LocalDate at){
        try(InputStream ins = new FileInputStream("./config/application.yml")){
            MasterMaps maps = new MasterMaps();
            Yaml yaml = new Yaml();
            Map<String,Object> map = yaml.load(ins);
            @SuppressWarnings("unchecked")
            Map<String,String> myclinic = (Map<String,String>)map.get("myclinic");
            String masterMapFile = myclinic.get("master-map-file");
            String nameMapFile = myclinic.get("name-map-file");
            String shinryouByoumeiFile = myclinic.get("shinryou-byoumei-file");
            MasterMap masterMap = MasterMap.loadMap(nameMapFile,masterMapFile);
            maps.masterMap = masterMap;
            maps.resolvedMap = masterMap.getResolvedMap(at);
            maps.shinryouByoumeiMap = masterMap.getResolvedShinryouByoumeiMap(shinryouByoumeiFile, at);
            return maps;
        } catch(Exception ex){
            logger.error("Failed to load master map.", ex);
            throw new RuntimeException("Failed to load master map.");
        }
    }
}