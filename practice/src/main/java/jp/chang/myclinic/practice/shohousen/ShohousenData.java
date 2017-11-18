package jp.chang.myclinic.practice.shohousen;

import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.HokenUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShohousenData {

    private String clinicAddress = "";
    private String clinicName = "";
    private String clinicPhone = "";
    private String kikancode = "";
    private String doctorName = "";
    private String hokenshaBangou = "";
    private String hihokensha = "";
    private String futansha = "";
    private String jukyuusha = "";
    private String futansha2 = "";
    private String jukyuusha2 = "";
    private String shimei = "";
    private LocalDate birthday;
    private Sex sex;
    private Boolean honnin = null;
    private Integer futanWari = null;
    private LocalDate koufuDate;
    private LocalDate validUptoDate;
    private List<String> drugLines;

    public void applyTo(ShohousenDrawer drawer){
        drawer.setHakkouKikan(clinicAddress, clinicName, clinicPhone, kikancode);
        drawer.setDoctorName(doctorName);
        drawer.setHokenshaBangou(hokenshaBangou);
        drawer.setHihokensha(hihokensha);
        drawer.setKouhi1Futansha(futansha);
        drawer.setKouhi1Jukyuusha(jukyuusha);
        drawer.setKouhi2Futansha(futansha2);
        drawer.setKouhi2Jukyuusha(jukyuusha2);
        drawer.setShimei(shimei);
        if( birthday != null ){
            drawer.setBirthday(birthday.getYear(), birthday.getMonthValue(), birthday.getDayOfMonth());
        }
        if( sex != null ) {
            switch (sex) {
                case Male:
                    drawer.setSexMale();
                    break;
                case Female:
                    drawer.setSexFemale();
                    break;
            }
        }
        if( honnin != null ){
            if( honnin ){
                drawer.setKubunHihokensha();
            } else {
                drawer.setKubunHifuyousha();
            }
        }
        if( futanWari != null ){
            if( futanWari != 10 ){
                drawer.setFutanWari(futanWari);
            }
        }
        if( koufuDate != null ){
            drawer.setKoufuDate(koufuDate.getYear(), koufuDate.getMonthValue(), koufuDate.getDayOfMonth());
        }
        if( validUptoDate != null ){
            if (koufuDate != null && validUptoDate.equals(koufuDate.plusDays(3))) {
                // it is the default value, so omit printing
            } else {
                drawer.setValidUptoDate(validUptoDate.getYear(), validUptoDate.getMonthValue(), validUptoDate.getDayOfMonth());
            }
        }
        if( drugLines != null ){
            drawer.setDrugLines(drugLines);
        }
    }

    public void setClinicInfo(ClinicInfoDTO clinicInfo){
        if( clinicInfo != null ){
            clinicAddress = clinicInfo.postalCode + " " + clinicInfo.address;
            clinicName = clinicInfo.name;
            clinicPhone = "電話 " + clinicInfo.tel;
            kikancode = clinicInfo.kikancode;
            doctorName = clinicInfo.doctorName;
        }
    }

    public void setHoken(HokenDTO hoken){
        if( hoken != null ){
            if( hoken.shahokokuho != null ){
                hokenshaBangou = hoken.shahokokuho.hokenshaBangou + "";
                hihokensha = composeHihokensha(hoken.shahokokuho);
                this.honnin = hoken.shahokokuho.honnin != 0;
            }
            List<KouhiDTO> kouhiList = new ArrayList<>();
            if( hoken.kouhi1 != null ){
                kouhiList.add(hoken.kouhi1);
                if( hoken.kouhi2 != null ){
                    kouhiList.add(hoken.kouhi2);
                    if( hoken.kouhi3 != null ){
                        kouhiList.add(hoken.kouhi3);
                    }
                }
            }
            if( kouhiList.size() > 0 ){
                KouhiDTO kouhi = kouhiList.get(0);
                futansha = kouhi.futansha + "";
                jukyuusha = kouhi.jukyuusha + "";
                if( kouhiList.size() > 1 ){
                    KouhiDTO kouhi2 = kouhiList.get(1);
                    futansha2 = kouhi2.futansha + "";
                    jukyuusha2 = kouhi2.jukyuusha + "";
                }
            }
        }
    }

    public void setPatient(PatientDTO patient){
        shimei = patient.lastName + patient.firstName;
        if( patient.birthday != null && !"0000-00-00".equals(patient.birthday) ){
            this.birthday = LocalDate.parse(patient.birthday);
        }
        sex = Sex.fromCode(patient.sex);
    }

    public void setFutanWari(HokenDTO hoken, PatientDTO patient, LocalDate visitedAt){
        if( patient.birthday != null && !"0000-00-00".equals(patient.birthday) ){
            LocalDate bd = LocalDate.parse(patient.birthday);
            int rcptAge = HokenUtil.calcRcptAge(bd.getYear(), bd.getMonthValue(), bd.getDayOfMonth(),
                    visitedAt.getYear(), visitedAt.getMonthValue());
            futanWari = HokenUtil.calcFutanWari(hoken, rcptAge);
        }
    }

    public void setKoufuDate(LocalDate koufuDate){
        this.koufuDate = koufuDate;
    }

    public void setValidUptoDate(LocalDate date){
        this.validUptoDate = date;
    }

    public void setDrugs(String content){
        if( content != null ){
            content = content.trim();
            List<String> lines = Arrays.stream(content.split("\\s*(?:\\r\\n|\\r|\\n)"))
                    .collect(Collectors.toList());
            if( lines.size() > 0 && lines.get(0).startsWith("院外処方") ){
                lines.remove(0);
            }
            if( lines.size() > 0 ) {
                lines.add("------以下余白------");
            }
            drugLines = lines;
        }
    }

    private String composeHihokensha(ShahokokuhoDTO shahokokuho){
        String kigou = shahokokuho.hihokenshaKigou;
        String bangou = shahokokuho.hihokenshaBangou;
        if( kigou == null ){
            if( bangou == null ){
                return "";
            } else {
                return bangou;
            }
        } else {
            if( bangou == null ){
                return kigou;
            } else {
                return kigou + " ・ " + bangou;
            }
        }
    }
}
