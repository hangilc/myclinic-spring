package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.support.diseaseexample.DiseaseExampleProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaService;
import jp.chang.myclinic.support.kizainames.KizaiNamesService;
import jp.chang.myclinic.support.meisai.MeisaiService;
import jp.chang.myclinic.support.shinryounames.ShinryouNamesService;
import jp.chang.myclinic.support.stockdrug.StockDrugService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Function;

public class DbBackend {

    public interface QueryStatement<T> {
        T query(Backend backend) throws SQLException;
    }

    public interface ProcStatement {
        void execute(Backend backend) throws SQLException;
    }

    private DB db;
    private Query query;
    private TableSet ts;
    private Backend backend;

    public DbBackend(DataSource ds, Function<Query, TableSet> tableSetCreator, StockDrugService stockDrugService,
                     HoukatsuKensaService houkatsuKensaService, MeisaiService meisaiService,
                     DiseaseExampleProvider diseaseExampleProvider,
                     ShinryouNamesService shinryouNamesService,
                     KizaiNamesService kizaiNamesService){
        this.db = new DB(ds);
        this.query = new Query(db.getConnectionProvider());
        this.ts = tableSetCreator.apply(query);
        this.backend = new Backend(ts, query, stockDrugService, houkatsuKensaService, meisaiService,
                diseaseExampleProvider, shinryouNamesService, kizaiNamesService);
    }

    public Query getQuery() {
        return query;
    }

    public TableSet getTableSet() {
        return ts;
    }

    public void proc(ProcStatement proc){
        db.query(() -> {
            proc.execute(backend);
            return null;
        });
    }

    public <T> T query(QueryStatement<T> proc){
        return db.query(() -> proc.query(backend));
    }

    public <T> T tx(QueryStatement<T> proc){
        return db.tx(() -> proc.query(backend));
    }

    public void txProc(ProcStatement proc){
        db.tx(() -> {
            proc.execute(backend);
            return null;
        });
    }

}
