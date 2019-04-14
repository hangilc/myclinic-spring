package jp.chang.myclinic.support.shinryoucodes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShinryoucodeFileResolver implements ShinryoucodeResolver {

    private Map<String, Integer> map;

    public ShinryoucodeFileResolver(File dataFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            this.map = mapper.readValue(dataFile,
                    new TypeReference<HashMap<String, Integer>>() {
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int resolveShinryoucodeByKey(String key) {
        return map.getOrDefault(key, 0);
    }
}
