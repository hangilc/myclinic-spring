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

    public Masters(int year, int month) throws Exception {
        at = LocalDate.of(year, month, 1).toString();
        setup();
    }

    private void setup() throws Exception {
        処方せん料 = resolveShinryouMaster("処方せん料");
        処方せん料７ = resolveShinryouMaster("処方せん料７");
        調基 = resolveShinryouMaster("調基");
    }

    private ShinryouMasterDTO resolveShinryouMaster(String name) throws Exception {
        ShinryouMasterDTO master = Service.api.resolveShinryouMasterByNameCall(name, at).execute().body();
        assert master != null;
        logger.info("{} resolved to {}", name, master.name);
        return master;
    }

}
