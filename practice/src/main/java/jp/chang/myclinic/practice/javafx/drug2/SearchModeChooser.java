package jp.chang.myclinic.practice.javafx.drug2;

import jp.chang.myclinic.utilfx.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SearchModeChooser extends RadioButtonGroup<DrugSearchMode> {

    private static Logger logger = LoggerFactory.getLogger(SearchModeChooser.class);

    public SearchModeChooser(List<DrugSearchMode> searchModes) {
        searchModes.forEach(this::addModeButton);
    }

    public SearchModeChooser(){
        this(List.of(DrugSearchMode.Master, DrugSearchMode.Example, DrugSearchMode.Previous));
    }

    private void addModeButton(DrugSearchMode mode) {
        switch (mode) {
            case Master: {
                createRadioButton("マスター", DrugSearchMode.Master);
                break;
            }
            case Example: {
                createRadioButton("約束処方", DrugSearchMode.Example);
                break;
            }
            case Previous: {
                createRadioButton("過去の処方", DrugSearchMode.Previous);
                break;
            }
            default: {
                logger.warn("Unknown DrugSearchMode: " + mode);
                break;
            }
        }
    }

}
