package jp.chang.myclinic.support.shinryounames;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ShinryouNamesFile implements ShinryouNamesService{

    private Map<String, List<String>> candidateMap;

    public ShinryouNamesFile(Path dataPath) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            this.candidateMap = mapper.readValue(dataPath.toFile(),
                    new TypeReference<Map<String, List<String>>>(){});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getCandidateNames(String key) {
        List<String> candidates = candidateMap.get(key);
        return candidates != null ? candidates : Collections.singletonList(key);
    }

    public static void main(String[] args) throws Exception {
        ShinryouNamesFile service = new ShinryouNamesFile(Paths.get("config/shinryou-names.yml"));
        System.out.println(service.candidateMap);
        System.out.println(service.getCandidateNames("単純撮影"));
    }
}
