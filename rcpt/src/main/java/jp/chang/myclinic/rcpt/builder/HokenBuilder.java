package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.HokenDTO;

public class HokenBuilder {

    //private static Logger logger = LoggerFactory.getLogger(HokenBuilder.class);
    private HokenDTO result;

    public HokenBuilder() {
        result = new HokenDTO();
    }

    public HokenDTO build(){
        return result;
    }

}
