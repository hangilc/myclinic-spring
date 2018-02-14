package jp.chang.myclinic.practice.javafx.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CopySelectedForm extends HandleSelectedForm {

    private static Logger logger = LoggerFactory.getLogger(CopySelectedForm.class);

    public CopySelectedForm(List<ShinryouFullDTO> shinryouList) {
        super("診療行為のコピー", shinryouList);
    }

}
