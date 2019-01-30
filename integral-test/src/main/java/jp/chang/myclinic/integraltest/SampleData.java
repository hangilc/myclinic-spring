package jp.chang.myclinic.integraltest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SampleData {

    //private static Logger logger = LoggerFactory.getLogger(SampleData.class);
    private static class NameEntry {
        public String kanji;
        public String yomi;
        public int freq;

        private NameEntry(String kanji, String yomi, int freq){
            this.kanji = kanji;
            this.yomi = yomi;
            this.freq = freq;
        }

        @Override
        public String toString() {
            return "NameEntry{" +
                    "kanji='" + kanji + '\'' +
                    ", yomi='" + yomi + '\'' +
                    ", freq=" + freq +
                    '}';
        }
    }

    private List<NameEntry> lastNames;
    private List<NameEntry> maleFirstNames;
    private List<NameEntry> femaleFirstNames;
    private Random random = new Random();

    public SampleData(){
        this.lastNames = loadNames("/last-names.txt");
        this.maleFirstNames = loadNames("/male-first-names.txt");
        this.femaleFirstNames = loadNames("/female-first-names.txt");
    }

    private List<NameEntry> loadNames(String name){
        List<NameEntry> lines = new ArrayList<>();
        try {
            Path path = Paths.get("config", name);
            InputStream ins = new FileInputStream(path.toFile());
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins, StandardCharsets.UTF_8));
            reader.lines().forEach(line -> {
                String[] parts = line.split("\\s*\\|\\s*");
                if( parts.length == 3 ){
                    lines.add(new NameEntry(parts[0], parts[1], Integer.parseInt(parts[2])));
                }
            });
            reader.close();
            return lines;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public NameEntry pickLastName(){
        int i = random.nextInt(lastNames.size());
        return lastNames.get(i);
    }

    public String pickSex(){
        if( random.nextDouble() < 0.5 ){
            return "M";
        } else {
            return "F";
        }
    }

    public NameEntry pickFirstName(String sex){
        List<NameEntry> entries;
        if( sex.equals("M") ){
            entries = maleFirstNames;
        } else {
            entries = femaleFirstNames;
        }
        int i = random.nextInt(entries.size());
        return entries.get(i);
    }

}
