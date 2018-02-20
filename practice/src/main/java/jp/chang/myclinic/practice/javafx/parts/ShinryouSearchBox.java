package jp.chang.myclinic.practice.javafx.parts;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShinryouSearchBox extends SearchBoxOld<ShinryouMasterDTO> {

    private static Logger logger = LoggerFactory.getLogger(ShinryouSearchBox.class);

    public ShinryouSearchBox(String at) {
        super(t -> Service.api.searchShinryouMaster(t, at), m -> m.name);
    }

}
