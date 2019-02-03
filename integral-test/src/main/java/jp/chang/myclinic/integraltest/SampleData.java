package jp.chang.myclinic.integraltest;

import jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass;
import jp.chang.myclinic.util.kanjidate.GengouNenPair;
import jp.chang.myclinic.util.kanjidate.KanjiDate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SampleData {

    //private static Logger logger = LoggerFactory.getLogger(SampleData.class);
    private static class NameEntry {
        String kanji;
        String yomi;
        int freq;

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

    private NameEntry pickLastName(){
        int i = random.nextInt(lastNames.size());
        return lastNames.get(i);
    }

    private String pickSex(){
        if( random.nextDouble() < 0.5 ){
            return "M";
        } else {
            return "F";
        }
    }

    private NameEntry pickFirstName(String sex){
        List<NameEntry> entries;
        if( sex.equals("M") ){
            entries = maleFirstNames;
        } else {
            entries = femaleFirstNames;
        }
        int i = random.nextInt(entries.size());
        return entries.get(i);
    }

    private int pickBirthdayYear(){
        int age = random.nextInt(100);
        return LocalDate.now().getYear() - 1 - age;
    }

    private LocalDate pickBirthday(){
        int year = pickBirthdayYear();
        int month = 1 + random.nextInt(12);
        int lastDay = LocalDate.of(year, month, 1).plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS)
                .getDayOfMonth();
        int day = 1 + random.nextInt(lastDay);
        return LocalDate.of(year, month, day);
    }

    private String pickAddress(){
        int choume = 1 + random.nextInt(9);
        int ban = 1 + random.nextInt(18);
        int chi = 1 + random.nextInt(28);
        return String.format("杉並区参考地%d-%d-%d", choume, ban, chi);
    }

    private String pickPhone(){
        List<String> ds = List.of("03", "070", "080", "090");
        String d = ds.get(random.nextInt(ds.size()));
        return String.format("%s-%04d-%04d", d, random.nextInt(10000), random.nextInt(10000));
    }

    public ReceptionMgmtOuterClass.PatientInputs pickPatientInputs(){
        NameEntry lastName = pickLastName();
        String sex = pickSex();
        NameEntry firstName = pickFirstName(sex);
        LocalDate birthday = pickBirthday();
        GengouNenPair gn = KanjiDate.yearToGengou(birthday);
        return ReceptionMgmtOuterClass.PatientInputs.newBuilder()
                .setLastName(lastName.kanji)
                .setFirstName(firstName.kanji)
                .setLastNameYomi(lastName.yomi)
                .setFirstNameYomi(firstName.yomi)
                .setBirthdayGengou(gn.gengou.getKanjiRep())
                .setBirthdayNen(gn.nen + "")
                .setBirthdayMonth(birthday.getMonthValue() + "")
                .setBirthdayDay(birthday.getDayOfMonth() + "")
                .setSex(sex)
                .setAddress(pickAddress())
                .setPhone(pickPhone())
                .build();
    }

}
