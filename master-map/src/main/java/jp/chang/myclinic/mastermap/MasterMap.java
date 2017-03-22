package jp.chang.myclinic.mastermap;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Created by hangil on 2017/03/04.
 */
public class MasterMap {

    private CodeMap iyakuhinCodeMap;
    private CodeMap shinryouCodeMap;
    private CodeMap kizaiCodeMap;
    private NameMap shinryouNameMap;
    private NameMap kizaiNameMap;
    private NameMap diseaseNameMap;
    private NameMap diseaseAdjNameMap;

    MasterMap() {
        this.iyakuhinCodeMap = new CodeMap();
        this.shinryouCodeMap = new CodeMap();
        this.kizaiCodeMap = new CodeMap();
        shinryouNameMap = new NameMap();
        kizaiNameMap = new NameMap();
        diseaseNameMap = new NameMap();
        diseaseAdjNameMap = new NameMap();
    }

    public int resolveIyakuhinCode(int code, LocalDate at){
        return iyakuhinCodeMap.resolve(code, at);
    }

    public int resolveShinryouCode(int code, LocalDate at){
        return shinryouCodeMap.resolve(code,at);
    }

    public int resolveKizaiCode(int code, LocalDate at){
        return kizaiCodeMap.resolve(code, at);
    }

    public Optional<Integer> getShinryoucodeByName(String name){
        return shinryouNameMap.get(name);
    }

    public Optional<Integer> getKizaicodeByName(String name){
        return kizaiNameMap.get(name);
    }

    public Optional<Integer> getShoubyoumeicodeByName(String name){
        return diseaseNameMap.get(name);
    }

    public Optional<Integer> getShuushokugocodeByName(String name){
        return diseaseAdjNameMap.get(name);
    }

    public void loadCodeMap(Stream<String> lines) {
        lines.forEach(line -> {
            if (line.isEmpty()) return;
            char kind = line.charAt(0);
            CodeMap codeMap = null;
            if (kind == 'Y') {
                codeMap = iyakuhinCodeMap;
            } else if (kind == 'S') {
                codeMap = shinryouCodeMap;
            } else if (kind == 'K') {
                codeMap = kizaiCodeMap;
            }
            if (codeMap == null) {
                line = line.trim();
                if( line.isEmpty() ) return;
                if (line.charAt(0) == ';') {
                    return;
                }
                throw new RuntimeException("invalid code map entry: " + line);
            } else {
                CodeMapEntry entry = CodeMapEntry.parse(line);
                codeMap.addEntry(entry);
            }
        });
        iyakuhinCodeMap.sortByDate();
        shinryouCodeMap.sortByDate();
        kizaiCodeMap.sortByDate();
    }

    void loadNameMap(Stream<String> lines) {
        lines.forEach(line -> {
            if (line.isEmpty()) return;
            char kind = line.charAt(0);
            NameMap nameMap = null;
            if (kind == 's') {
                nameMap = shinryouNameMap;
            } else if (kind == 'k') {
                nameMap = kizaiNameMap;
            } else if (kind == 'd') {
                nameMap = diseaseNameMap;
            } else if( kind == 'a' ){
                nameMap = diseaseAdjNameMap;
            }
            if (nameMap == null) {
                line = line.trim();
                if( line.isEmpty() ) return;
                if (line.charAt(0) == ';') {
                    return;
                }
                throw new RuntimeException("invalid name map entry: " + line);
            } else {
                nameMap.parseAndEnter(line);
            }
        });
    }

    // void loadNameMapResource(Resource resource) throws IOException {
    //     try (InputStream in = resource.getInputStream()) {
    //         Scanner scanner = new Scanner(in, "UTF-8");
    //         while (scanner.hasNextLine()) {
    //             String line = scanner.nextLine();
    //             if (line.isEmpty()) continue;
    //             char kind = line.charAt(0);
    //             NameMap nameMap = null;
    //             if (kind == 's') {
    //                 nameMap = shinryouNameMap;
    //             } else if (kind == 'k') {
    //                 nameMap = kizaiNameMap;
    //             } else if (kind == 'd') {
    //                 nameMap = diseaseNameMap;
    //             } else if( kind == 'a' ){
    //                 nameMap = diseaseAdjNameMap;
    //             }
    //             if (nameMap == null) {
    //                 line = line.trim();
    //                 if( line.isEmpty() ) continue;
    //                 if (line.charAt(0) == ';') {
    //                     continue;
    //                 }
    //                 throw new RuntimeException("invalid name map entry: " + line);
    //             } else {
    //                 nameMap.parseAndEnter(line);
    //             }
    //         }
    //         scanner.close();
    //         iyakuhinCodeMap.sortByDate();
    //         shinryouCodeMap.sortByDate();
    //         kizaiCodeMap.sortByDate();
    //     }
    // }

}
