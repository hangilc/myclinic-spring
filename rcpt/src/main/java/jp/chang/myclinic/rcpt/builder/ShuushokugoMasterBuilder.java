package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.ShuushokugoMasterDTO;

public class ShuushokugoMasterBuilder {

    //private static Logger logger = LoggerFactory.getLogger(ShuushokugoMasterBuilder.class);
    private ShuushokugoMasterDTO result = new ShuushokugoMasterDTO();

    ShuushokugoMasterBuilder() {

    }

    public ShuushokugoMasterDTO build(){
        return result;
    }

}
