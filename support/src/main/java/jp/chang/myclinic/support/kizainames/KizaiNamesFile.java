package jp.chang.myclinic.support.kizainames;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class KizaiNamesFile implements KizaiNamesService {

    private Map<String, List<String>> candidateMap;

    public KizaiNamesFile(Path dataPath) {
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

}
