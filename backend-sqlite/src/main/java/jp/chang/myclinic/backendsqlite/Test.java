package jp.chang.myclinic.backendsqlite;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.Tester;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleFileProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaFile;
import jp.chang.myclinic.support.kizainames.KizaiNamesFile;
import jp.chang.myclinic.support.meisai.MeisaiServiceImpl;
import jp.chang.myclinic.support.shinryounames.ShinryouNamesFile;
import jp.chang.myclinic.support.stockdrug.StockDrugFile;

import javax.sql.DataSource;
import java.nio.file.Paths;

public class Test {

    public static void main(String[] args) {
        if( args.length != 1 ){
            System.err.println("Usage: Test dbFile");
            System.exit(1);
        }
        String dbFile = args[0];
        DataSource ds = SqliteDataSource.createTemporaryFromDbFile(dbFile);
        DbBackend dbBackend = new DbBackend(ds, SqliteTableSet::create,
                new StockDrugFile(Paths.get("config/stock-drug.txt")),
                new HoukatsuKensaFile(Paths.get("conifg/houkatsu-kensa.xml")),
                new MeisaiServiceImpl(),
                new DiseaseExampleFileProvider(Paths.get("config/disease-example.yml")),
                new ShinryouNamesFile(Paths.get("config/shinryou-names.yml")),
                new KizaiNamesFile(Paths.get("config/kizai-names.yml")));
        new Tester().test(dbBackend);
    }

}

