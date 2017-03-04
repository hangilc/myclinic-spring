package jp.chang.myclinic.mastermap;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Created by hangil on 2017/03/04.
 */
public class MasterMap {

    private CodeMap iyakuhinCodeMap;
    private CodeMap shinryouCodeMap;
    private CodeMap kizaiCodeMap;

    MasterMap() {
        this.iyakuhinCodeMap = new CodeMap();
        this.shinryouCodeMap = new CodeMap();
        this.kizaiCodeMap = new CodeMap();
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

    void loadResource(Resource resource) throws IOException {
        try (InputStream in = resource.getInputStream()) {
            Scanner scanner = new Scanner(in);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) continue;
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
                    if (kind == ';') {
                        continue;
                    }
                    throw new RuntimeException("invalid master map entry: " + line);
                } else {
                    CodeMapEntry entry = CodeMapEntry.parse(line);
                    codeMap.addEntry(entry);
                }
            }
            scanner.close();
            iyakuhinCodeMap.sortByDate();
            shinryouCodeMap.sortByDate();
            kizaiCodeMap.sortByDate();
        }
    }

}
