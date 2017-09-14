package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;

import java.awt.*;
import java.util.List;

public interface LeftPaneContext {

    void onDrugsEntered(int visitId, List<DrugFullDTO> drugs);
    void onShinryouEntered(int visitId, List<ShinryouFullDTO> entered, Runnable uiCallback);
    void onConductEntered(int visitId, List<ConductFullDTO> entered, Runnable uiCallback);

    static LeftPaneContext get(Component comp){
        while( comp != null ){
            if( comp instanceof LeftPaneContext ){
                return (LeftPaneContext)comp;
            }
            comp = comp.getParent();
        }
        throw new RuntimeException("LeftPaneContext not found");
    }
}
