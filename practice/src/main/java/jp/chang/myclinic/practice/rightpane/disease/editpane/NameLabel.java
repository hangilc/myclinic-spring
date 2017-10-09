package jp.chang.myclinic.practice.rightpane.disease.editpane;

import jp.chang.myclinic.practice.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class NameLabel extends JPanel {
    private WrappedText wrappedText;

    NameLabel(){
        setLayout(new MigLayout("insets 0", "", ""));
    }

    void setText(String text){
        if( wrappedText == null ){
            wrappedText = new WrappedText(getWidth(), text);
            add(wrappedText);
        } else {
            wrappedText.setText(text);
        }
        revalidate();
        repaint();
    }
}
