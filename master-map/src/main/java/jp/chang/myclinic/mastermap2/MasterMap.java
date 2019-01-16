package jp.chang.myclinic.mastermap2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MasterMap {

    public Map<MapKind, Map<String, Integer>> loadNameMaps(String srcFile) {
        Map<MapKind, Map<String, Integer>> result = new EnumMap<>(MapKind.class);
        for (MapKind mk : MapKind.values()) {
            result.put(mk, new HashMap<>());
        }
        try (Stream<String> lines = Files.lines(Paths.get(srcFile))) {
            lines.forEach(line -> {
                String origLine = line;
                int commentIndex = line.indexOf(';');
                if( commentIndex >= 0 ){
                    line = line.substring(0, commentIndex);
                }
                line = line.trim();
                if (line.isEmpty()) return;
                String[] parts = line.split(",");
                if( parts.length == 3 ){
                    String first = parts[0].trim();
                    if( first.length() != 1 ){
                        throw new RuntimeException("Invalid map kind key: " + origLine);
                    }
                    char leadChar = first.charAt(0);
                    MapKind kind = MapKind.fromNameKey(leadChar);
                    if (kind == null) {
                        throw new RuntimeException("Cannot find map kind: " + line);
                    }
                    String key = parts[1].trim();
                    if( key.isEmpty() ){
                        throw new RuntimeException("Invalid name: " + origLine);
                    }
                    Integer code = Integer.parseInt(parts[2].trim());
                    result.get(kind).put(key, code);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
        return result;
    }

}
