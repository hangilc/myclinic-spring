package jp.chang.myclinic.support.stockdrug;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class StockDrugFile implements StockDrugService {

    private static class Entry {
        int fromCode;
        LocalDate startAt;
        int toCode;
        String comment;

        public Entry(int fromCode, LocalDate startAt, int toCode, String comment) {
            this.fromCode = fromCode;
            this.startAt = startAt;
            this.toCode = toCode;
            this.comment = comment;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "fromCode=" + fromCode +
                    ", startAt=" + startAt +
                    ", toCode=" + toCode +
                    ", comment='" + comment + '\'' +
                    '}';
        }
    }

    private Map<Integer, Entry> entryMap = new HashMap<>();

    public StockDrugFile(Path dataPath) {
        loadData(dataPath);
    }

    private void loadData(Path dataPath) {
        try (Stream<String> lines = Files.lines(dataPath)) {
            lines.forEach(line -> {
                String origLine = line;
                line = line.trim();
                if( line.isEmpty() || line.startsWith(";") ){
                    return;
                }
                String[] parts = line.split("[, ]", 4);
                if( parts.length < 3 ){
                    throw new RuntimeException("Invalid stock drug line: " + origLine);
                }
                String comment = parts.length == 4 ? parts[3] : "";
                Entry entry = new Entry(
                        Integer.parseInt(parts[0]),
                        LocalDate.parse(parts[1]),
                        Integer.parseInt(parts[2]),
                        comment
                );
                entryMap.put(entry.fromCode, entry);
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public int resolve(int iyakuhincode, LocalDate at) {
        int iter = 0;
        while( ++iter <= 20 ){
            Entry e = entryMap.get(iyakuhincode);
            if( e == null ){
                return iyakuhincode;
            }
            if( e.startAt.isBefore(at) || e.startAt.isEqual(at) ){
                iyakuhincode = e.toCode;
            }

        }
        throw new RuntimeException("Too many iteration StockDrugFile#resolve.");
    }

    public static void main(String[] args){
        StockDrugService srv = new StockDrugFile(Paths.get("config/stock-drug.txt"));
        System.out.println(srv.resolve(622142801, LocalDate.now()));
    }
}
