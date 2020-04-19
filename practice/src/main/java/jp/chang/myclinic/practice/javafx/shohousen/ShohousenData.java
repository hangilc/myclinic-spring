package jp.chang.myclinic.practice.javafx.shohousen;

import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.HokenUtil;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private String memo = "";

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
        if( memo != null && !memo.isEmpty() ){
            drawer.setMemo(memo);
        }
    }

    public void setClinicInfo(ClinicInfoDTO clinicInfo){
        if( clinicInfo != null ){
            clinicAddress = clinicInfo.postalCode + " " + clinicInfo.address;
            clinicName = clinicInfo.name;
            clinicPhone = "電話 " + clinicInfo.tel;
            kikancode = clinicInfo.todoufukencode + clinicInfo.tensuuhyoucode + clinicInfo.kikancode;
            doctorName = clinicInfo.doctorName;
        }
    }

    public void setHoken(HokenDTO hoken){
        if( hoken != null ){
            if( hoken.shahokokuho != null ){
                hokenshaBangou = hoken.shahokokuho.hokenshaBangou + "";
                hihokensha = composeHihokensha(hoken.shahokokuho);
                this.honnin = hoken.shahokokuho.honnin != 0;
            } else if( hoken.koukikourei != null ){
                hokenshaBangou = hoken.koukikourei.hokenshaBangou;
                hihokensha = hoken.koukikourei.hihokenshaBangou;
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

    private final Pattern patValidUptoDate = Pattern.compile("@有効期限\\s*[:：]\\s*(\\d{4}-\\d{2}-\\d{2})\\s*");
    private final Pattern pat0410 = Pattern.compile("@(0410対応|@０４１０対応)"); //新型コロナ感染対策

    public void setDrugs(String content){
        if( content != null ){
            content = content.trim();
            List<String> lines = Arrays.stream(content.split("\\s*(?:\\r\\n|\\r|\\n)"))
                    .collect(Collectors.toList());
            if( lines.size() > 0 && lines.get(0).startsWith("院外処方") ){
                lines.remove(0);
            }
            List<String> dLines = new ArrayList<>();
            for(String line: lines){
                if( line.startsWith("@") ){
                    Matcher m = patValidUptoDate.matcher(line);
                    if( m.matches() ){
                        String value = m.group(1);
                        LocalDate d = LocalDate.parse(value);
                        setValidUptoDate(d);
                        continue;
                    }
                    m = pat0410.matcher(line);
                    if( m.matches() ){
                        //noinspection StringConcatenationInLoop
                        memo = memo + "0410対応\n";
                        continue;
                    }
                    GuiUtil.alertError("Unknown command: " + line + "\n" +
                            "@有効期限：2020-04-19\n" +
                            "@0410対応");
                } else {
                    dLines.add(line);
                }
            }
            if( dLines.size() > 0 ) {
                dLines.add("------以下余白------");
            }
            this.drugLines = dLines;
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
