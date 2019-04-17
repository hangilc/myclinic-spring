package jp.chang.myclinic.practice.javafx.parts;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.Context;

import java.time.LocalDate;

public class ShinryouSearchBox extends SearchBox<ShinryouMasterDTO> {

    public ShinryouSearchBox(LocalDate at) {
        super(t -> Context.frontend.searchShinryouMaster(t, at), m -> m.name);
    }

}
