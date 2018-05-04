package jp.chang.myclinic.mastermap;

import jp.chang.myclinic.mastermap.generated.ResolvedDiseaseAdjMap;
import jp.chang.myclinic.mastermap.generated.ResolvedDiseaseMap;
import jp.chang.myclinic.mastermap.generated.ResolvedKizaiMap;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class MasterMap {

    private Map<MapKind, CodeMap> codeMapRegistry = new EnumMap<>(MapKind.class);
    private Map<MapKind, NameMap> nameMapRegistry = new EnumMap<>(MapKind.class);

    public MasterMap() {
        for (MapKind kind : MapKind.values()) {
            codeMapRegistry.put(kind, new CodeMap());
            nameMapRegistry.put(kind, new NameMap());
        }
    }

    public int resolveIyakuhinCode(int code, LocalDate at) {
        return codeMapRegistry.get(MapKind.Iyakuhin).resolve(code, at);
    }

    public int resolveShinryouCode(int code, LocalDate at) {
        return codeMapRegistry.get(MapKind.Shinryou).resolve(code, at);
    }

    public int resolveKizaiCode(int code, LocalDate at) {
        return codeMapRegistry.get(MapKind.Kizai).resolve(code, at);
    }

    public Optional<Integer> getShinryoucodeByName(String name) {
        return nameMapRegistry.get(MapKind.Shinryou).get(name);
    }

    public Optional<Integer> getKizaicodeByName(String name) {
        return nameMapRegistry.get(MapKind.Kizai).get(name);
    }

    public Optional<Integer> getShoubyoumeicodeByName(String name) {
        return nameMapRegistry.get(MapKind.Disease).get(name);
    }

    public Optional<Integer> getShuushokugocodeByName(String name) {
        return nameMapRegistry.get(MapKind.DiseaseAdj).get(name);
    }

    void loadCodeMap(Stream<String> lines) {
        lines.forEach(line -> {
            if (line.isEmpty()) return;
            char leadChar = line.charAt(0);
            MapKind kind = MapKind.fromCodeKey(leadChar);
            if (kind == null) {
                line = line.trim();
                if (line.isEmpty()) return;
                if (line.charAt(0) == ';') {
                    return;
                }
                throw new RuntimeException("invalid code map entry: " + line);
            }
            CodeMap codeMap = codeMapRegistry.get(kind);
            CodeMapEntry entry = CodeMapEntry.parse(line);
            codeMap.addEntry(entry);
        });
        for (CodeMap codeMap : codeMapRegistry.values()) {
            codeMap.sortByDate();
        }
    }

    void loadNameMap(Stream<String> lines) {
        lines.forEach(line -> {
            if (line.isEmpty()) return;
            char leadChar = line.charAt(0);
            MapKind kind = MapKind.fromNameKey(leadChar);
            if (kind == null) {
                line = line.trim();
                if (line.isEmpty()) return;
                if (line.charAt(0) == ';') {
                    return;
                }
                throw new RuntimeException("invalid code map entry: " + line);
            }
            NameMap nameMap = nameMapRegistry.get(kind);
            nameMap.parseAndEnter(line);
        });
    }

    public static MasterMap loadMap(String nameMapLocation, String codeMapLocation) throws IOException {
        MasterMap masterMap = new MasterMap();
        {
            try (Stream<String> lines = Files.lines(Paths.get(nameMapLocation))) {
                masterMap.loadNameMap(lines);
            }
        }
        {
            try (Stream<String> lines = Files.lines(Paths.get(codeMapLocation))) {
                masterMap.loadCodeMap(lines);
            }
        }
        return masterMap;
    }

    private Resolver createResolver(MapKind kind) {
        return new ResolverImpl(
                nameMapRegistry.get(kind),
                codeMapRegistry.get(kind));
    }


    public ResolvedMap getResolvedMap(LocalDate at) {
        ResolvedMap m = new ResolvedMap();
        m.diseaseAdjMap = new ResolvedDiseaseAdjMap(createResolver(MapKind.DiseaseAdj), at);
        m.diseaseMap = new ResolvedDiseaseMap(createResolver(MapKind.Disease), at);
        m.shinryouMap = new ResolvedShinryouMap(createResolver(MapKind.Shinryou), at);
        m.kizaiMap = new ResolvedKizaiMap(createResolver(MapKind.Kizai), at);
        return m;
    }

    public static Map<String, List<ShinryouByoumei>> createShinryouByoumeiMap(String srcLocation) throws Exception {
        try(InputStream ins = new FileInputStream(srcLocation)){
            Map<String, List<ShinryouByoumei>> map = new HashMap<>();
            Yaml yaml = new Yaml();
            Map<String, Object> top = yaml.load(ins);
            for(Map.Entry<String, Object> entry: top.entrySet()){
                String byoumei = entry.getKey();
                List<ShinryouByoumei> sb = new ArrayList<>();
                @SuppressWarnings("unchecked")
                List<Object> values = (List<Object>)entry.getValue();
                for(Object value: values){
                    if( value instanceof String ){
                        sb.add(new ShinryouByoumei((String)value));
                    } else if( value instanceof List ){
                        List<String> elements = new ArrayList<>();
                        @SuppressWarnings("unchecked")
                        List<Object> valueList = (List<Object>)value;
                        for(Object valueElem: valueList){
                            elements.add((String)valueElem);
                        }
                        if( elements.size() > 0 ){
                            sb.add(new ShinryouByoumei(elements.get(0), elements.subList(1, elements.size())));
                        } else {
                            throw new RuntimeException("Failed to read shinryou byoumei: " + byoumei);
                        }
                    } else {
                        throw new RuntimeException("Failed to read shinryou byoumei: " + byoumei);
                    }
                    map.put(byoumei, sb);
                }
            }
            return map;
        }
    }

}
