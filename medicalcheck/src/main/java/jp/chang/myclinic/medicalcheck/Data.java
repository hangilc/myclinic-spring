package jp.chang.myclinic.medicalcheck;

import java.util.ArrayList;
import java.util.List;

class Data {

    class ExamEntry {
        String key;
        String value;
    }

    String name = "";
    String birthday = "";
    String sex = "";
    String address = "";
    String height = "";
    String weight = "";
    String physicalExam = "";
    String visualAcuity = "";
    String hearingAbility1000Right = "";
    String hearingAbility4000Right = "";
    String hearingAbility1000Left = "";
    String hearingAbility4000Left = "";
    String bloodPressure = "";
    String ekg = "";
    String history = "";
    String chestXp = "";
    String urinaryProtein = "";
    String urinaryBlood = "";
    String urinarySugar = "";
    List<String> others = new ArrayList<>();
    List<ExamEntry> examEntries = new ArrayList<>();
    String clinicAddress1 = "";
    String clinicAddress2 = "";
    String clinicName = "";
    String doctorName = "";

}
