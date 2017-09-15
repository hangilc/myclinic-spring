package jp.chang.myclinic.practice.leftpane.conduct.addshinryou;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AddConductShinryouForm extends JPanel {

    public interface Callback {
        default void onCancel(){}
    }

    private Callback callback = new Callback(){};

    public AddConductShinryouForm(int width, String at){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        CommandBox commandBox = new CommandBox();
        commandBox.setCallback(new CommandBox.Callback() {
            @Override
            public void onEnter() {

            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
        add(commandBox, "growx");
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }
}
