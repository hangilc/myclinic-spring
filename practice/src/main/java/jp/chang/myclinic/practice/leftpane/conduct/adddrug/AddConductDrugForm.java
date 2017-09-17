package jp.chang.myclinic.practice.leftpane.conduct.adddrug;

import jp.chang.myclinic.dto.ConductDrugFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AddConductDrugForm extends JPanel {

    public interface Callback {
        default void onEntered(ConductDrugFullDTO entered){}
        default void onCancel(){}
    }

    private Callback callback = new Callback(){};

    public AddConductDrugForm(int width, String at, int conductId){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        CommandBox commandBox = new CommandBox();
        add(commandBox, "growx, wrap");
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }
}
