package jp.chang.myclinic.drawer.preview.manage;

import jp.chang.myclinic.drawer.printer.AuxSetting;
import net.miginfocom.swing.MigLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;

class AuxPart extends JPanel {

    AuxPart(){
        setLayout(new MigLayout("insets 0", "", ""));
    }

    void clear(){
        throw new NotImplementedException();
    }

    void setData(AuxSetting auxSetting){

    }
}
