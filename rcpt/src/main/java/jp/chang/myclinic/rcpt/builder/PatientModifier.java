package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;

import java.time.LocalDate;

public class PatientModifier {

    private PatientDTO patient;

    PatientModifier(PatientDTO patient) {
        this.patient = patient;
    }

    public void setBirthDay(String birthday){
        LocalDate d = LocalDate.parse(birthday);
        patient.birthday = d.toString();
    }

    public void setSex(Sex sex){
        patient.sex = sex.getCode();
    }

}
