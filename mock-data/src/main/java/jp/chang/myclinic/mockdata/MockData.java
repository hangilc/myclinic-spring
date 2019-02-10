package jp.chang.myclinic.mockdata;

import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.consts.TodoufukenCode;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.kanjidate.GengouNenPair;
import jp.chang.myclinic.util.kanjidate.KanjiDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public MockData() {
        this.lastNames = loadNames("/last-names.txt");
        this.maleFirstNames = loadNames("/male-first-names.txt");
        this.femaleFirstNames = loadNames("/female-first-names.txt");
    }

    public PatientDTO mockPatient() {
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

    private <T> T pickFromList(List<T> list) {
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

}