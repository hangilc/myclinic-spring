package jp.chang.myclinic.support.kizaicodes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KizaicodeFileResolver implements KizaicodeResolver {

    private Map<String, Integer> map;

    public KizaicodeFileResolver(File dataFile) {
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
    public int resolveKizaicodeByKey(String key) {
        return map.getOrDefault(key, 0);
    }
}
