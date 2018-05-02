package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class Masters {

    private static Logger logger = LoggerFactory.getLogger(Masters.class);
    private String at;
    public ShinryouMasterDTO 処方せん料;
    public ShinryouMasterDTO 処方せん料７;
    public ShinryouMasterDTO 調基;
    public ShinryouMasterDTO 特定疾患管理料;
    public ShinryouMasterDTO 長期投薬加算;
    public ShinryouMasterDTO 特定疾患処方管理加算;

    public Masters(int year, int month) throws Exception {
        at = LocalDate.of(year, month, 1).toString();
        setup();
    }

    private void setup() throws Exception {
        処方せん料 = resolveShinryouMaster("処方せん料");
        処方せん料７ = resolveShinryouMaster("処方せん料７");
        調基 = resolveShinryouMaster("調基");
        特定疾患管理料 = resolveShinryouMaster("特定疾患管理");
        長期投薬加算 = resolveShinryouMaster("長期処方");
        特定疾患処方管理加算 = resolveShinryouMaster("特定疾患処方");
    }

    private ShinryouMasterDTO resolveShinryouMaster(String name) throws Exception {
        ShinryouMasterDTO master = Service.api.resolveShinryouMasterByNameCall(name, at).execute().body();
        assert master != null;
        logger.info("{} resolved to {}", name, master.name);
        return master;
    }

}
