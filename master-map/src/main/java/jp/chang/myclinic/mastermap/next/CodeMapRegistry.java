package jp.chang.myclinic.mastermap.next;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CodeMapRegistry {

    //private static Logger logger = LoggerFactory.getLogger(CodeMapRegistry.class);
    private Map<MapKind, CodeMap> registry = new HashMap<>();

    private CodeMapRegistry() {
        for(MapKind mapKind: MapKind.values()){
            registry.put(mapKind, new CodeMap());
        }
    }

    public static CodeMapRegistry load(Path path) throws IOException {
        CodeMapRegistry result = new CodeMapRegistry();
        try(Stream<String> lines = Files.lines(path)){
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
                CodeMap codeMap = result.registry.get(kind);
                CodeMapEntry entry = CodeMapEntry.parse(line);
                codeMap.addEntry(entry);
            });
            for (CodeMap codeMap : result.registry.values()) {
                codeMap.sortByDate();
            }
        }
        return result;
    }

    public CodeMap getCodeMap(MapKind mapKind){
        return registry.get(mapKind);
    }

}
