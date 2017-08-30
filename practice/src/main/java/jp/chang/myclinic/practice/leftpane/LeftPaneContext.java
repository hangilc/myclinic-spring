package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.DrugFullDTO;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface LeftPaneContext {

    void onDrugsEntered(int visitId, List<DrugFullDTO> drugs);

    static Optional<LeftPaneContext> get(Component comp){
        while( comp != null ){
            if( comp instanceof LeftPaneContext ){
                return Optional.of((LeftPaneContext)comp);
            }
            comp = comp.getParent();
        }
        System.out.println("LeftPaneContext not found");
        return Optional.empty();
    }
}
