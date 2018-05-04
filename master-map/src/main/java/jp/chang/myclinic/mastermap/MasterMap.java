package jp.chang.myclinic.mastermap;

import jp.chang.myclinic.mastermap.generated.ResolvedDiseaseAdjMap;
import jp.chang.myclinic.mastermap.generated.ResolvedDiseaseMap;
import jp.chang.myclinic.mastermap.generated.ResolvedKizaiMap;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
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

}
