package jp.chang.myclinic.rcpt.data;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.rcpt.RcptUtil;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.chrono.JapaneseEra;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Data {

    private static Logger logger = LoggerFactory.getLogger(Data.class);
    private int year;
    private int month;

    Data(int year, int month) {
        this.year = year;
        this.month = month;
    }

    void run() throws Exception {
        List<Integer> patientIds =
                Service.api.listVisitingPatientIdHavingHokenCall(year, month).execute().body();
        System.err.printf("Patients %d\n", patientIds.size());
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        System.out.println("<レセプト>");
        outProlog();
        for (int patientId : patientIds) {
            PatientDTO patient = Service.api.getPatientCall(patientId).execute().body();
            List<DiseaseFullDTO> diseases = Service.api.listDiseaseByPatientAtCall(patientId,
                    year, month).execute().body();
            List<VisitFull2DTO> visits = Service.api.listVisitByPatientHavingHokenCall(patientId,
                    year, month).execute().body();
            Map<HokenIds, List<VisitFull2DTO>> bundles = visits.stream()
                    .collect(Collectors.groupingBy(visit -> new HokenIds(visit.visit)));
            if (bundles.keySet().size() > 1) {
                System.err.printf("Multiple hoken for (%d) %s%s\n", patient.patientId,
                        patient.lastName, patient.firstName);
            }
            // TODO: handle bundles seprately
            outPatient(patient, visits, diseases);
        }
        System.out.println("</レセプト>");
    }

    private void outProlog() throws Exception {
        LocalDate d = LocalDate.of(year, month, 1);
        JapaneseEra era = DateTimeUtil.getEra(d);
        ClinicInfoDTO info = Service.api.getClinicInfoCall().execute().body();
        System.out.printf("  <元号>%s</元号>\n", Gengou.fromEra(era).getKanji());
        System.out.printf("  <年>%d</年>\n", DateTimeUtil.getNen(d));
        System.out.printf("  <月>%d</月>\n", month);
        System.out.printf("  <都道府県番号>%s</都道府県番号>\n", info.todoufukencode);
        System.out.printf("  <医療機関コード>%s.%s.%s</医療機関コード>\n",
                info.kikancode.substring(0, 2),
                info.kikancode.substring(2, 6),
                info.kikancode.substring(6, 7));
        System.out.printf("  <医療機関住所>%s</医療機関住所>\n", info.address);
        String phone = info.tel;
        if (phone.contains("-")) {
            String[] phoneParts = info.tel.split("-");
            phone = String.format("%s (%s) %s", phoneParts[0], phoneParts[1], phoneParts[2]);
        }
        System.out.printf("  <医療機関電話>%s</医療機関電話>\n", phone);
        System.out.printf("  <医療機関名称>%s</医療機関名称>\n", info.name);
    }

    private void outPatient(PatientDTO patient, List<VisitFull2DTO> visits,
                            List<DiseaseFullDTO> diseases) {
        HokenDTO hoken = visits.get(0).hoken;
        String futan = getFutan(patient, hoken);
        System.out.println("<請求>");
        System.out.printf("  <患者番号>%d</患者番号>\n", patient.patientId);
        System.out.printf("  <保険種別>%s</保険種別>\n", getShubetsu(hoken));
        System.out.printf("  <保険単独>%s</保険単独>\n", getTandoku(hoken));
        System.out.printf("  <保険負担>%s</保険負担>\n", futan);
        System.out.printf("  <給付割合>%d</給付割合>\n", getKyuufuWari(futan));
        outHokenDetail(hoken);
        System.out.printf("  <氏名>%s%s</氏名>\n", patient.lastName, patient.firstName);
        System.out.printf("  <性別>%s</性別>\n",
                patient.sex.equals("F") ? "女" : "男");
        System.out.printf("  <生年月日>%s</生年月日>\n", patient.birthday);
        diseases.forEach(this::outDisease);
        visits.forEach(this::outVisit);
        System.out.println("</請求>");
    }

    private String getShubetsu(HokenDTO hoken) {
        if (hoken.roujin != null) {
            return "老人";
        } else if (hoken.koukikourei != null) {
            return "後期高齢";
        } else if (hoken.shahokokuho != null) {
            int n = hoken.shahokokuho.hokenshaBangou / 1000000;
            if (n == 67 || (n >= 72 && n <= 75)) {
                return "退職";
            } else {
                return "社国";
            }
        } else if (hoken.kouhi1 != null || hoken.kouhi2 != null || hoken.kouhi3 != null) {
            return "公費";
        } else {
            return "????";
        }
    }

    private String getTandoku(HokenDTO hoken){
        if( hoken.shahokokuho != null || hoken.roujin != null || hoken.koukikourei != null ){
            if( hoken.kouhi2 != null ){
                return "３併";
            } else if( hoken.kouhi1 != null ){
                return "２併";
            } else {
                return "単独";
            }
        } else {
            if( hoken.kouhi2 != null ){
                return "２併";
            } else {
                return "単独";
            }
        }
    }

    private String getFutan(PatientDTO patient, HokenDTO hoken){
        if( patient.birthday != null && !patient.birthday.equals("0000-00-00") ){
            LocalDate birthday = LocalDate.parse(patient.birthday);
            int age = RcptUtil.calcRcptAge(birthday.getYear(), birthday.getMonthValue(),
                    birthday.getDayOfMonth(), year, month);
            if( age < 6 ){
                return "六才未満";
            }
        }

        if( hoken.roujin != null ){
            switch(hoken.roujin.futanWari){
                case 1: return "高齢９";
                case 2: return "高齢８";
                case 3: return "高齢７";
                default: return "??????";
            }
        } else if( hoken.koukikourei != null ){
            switch(hoken.koukikourei.futanWari){
                case 1: return "高齢９";
                case 2: return "高齢８";
                case 3: return "高齢７";
                default: return "??????";
            }
        } else if( hoken.shahokokuho != null ){
            switch(hoken.shahokokuho.kourei){
                case 1: return "高齢９";
                case 2: return "高齢８";
                case 3: return "高齢７";
                default: {
                    return hoken.shahokokuho.honnin != 0 ? "本人" : "家族";
                }
            }
        } else {
            return "本人";
        }
    }

    private int getKyuufuWari(String futan){
        switch(futan){
            case "六才未満": return 8;
            case "高齢９": return 9;
            case "高齢８": return 8;
            default: return 7;
        }
    }

    private boolean needKouhiSwap(KouhiDTO k1, KouhiDTO k2){
        int f1 = k1.futansha/1000000;
        int f2 = k2.futansha/1000000;
        return f1 == 88 && f2 == 82;
    }

    private void outHokenDetail(HokenDTO hoken){
        if( hoken.shahokokuho != null ){
            ShahokokuhoDTO shaho = hoken.shahokokuho;
            System.out.printf("  <保険者番号>%d</保険者番号>\n", shaho.hokenshaBangou);
            System.out.printf("  <被保険者記号>%s</被保険者記号>\n", shaho.hihokenshaKigou);
            System.out.printf("  <被保険者番号>%s</被保険者番号>\n", shaho.hihokenshaBangou);
        } else if( hoken.koukikourei != null ){
            KoukikoureiDTO kouki = hoken.koukikourei;
            System.out.printf("  <保険者番号>%s</保険者番号>\n", kouki.hokenshaBangou);
            System.out.printf("  <被保険者記号>%s</被保険者記号>\n", "");
            System.out.printf("  <被保険者番号>%s</被保険者番号>\n", kouki.hihokenshaBangou);
        }
        KouhiDTO kouhi1 = hoken.kouhi1;
        KouhiDTO kouhi2 = hoken.kouhi2;
        if( kouhi1 != null && kouhi2 != null && needKouhiSwap(kouhi1, kouhi2) ){
            KouhiDTO tmp = kouhi1;
            kouhi1 = kouhi2;
            kouhi2 = tmp;
        }
        if( kouhi1 != null ){
            System.out.printf("  <公費1負担者番号>%d</公費1負担者番号>\n", kouhi1.futansha);
            System.out.printf("  <公費1受給者番号>%d</公費1受給者番号>\n", kouhi1.jukyuusha);
        }
        if( kouhi2 != null ){
            System.out.printf("  <公費2負担者番号>%d</公費2負担者番号>\n", kouhi2.futansha);
            System.out.printf("  <公費2受給者番号>%d</公費2受給者番号>\n", kouhi2.jukyuusha);
        }
    }

    private void outDisease(DiseaseFullDTO disease){
        System.out.println("  <傷病名>");
        System.out.printf("    <名称>%s</名称>\n", DiseaseUtil.getFullName(disease));
        System.out.printf("    <診療開始日>%s</診療開始日>\n", disease.disease.startDate);
        if( disease.disease.endReason != DiseaseEndReason.NotEnded.getCode() ){
            System.out.printf("    <転帰>%s</転帰>\n", getTenki(disease.disease.endReason));
            System.out.printf("    <診療終了日>%s</診療終了日>\n", disease.disease.endDate);
        }
        System.out.println("  </傷病名>");
    }

    private String getTenki(char endReason){
        switch(endReason){
            case 'S': return "中止";
            case 'C': return "治ゆ";
            case 'N': return "継続";
            case 'D': return "死亡";
            default: return "????";
        }
    }

    private void outVisit(VisitFull2DTO visit){
        System.out.println("  <受診>");
        System.out.printf("    <受診日>%s</受診日>\n", visit.visit.visitedAt);
        visit.shinryouList.forEach(this::outShinryou);

        System.out.println("  </受診>");
    }

    private void outShinryou(ShinryouFullDTO shinryou){
        System.out.println("    <診療>");
        System.out.printf("      <診療コード>%d</診療コード>\n", shinryou.master.shinryoucode);
        System.out.printf("      <名称>%s</名称>\n", shinryou.master.name);
        System.out.printf("      <点数>%d</点数>\n", shinryou.master.tensuu);
        System.out.printf("      <集計先>%s</集計先>\n", shinryou.master.shuukeisaki);
        if( !shinryou.master.houkatsukensa.equals("00") ){
            System.out.printf("      <包括検査>%s</包括検査>\n", shinryou.master.houkatsukensa);
        }
        if( !shinryou.master.kensaGroup.equals("00") ){
            System.out.printf("      <検査グループ>%s</検査グループ>\n", shinryou.master.kensaGroup);
        }
        System.out.println("    </診療>");
    }

}
