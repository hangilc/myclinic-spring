package jp.chang.myclinic.mastermap2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
                if (commentIndex >= 0) {
                    line = line.substring(0, commentIndex);
                }
                line = line.trim();
                if (line.isEmpty()) return;
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String first = parts[0].trim();
                    if (first.length() != 1) {
                        throw new RuntimeException("Invalid map kind key: " + origLine);
                    }
                    char leadChar = first.charAt(0);
                    MapKind kind = MapKind.fromNameKey(leadChar);
                    if (kind == null) {
                        throw new RuntimeException("Cannot find map kind: " + line);
                    }
                    String key = parts[1].trim();
                    if (key.isEmpty()) {
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

    private static Pattern patternCodeMapLine = Pattern.compile("^(.),(\\d{9}),(\\d{4}-\\d{2}-\\d{2}),(\\d{9})(\\s+(.*))?");

    public Map<MapKind, List<CodeMapEntry>> loadCodeMaps(String srcFile) {
        Map<MapKind, List<CodeMapEntry>> result = new EnumMap<>(MapKind.class);
        for (MapKind mk : MapKind.values()) {
            result.put(mk, new ArrayList<>());
        }
        try (Stream<String> lines = Files.lines(Paths.get(srcFile))) {
            lines.forEach(line -> {
                String origLine = line;
                line = line.trim();
                if( line.isEmpty() || line.startsWith(";") ){
                    return;
                }
                Matcher matcher = patternCodeMapLine.matcher(line);
                if( !matcher.matches() ){
                    throw new RuntimeException("Invalid master map entry: " + origLine);
                }
                char kindChar = matcher.group(1).charAt(0);
                String oldCode = matcher.group(2);
                String validFrom = matcher.group(3);
                String newCode = matcher.group(4);
                String comment = matcher.group(6);
                MapKind kind = MapKind.fromCodeKey(kindChar);
                if( kind == null ){
                    throw new RuntimeException("Invalid map kind: " + origLine);
                }
                int oldCodeValue = Integer.parseInt(oldCode);
                int newCodeValue = Integer.parseInt(newCode);
                LocalDate validFromValue = LocalDate.parse(validFrom);
                CodeMapEntry entry =  new CodeMapEntry(oldCodeValue, newCodeValue, validFromValue, comment);
                result.get(kind).add(entry);
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
        for(List<CodeMapEntry> entries: result.values()){
            entries.sort(CodeMapEntry::compareTo);
        }
        return result;
    }

}
