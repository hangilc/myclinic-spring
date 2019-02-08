package jp.chang.myclinic.integraltest;

import jp.chang.myclinic.util.dto_logic.HokenLib;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.util.kanjidate.GengouNenPair;
import jp.chang.myclinic.util.kanjidate.KanjiDate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static jp.chang.myclinic.reception.grpc.generated.ReceptionMgmtOuterClass.*;

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

    public PatientInputs pickPatientInputs(){
        NameEntry lastName = pickLastName();
        String sex = pickSex();
        NameEntry firstName = pickFirstName(sex);
        LocalDate birthday = pickBirthday();
        GengouNenPair gn = KanjiDate.yearToGengou(birthday);
        return PatientInputs.newBuilder()
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

    int pickNonZeroDigit(){
        return 1 + random.nextInt(9);
    }

    int pickNonLeadingZeroDigits(int nDigits){
        int d = pickNonZeroDigit();
        while( --nDigits > 0 ){

        }
    }

    int pickDigits(int nDigit){
        if( nDigit <= 0 ){
            return 0;
        }
        int ival = random.nextInt(9) + 1;
        while( --nDigit > 0 ){
            int d = random.nextInt(10);
            ival = ival * 10 + d;
        }
        return ival;
    }

    int pickHokenshaBangou(int nDigits){
        int ival = pickDigits(nDigits - 1);
        return ival * 10 + HokenLib.calcCheckingDigit(ival);
    }

    private int pickShahokokuhoHokenshaBangou(){
        return pickHokenshaBangou(8);
    }

    private LocalDate pickLocalDate(int year){
        int month = 1 + random.nextInt(12);
        int lastDay = LocalDate.of(year, month, 1).plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS).getDayOfMonth();
        int day = 1 + random.nextInt(lastDay);
        return LocalDate.of(year, month, day);
    }

    private DateInputs toDateInputs(LocalDate src){
        GengouNenPair gn = KanjiDate.yearToGengou(src);
        return DateInputs.newBuilder()
                .setGengou(gn.gengou.getKanjiRep())
                .setNen("" + gn.nen)
                .setMonth("" + src.getMonthValue())
                .setDay("" + src.getDayOfMonth())
                .build();
    }

    private DateInputs emptyDateInputs(Gengou gengou){
        return DateInputs.newBuilder()
                .setGengou(gengou.getKanjiRep())
                .setNen("")
                .setMonth("")
                .setDay("")
                .build();
    }

    public ShahokokuhoInputs pickShahokokuhoInputs(){
        LocalDate validFrom = LocalDate.now().minus(random.nextInt(30*6), ChronoUnit.DAYS);
        int span = random.nextInt(3);
        DateInputs validUptoInputs;
        if( span == 0 ){
            validUptoInputs = emptyDateInputs(KanjiDate.yearToGengou(validFrom).gengou);
        } else {
            validUptoInputs = toDateInputs(validFrom.plus(1, ChronoUnit.YEARS));
        }
        return ShahokokuhoInputs.newBuilder()
                .setHokenshaBangou("" + pickShahokokuhoHokenshaBangou())
                .setHihokenshaKigou(pickDigits(2) + "-" + pickDigits(2))
                .setHihokenshaBangou("" + pickDigits(4))
                .setHonnin(random.nextInt(2))
                .setValidFromInputs(toDateInputs(validFrom))
                .setValidUptoInputs(validUptoInputs)
                .setKourei(1 + random.nextInt(3))
                .build();
    }

    public KouhiInputs pickKouhiInputs(){
        LocalDate validFrom = LocalDate.now().minus(random.nextInt(30*6), ChronoUnit.DAYS);
        LocalDate validUpto = validFrom.plus(1, ChronoUnit.YEARS);
        return KouhiInputs.newBuilder()
                .setFutanshaBangou("" + pickHokenshaBangou(8))
                .setJukyuushaBangou("" + pickHokenshaBangou(7))
                .setValidFromInputs(toDateInputs(validFrom))
                .setValidUptoInputs(toDateInputs(validUpto))
                .build();
    }

}
