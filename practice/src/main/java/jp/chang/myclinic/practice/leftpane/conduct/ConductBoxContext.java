package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.practice.leftpane.RecordContext;

import java.awt.*;

interface ConductBoxContext {

    void onDelete(int conductId);

    static ConductBoxContext get(Component comp){
        while( comp != null ){
            if( comp instanceof ConductBoxContext){
                return (ConductBoxContext)comp;
            }
            comp = comp.getParent();
        }
        throw new RuntimeException("ConductBoxContext not found");
    }
}
