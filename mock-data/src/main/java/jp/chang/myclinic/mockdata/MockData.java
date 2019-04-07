package jp.chang.myclinic.mockdata;

import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.consts.TodoufukenCode;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.dto_logic.HokenLib;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MockData {

    //private static Logger logger = LoggerFactory.getLogger(MockData.class);

    private Random random = new Random();
    private List<NameEntry> lastNames;
    private List<NameEntry> maleFirstNames;
    private List<NameEntry> femaleFirstNames;
    private List<Sex> sexList = Arrays.asList(Sex.values());
    private List<TodoufukenCode> todoufukenCodes = Arrays.asList(TodoufukenCode.values());
    private int serialPatientId = 1;
    private int serialVisitId = 1;
    private int serialShahokokuhoId = 1;
    private int serialKoukikoureiId = 1;
    private int serialKouhiId = 1;

    public MockData() {
        this.lastNames = loadNames("/last-names.txt");
        this.maleFirstNames = loadNames("/male-first-names.txt");
        this.femaleFirstNames = loadNames("/female-first-names.txt");
    }

    public PatientDTO pickPatient() {
        NameEntry lastName = pickLastName();
        Sex sex = pickSex();
        NameEntry firstName = pickFirstName(sex);
        LocalDate birthday = pickBirthday();
        PatientDTO dto = new PatientDTO();
        dto.lastName = lastName.kanji;
        dto.firstName = firstName.kanji;
        dto.lastNameYomi = lastName.yomi;
        dto.firstNameYomi = firstName.yomi;
        dto.birthday = birthday.toString();
        dto.sex = sex.getCode();
        dto.address = pickAddress();
        dto.phone = pickPhone();
        return dto;
    }

    public PatientDTO pickPatientWithPatientId(){
        PatientDTO patient = pickPatient();
        patient.patientId = serialPatientId++;
        return patient;
    }

    private List<NameEntry> loadNames(String name) {
        List<NameEntry> lines = new ArrayList<>();
        try {
            Path path = Paths.get("config", name);
            InputStream ins = new FileInputStream(path.toFile());
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins, StandardCharsets.UTF_8));
            reader.lines().forEach(line -> {
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length == 3) {
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

    public <T> T pickFromList(List<T> list) {
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    private Sex pickSex() {
        return pickFromList(sexList);
    }

    private NameEntry pickLastName() {
        int i = random.nextInt(lastNames.size());
        return lastNames.get(i);
    }

    private NameEntry pickFirstName(Sex sex) {
        List<NameEntry> entries;
        if (sex == Sex.Male) {
            entries = maleFirstNames;
        } else {
            entries = femaleFirstNames;
        }
        return pickFromList(entries);
    }

    private int pickBirthdayYear() {
        int age = random.nextInt(100);
        return LocalDate.now().getYear() - 1 - age;
    }

    private LocalDate pickBirthday() {
        int year = pickBirthdayYear();
        int month = 1 + random.nextInt(12);
        int lastDay = LocalDate.of(year, month, 1).plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS)
                .getDayOfMonth();
        int day = 1 + random.nextInt(lastDay);
        return LocalDate.of(year, month, day);
    }

    private String pickAddress() {
        int choume = 1 + random.nextInt(9);
        int ban = 1 + random.nextInt(18);
        int chi = 1 + random.nextInt(28);
        return String.format("杉並区参考地%d-%d-%d", choume, ban, chi);
    }

    private String pickPhone() {
        List<String> ds = List.of("03", "070", "080", "090");
        String d = ds.get(random.nextInt(ds.size()));
        return String.format("%s-%04d-%04d", d, random.nextInt(10000), random.nextInt(10000));
    }

    int pickInt(int low, int high){
        return low + random.nextInt(high + 1 - low);
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

    private int addCheckingDigit(int ival){
        return ival * 10 + HokenLib.calcCheckingDigit(ival);
    }

    int pickHokenshaBangou(int nDigits){
        return addCheckingDigit(pickDigits(nDigits-1));
    }

    private int pickShahokokuhoHokenshaBangou(){
        return pickHokenshaBangou(8);
    }

    private TodoufukenCode pickTodofukenCode(){
        int i = random.nextInt(todoufukenCodes.size());
        return todoufukenCodes.get(i);
    }

    private int pickKouhiFutanshaBangou(){
        int houbetsu = pickInt(10, 99);
        TodoufukenCode todoufukenCode = pickTodofukenCode();
        int hokensha = pickDigits(3);
        int bangou = houbetsu;
        bangou = bangou * 100 + todoufukenCode.getCode();
        bangou = bangou * 1000 + hokensha;
        return addCheckingDigit(bangou);
    }

    public ShahokokuhoDTO pickShahokokuho(int patientId){
        LocalDate validFrom = LocalDate.now().minus(random.nextInt(30*6), ChronoUnit.DAYS);
        String validUptoRep;
        int span = random.nextInt(3);
        if( span == 0 ){
            validUptoRep = "0000-00-00";
        } else {
            validUptoRep = validFrom.plus(1, ChronoUnit.YEARS).minus(1, ChronoUnit.DAYS).toString();
        }
        ShahokokuhoDTO dto = new ShahokokuhoDTO();
        dto.patientId = patientId;
        dto.hokenshaBangou = pickShahokokuhoHokenshaBangou();
        dto.hihokenshaKigou = pickDigits(2) + "-" + pickDigits(2);
        dto.hihokenshaBangou = "" + pickDigits(4);
        dto.honnin = random.nextInt(2);
        dto.validFrom = validFrom.toString();
        dto.validUpto = validUptoRep;
        dto.kourei = pickInt(0, 3);
        return dto;
    }

    public ShahokokuhoDTO pickShahokokuhoWithShahokokuhoId(int patientId){
        ShahokokuhoDTO result = pickShahokokuho(patientId);
        result.shahokokuhoId = serialShahokokuhoId++;
        return result;
    }

    public int pickKoukikoureiHokenshaBangou(){
        int housei = 39;
        return addCheckingDigit(housei * 100000 + pickDigits(5));
    }

    public KoukikoureiDTO pickKoukikourei(int patientId){
        LocalDate validFrom = LocalDate.now().minus(random.nextInt(30*6), ChronoUnit.DAYS);
        LocalDate validUpto = validFrom.plus(2, ChronoUnit.YEARS).minus(1, ChronoUnit.DAYS);
        int hokenshaBangou = pickKoukikoureiHokenshaBangou();
        int hihokenshaBangou = pickHokenshaBangou(8);
        KoukikoureiDTO dto = new KoukikoureiDTO();
        dto.patientId = patientId;
        dto.hokenshaBangou = "" + hokenshaBangou;
        dto.hihokenshaBangou = "" + hihokenshaBangou;
        dto.validFrom = validFrom.toString();
        dto.validUpto = validUpto.toString();
        dto.futanWari = pickInt(1, 3);
        return dto;
    }

    public KoukikoureiDTO pickKoukikoureiWithKoukikoureiId(int patientId){
        KoukikoureiDTO result = pickKoukikourei(patientId);
        result.koukikoureiId = serialKoukikoureiId++;
        return result;
    }

    public KouhiDTO pickKouhi(int patientId){
        LocalDate validFrom = LocalDate.now().minus(random.nextInt(30*6), ChronoUnit.DAYS);
        LocalDate validUpto = validFrom.plus(1, ChronoUnit.YEARS).minus(1, ChronoUnit.DAYS);
        KouhiDTO dto = new KouhiDTO();
        dto.patientId = patientId;
        dto.futansha = pickKouhiFutanshaBangou();
        dto.jukyuusha = pickHokenshaBangou(7);
        dto.validFrom = validFrom.toString();
        dto.validUpto = validUpto.toString();
        return dto;
    }

    public KouhiDTO pickKouhiWithKouhiId(int patientId){
        KouhiDTO result = pickKouhi(patientId);
        result.kouhiId = serialKouhiId++;
        return result;
    }

    public VisitDTO pickVisit(int patientId, LocalDateTime at){
        VisitDTO visit = new VisitDTO();
        visit.patientId = patientId;
        visit.visitedAt = DateTimeUtil.toSqlDateTime(at);
        return visit;
    }

    public VisitDTO pickVisitWithVisitId(int patientId, LocalDateTime at){
        VisitDTO visit = pickVisit(patientId, at);
        visit.visitId = serialVisitId++;
        return visit;
    }


}