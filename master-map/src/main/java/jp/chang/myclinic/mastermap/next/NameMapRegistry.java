package jp.chang.myclinic.mastermap.next;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class NameMapRegistry {

    private Map<MapKind, NameMap> registry = new HashMap<>();

    NameMapRegistry() {
        for(MapKind mapKind: MapKind.values()){
            registry.put(mapKind, new NameMap());
        }
    }

    public NameMap getNameMap(MapKind mapKind){
        return registry.get(mapKind);
    }

    public static NameMapRegistry load(Path path) throws IOException {
        NameMapRegistry registry = new NameMapRegistry();
        try(Stream<String> lines = Files.lines(path)){
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
                    throw new RuntimeException("invalid name map entry: " + line);
                }
                NameMap nameMap = registry.getNameMap(kind);
                nameMap.parseAndEnter(line);
            });
        }
        return registry;
    }

}
