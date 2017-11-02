package jp.chang.myclinic.drawer.preview.manage;

import net.miginfocom.swing.MigLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;

class DevPart extends JPanel {

    DevPart(){
        setLayout(new MigLayout("insets 0", "", ""));
        add(new JLabel("a\nb\nc"));
    }

    void clear(){
        throw new NotImplementedException();
    }

    void setData(byte[] devnames, byte[] devmode){

    }
}
