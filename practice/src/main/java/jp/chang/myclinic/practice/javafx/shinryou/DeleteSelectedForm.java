package jp.chang.myclinic.practice.javafx.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DeleteSelectedForm extends HandleSelectedForm {

    private static Logger logger = LoggerFactory.getLogger(DeleteSelectedForm.class);

    public DeleteSelectedForm(List<ShinryouFullDTO> shinryouList) {
        super("複数診療削除", shinryouList);
    }

}
