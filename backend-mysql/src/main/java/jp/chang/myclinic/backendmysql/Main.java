package jp.chang.myclinic.backendmysql;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.SupportSet;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoFileProvider;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleFileProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaFile;
import jp.chang.myclinic.support.kizaicodes.KizaicodeFileResolver;
import jp.chang.myclinic.support.meisai.MeisaiServiceImpl;
import jp.chang.myclinic.support.shinryoucodes.ShinryoucodeFileResolver;
import jp.chang.myclinic.support.stockdrug.StockDrugFile;

import java.io.File;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        DbBackend ctx = new DbBackend(MysqlDataSource.create(), MysqlTableSet::create, createSupportSet());
        ctx.proc(backend -> {
            PatientDTO patient = backend.getPatient(198);
            System.out.println(patient);
        });
    }

    private static SupportSet createSupportSet(){
        SupportSet ss = new SupportSet();
        ss.stockDrugService = new StockDrugFile(Paths.get("config/stock-drug.txt"));
        ss.houkatsuKensaService = new HoukatsuKensaFile(Paths.get("conifg/houkatsu-kensa.xml"));
        ss.meisaiService = new MeisaiServiceImpl();
        ss.diseaseExampleProvider = new DiseaseExampleFileProvider(Paths.get("config/disease-example.yml"));
        ss.shinryoucodeResolver = new ShinryoucodeFileResolver(new File("config/shinryou-names.yml"));
        ss.kizaicodeResolver = new KizaicodeFileResolver(new File("config/kizai-names.yml"));
        ss.clinicInfoProvider = new ClinicInfoFileProvider(Paths.get("config/clinic-info.yml"));
        return ss;
    }

}
