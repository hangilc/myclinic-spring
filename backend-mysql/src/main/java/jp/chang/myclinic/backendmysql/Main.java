package jp.chang.myclinic.backendmysql;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleFileProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaFile;
import jp.chang.myclinic.support.kizainames.KizaiNamesFile;
import jp.chang.myclinic.support.meisai.MeisaiServiceImpl;
import jp.chang.myclinic.support.shinryounames.ShinryouNamesFile;
import jp.chang.myclinic.support.stockdrug.StockDrugFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        DbBackend ctx = new DbBackend(MysqlDataSource.create(), MysqlTableSet::create,
                new StockDrugFile(Paths.get("config/stock-drug.txt")),
                new HoukatsuKensaFile(Paths.get("conifg/houkatsu-kensa.xml")),
                new MeisaiServiceImpl(),
                new DiseaseExampleFileProvider(Paths.get("config/disease-example.yml")),
                new ShinryouNamesFile(Paths.get("config/shinryou-names.yml")),
                new KizaiNamesFile(Paths.get("config/kizai-names.yml")));
        ctx.proc(backend -> {
            PatientDTO patient = backend.getPatient(198);
            System.out.println(patient);
        });
    }

}
