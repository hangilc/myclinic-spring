package jp.chang.myclinic.rcpt.data;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.rcpt.RcptUtil;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;
import jp.chang.myclinic.util.NumberUtil;
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

    private void out(String fmt, Object... args){
        System.out.printf(fmt, args);
        System.out.print('\n');
    }

    void run() throws Exception {
        List<Integer> patientIds =
                Service.api.listVisitingPatientIdHavingHokenCall(year, month).execute().body();
        System.err.printf("Patients %d\n", patientIds.size());
        out("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out("<レセプト>");
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
        out("</レセプト>");
    }

    private void outProlog() throws Exception {
        LocalDate d = LocalDate.of(year, month, 1);
        JapaneseEra era = DateTimeUtil.getEra(d);
        ClinicInfoDTO info = Service.api.getClinicInfoCall().execute().body();
        out("  <元号>%s</元号>", Gengou.fromEra(era).getKanji());
        out("  <年>%d</年>", DateTimeUtil.getNen(d));
        out("  <月>%d</月>", month);
        out("  <都道府県番号>%s</都道府県番号>", info.todoufukencode);
        out("  <医療機関コード>%s.%s.%s</医療機関コード>",
                info.kikancode.substring(0, 2),
                info.kikancode.substring(2, 6),
                info.kikancode.substring(6, 7));
        out("  <医療機関住所>%s</医療機関住所>", info.address);
        String phone = info.tel;
        if (phone.contains("-")) {
            String[] phoneParts = info.tel.split("-");
            phone = String.format("%s (%s) %s", phoneParts[0], phoneParts[1], phoneParts[2]);
        }
        out("  <医療機関電話>%s</医療機関電話>", phone);
        out("  <医療機関名称>%s</医療機関名称>", info.name);
    }

    private void outPatient(PatientDTO patient, List<VisitFull2DTO> visits,
                            List<DiseaseFullDTO> diseases) {
        HokenDTO hoken = visits.get(0).hoken;
        String futan = getFutan(patient, hoken);
        out("<請求>");
        out("  <患者番号>%d</患者番号>", patient.patientId);
        out("  <保険種別>%s</保険種別>", getShubetsu(hoken));
        out("  <保険単独>%s</保険単独>", getTandoku(hoken));
        out("  <保険負担>%s</保険負担>", futan);
        out("  <給付割合>%d</給付割合>", getKyuufuWari(futan));
        outHokenDetail(hoken);
        out("  <氏名>%s%s</氏名>", patient.lastName, patient.firstName);
        out("  <性別>%s</性別>",
                patient.sex.equals("F") ? "女" : "男");
        out("  <生年月日>%s</生年月日>", patient.birthday);
        diseases.forEach(this::outDisease);
        visits.forEach(this::outVisit);
        out("</請求>");
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
            out("  <保険者番号>%d</保険者番号>", shaho.hokenshaBangou);
            out("  <被保険者記号>%s</被保険者記号>", shaho.hihokenshaKigou);
            out("  <被保険者番号>%s</被保険者番号>", shaho.hihokenshaBangou);
        } else if( hoken.koukikourei != null ){
            KoukikoureiDTO kouki = hoken.koukikourei;
            out("  <保険者番号>%s</保険者番号>", kouki.hokenshaBangou);
            out("  <被保険者記号>%s</被保険者記号>", "");
            out("  <被保険者番号>%s</被保険者番号>", kouki.hihokenshaBangou);
        }
        KouhiDTO kouhi1 = hoken.kouhi1;
        KouhiDTO kouhi2 = hoken.kouhi2;
        if( kouhi1 != null && kouhi2 != null && needKouhiSwap(kouhi1, kouhi2) ){
            KouhiDTO tmp = kouhi1;
            kouhi1 = kouhi2;
            kouhi2 = tmp;
        }
        if( kouhi1 != null ){
            out("  <公費1負担者番号>%d</公費1負担者番号>", kouhi1.futansha);
            out("  <公費1受給者番号>%d</公費1受給者番号>", kouhi1.jukyuusha);
        }
        if( kouhi2 != null ){
            out("  <公費2負担者番号>%d</公費2負担者番号>", kouhi2.futansha);
            out("  <公費2受給者番号>%d</公費2受給者番号>", kouhi2.jukyuusha);
        }
    }

    private void outDisease(DiseaseFullDTO disease){
        out("  <傷病名>");
        out("    <名称>%s</名称>", DiseaseUtil.getFullName(disease));
        out("    <診療開始日>%s</診療開始日>", disease.disease.startDate);
        if( disease.disease.endReason != DiseaseEndReason.NotEnded.getCode() ){
            out("    <転帰>%s</転帰>", getTenki(disease.disease.endReason));
            out("    <診療終了日>%s</診療終了日>", disease.disease.endDate);
        }
        out("  </傷病名>");
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
        out("  <受診>");
        out("    <受診日>%s</受診日>", visit.visit.visitedAt);
        visit.shinryouList.forEach(this::outShinryou);
        outDrugs(visit.drugs);
        out("  </受診>");
    }

    private void outShinryou(ShinryouFullDTO shinryou){
        out("    <診療>");
        out("      <診療コード>%d</診療コード>", shinryou.master.shinryoucode);
        out("      <名称>%s</名称>", shinryou.master.name);
        out("      <点数>%d</点数>", shinryou.master.tensuu);
        out("      <集計先>%s</集計先>", shinryou.master.shuukeisaki);
        if( !shinryou.master.houkatsukensa.equals("00") ){
            out("      <包括検査>%s</包括検査>", shinryou.master.houkatsukensa);
        }
        if( !shinryou.master.kensaGroup.equals("00") ){
            out("      <検査グループ>%s</検査グループ>", shinryou.master.kensaGroup);
        }
        out("    </診療>");
    }

    private void outDrugs(List<DrugFullDTO> drugs){
        if( drugs.size() > 0 ){
            out("    <投薬>");
            drugs.forEach(this::outOneDrug);
            out("    </投薬>");
        }
    }
    private void outOneDrug(DrugFullDTO drug){
        DrugCategory drugCategory = DrugCategory.fromCode(drug.drug.category);
        String category = "????";
        if( drugCategory == null ){
            System.err.println("Unknown drug category: " + drug.drug.category);
        } else {
            category = drugCategory.getKanji();
        }
        out("      <%s>", category);
        out("        <医薬品コード>%d</医薬品コード>", drug.master.iyakuhincode);
        out("        <名称>%s</名称>", drug.master.name);
        out("        <用量>%s</用量>", NumberUtil.formatNumber(drug.drug.amount));
        out("        <単位>%s</単位>", drug.master.unit);
        out("        <用法>%s</用法>", drug.drug.usage);
        out("        <薬価>%s</薬価>", String.format("%.2f", drug.master.yakka));
        out("        <麻毒>%c</麻毒>", drug.master.madoku);
        if( drugCategory == DrugCategory.Naifuku ){
            out("        <日数>%d</日数>", drug.drug.days);
        } else if( drugCategory == DrugCategory.Tonpuku ){
            out("        <回数>%d</回数>", drug.drug.days);
        }
        out("      </%s>", category);
    }

}
