package jp.chang.myclinic.management;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.HokenUtil;
import jp.chang.myclinic.util.KouhiFutanshaError;
import jp.chang.myclinic.util.KoukikoureiError;
import jp.chang.myclinic.util.ShahokokuhoError;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CheckHokenshaBangou {

    public static void main(String[] args) throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
        Service.setServerUrl(System.getenv("MYCLINIC_SERVICE"));
        for(int i=0;i<3;i++){
            int year = firstDayOfMonth.getYear();
            int month = firstDayOfMonth.getMonthValue();
            List<Integer> patientIds = Service.api.listVisitingPatientIdHavingHokenCall(year, month).execute().body();
            for(int patientId: patientIds){
                List<VisitFull2DTO> visits = Service.api.listVisitByPatientHavingHokenCall(patientId, year, month).execute().body();
                for(VisitFull2DTO visit: visits){
                    ShahokokuhoDTO shahokokuho = visit.hoken.shahokokuho;
                    if( shahokokuho != null ){
                        verifyShahokokuho(shahokokuho.hokenshaBangou, patientId);
                    }
                    KoukikoureiDTO koukikourei = visit.hoken.koukikourei;
                    if( koukikourei != null ){
                        verifyKoukikourei(koukikourei.hokenshaBangou, patientId);
                    }
                    for(KouhiDTO kouhi: getKouhiList(visit.hoken)){
                        if( kouhi != null ){
                            verifyKouhiFutansha(kouhi.futansha, patientId);
                            verifyKouhiJukyuusha(kouhi.jukyuusha, patientId);
                        }
                    }
                }
            }
            firstDayOfMonth = firstDayOfMonth.minus(1, ChronoUnit.MONTHS);
        }
    }

    private static PatientDTO getPatient(int patientId) throws IOException {
        return Service.api.getPatientCall(patientId).execute().body();
    }

    private static void reportError(int patientId, String kind, String message) throws IOException {
        PatientDTO patient = getPatient(patientId);
        System.out.printf("(%4d) %s%s %s %s\n ",patient.patientId, patient.lastName, patient.firstName,
                kind, message);
    }

    private static void verifyShahokokuho(int bangou, int patientId) throws IOException {
        ShahokokuhoError err = HokenUtil.verifyShahokokuhoHokenshaBangou(bangou);
        if( err != null ){
            reportError(patientId, "社保国保", err.getMessage());
        }
    }

    private static void verifyKoukikourei(String bangouInput, int patientId) throws IOException {
        KoukikoureiError err = HokenUtil.verifyKoukikoureiHokenshaBangouInput(bangouInput);
        if( err != null ){
            reportError(patientId, "後期高齢", err.getMessage());
        }
    }

    private static void verifyKouhiFutansha(int bangou, int patientId) throws IOException {
        KouhiFutanshaError err = HokenUtil.verifyKouhiFutanshaBangou(bangou);
        if( err != null ){
            reportError(patientId, "公費負担者", err.getMessage());
        }
    }

    private static void verifyKouhiJukyuusha(int jukyuusha, int patientId) throws IOException {
        if( !(jukyuusha >= 1000000 && jukyuusha <= 9999999) ){
            PatientDTO patient = Service.api.getPatientCall(patientId).execute().body();
            System.out.printf("(%d) %s%s 公費受給者番号 %d ７桁でありません。\n", patient.patientId,
                    patient.lastName, patient.firstName, jukyuusha);
            return;
        }
        verify(jukyuusha, patientId, "公費受給者番号");
    }

    private static List<KouhiDTO> getKouhiList(HokenDTO hoken){
        List<KouhiDTO> result = new ArrayList<>();
        result.add(hoken.kouhi1);
        result.add(hoken.kouhi2);
        result.add(hoken.kouhi3);
        return result;
    }



    private static void verify(int bangou, int patientId, String kind) throws IOException {
//        HokenshaBangouAnalysisResult result = HokenUtil.analyzeHokenshaBangou(bangou);
//        if( result != HokenshaBangouAnalysisResult.OK ){
//            PatientDTO patient = Service.api.getPatientCall(patientId).execute().body();
//            System.out.printf("(%d) %s%s %s %d %s\n", patientId, patient.lastName, patient.firstName,
//                    kind, bangou, result.getMessage());
//        }
    }

}
