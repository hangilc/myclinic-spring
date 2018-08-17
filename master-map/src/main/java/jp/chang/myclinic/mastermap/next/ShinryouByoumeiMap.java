package jp.chang.myclinic.mastermap.next;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ShinryouByoumeiMap {

    private Map<String, List<ByoumeiByName>> map = new HashMap<>();

    private ShinryouByoumeiMap() {

    }

    public static ShinryouByoumeiMap loadFromYaml(Path path) throws IOException {
        ShinryouByoumeiMap result = new ShinryouByoumeiMap();
        try (InputStream ins = new FileInputStream(path.toFile())) {
            Yaml yaml = new Yaml();
            Map<String, Object> top = yaml.load(ins);
            for (Map.Entry<String, Object> entry : top.entrySet()) {
                String shinryou = entry.getKey();
                @SuppressWarnings("unchecked")
                List<Object> values = (List<Object>) entry.getValue();
                @SuppressWarnings("unchecked")
                List<ByoumeiByName> byoumeiList = values.stream()
                        .map(value -> {
                            if( value instanceof String ){
                                return new ByoumeiByName((String)value);
                            } else {
                                return new ByoumeiByName((List<String>)value);
                            }
                        })
                        .collect(Collectors.toList());
                result.map.put(shinryou, byoumeiList);
            }
            return result;
        }
    }

    public Set<String> keySet(){
        return map.keySet();
    }

    public List<ByoumeiByName> get(String name){
        return Collections.unmodifiableList(map.get(name));
    }

}
