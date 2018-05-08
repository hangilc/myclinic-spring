package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Common;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.List;

public class Check {

    private Check() {}

    public static void run(RunEnv runEnv) {
        int year = runEnv.year;
        int month = runEnv.month;
        Common.MasterMaps masterMaps = Common.getMasterMaps(LocalDate.of(year, month, 1));
        List<Integer> patientIds = runEnv.patientIds;
        try {
            if (patientIds == null) {
                patientIds = runEnv.api.listVisitingPatientIdHavingHokenCall(year, month).execute().body();
            }
            for (int patientId : patientIds) {
                PatientDTO patient = Service.api.getPatientCall(patientId).execute().body();
                if (runEnv.verbose) {
                    System.out.printf("%04d %s%s%n", patient.patientId, patient.lastName, patient.firstName);
                }
                List<VisitFull2DTO> visits = Service.api.listVisitByPatientHavingHokenCall(patientId, year, month)
                        .execute().body();
                assert visits.size() > 0;
                List<DiseaseFullDTO> diseases = Service.api.listDiseaseByPatientAtCall(patientId, year, month)
                        .execute().body();
                if (diseases == null) {
                    System.err.println("Failed to get disease list (some checks skipped). PatientID " + patientId);
                    continue;
                }
                Scope scope = new Scope(patient, visits, masterMaps.resolvedMap, masterMaps.shinryouByoumeiMap,
                        diseases, runEnv.errorHandler, runEnv.api);
                new CheckChouki(scope).check();
                // TODO: check duplicate shinryou in a visit
                new CheckTokuteiShikkanKanri(scope).check();
                new CheckChoukiTouyakuKasan(scope).check();
                new CheckHandanryou(scope).check();
                new CheckShoshinSaisin(scope).check();
                new CheckKouseishinyaku(scope).check();
                new CheckGaiyou(scope).check();
                new CheckShohouryou(scope).check();
                new CheckShoshinByoumei(scope).check();
                new CheckSaishinByoumei(scope).check();
                new CheckByoumei(scope).check();
            }
        } catch(IOException ex){
            throw new UncheckedIOException(ex);
        }
    }

}
