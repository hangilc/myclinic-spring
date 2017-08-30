package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.DrugFullDTO;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface RecordContext {
    void onDrugsModified(List<DrugFullDTO> modifiedDrugs);

    static Optional<RecordContext> get(Component comp){
        while( comp != null ){
            if( comp instanceof RecordContext ){
                return Optional.of((RecordContext)comp);
            }
            comp = comp.getParent();
        }
        System.out.println("RecordContext not found");
        return Optional.empty();
    }
}
