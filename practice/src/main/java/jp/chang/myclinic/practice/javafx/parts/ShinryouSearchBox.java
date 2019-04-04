package jp.chang.myclinic.practice.javafx.parts;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ShinryouSearchBox extends SearchBoxOld<ShinryouMasterDTO> {

    private static Logger logger = LoggerFactory.getLogger(ShinryouSearchBox.class);

    public ShinryouSearchBox(String at) {
        super(t -> Context.frontend.searchShinryouMaster(t, LocalDate.parse(at)), m -> m.name);
    }

}
