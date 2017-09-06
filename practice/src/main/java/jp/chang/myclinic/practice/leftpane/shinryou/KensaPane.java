package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class KensaPane extends JPanel {

    interface Callback {
        default void onEnter(List<String> names){}
        default void onCancel(){}
    }

    private Callback callback = new Callback(){};

    private static class KensaEntry {
        String name;
        String value;
        boolean preset;

        static KensaEntry sep = new KensaEntry("-");

        KensaEntry(String name){
            this(name, name, false);
        }

        KensaEntry(String name, boolean preset){
            this(name, name, preset);
        }

        KensaEntry(String name, String value){
            this(name, value, false);
        }

        KensaEntry(String name, String value, boolean preset){
            this.name = name;
            this.value = value;
            this.preset = preset;
        }
    }

    private static KensaEntry[] leftEntries = new KensaEntry[]{
            new KensaEntry("血算", true),
            new KensaEntry("末梢血液像"),
            new KensaEntry("ＨｂＡ１ｃ", true),
            new KensaEntry("ＰＴ"),
            KensaEntry.sep,
            new KensaEntry("ＧＯＴ", true),
            new KensaEntry("ＧＰＴ", true),
            new KensaEntry("γＧＴＰ", true),
            new KensaEntry("ＣＰＫ"),
            new KensaEntry("クレアチニン", true),
            new KensaEntry("尿酸", true),
            new KensaEntry("カリウム"),
            new KensaEntry("ＬＤＬ－Ｃｈ", "ＬＤＬ－コレステロール", true),
            new KensaEntry("ＨＤＬ－Ｃｈ", "ＨＤＬ－コレステロール", true),
            new KensaEntry("ＴＧ", true),
            new KensaEntry("グルコース"),
    };

    private static KensaEntry[] rightEntries = new KensaEntry[]{
            new KensaEntry("ＣＲＰ"),
            KensaEntry.sep,
            new KensaEntry("ＴＳＨ"),
            new KensaEntry("ＦＴ４"),
            new KensaEntry("ＦＴ３"),
            new KensaEntry("ＰＳＡ"),
            KensaEntry.sep,
            new KensaEntry("蛋白定量（尿）"),
            new KensaEntry("クレアチニン（尿）"),
    };

    private static class KensaInput {
        String value;
        JCheckBox checkBox;
        boolean preset;

        KensaInput(String value, JCheckBox checkBox, boolean preset){
            this.value = value;
            this.checkBox = checkBox;
            this.preset = preset;
        }
    }

    private List<KensaInput> kensaInputs = new ArrayList<>();

    KensaPane(){
        setLayout(new MigLayout("insets 0, fill", "[grow] [grow]", ""));
        add(makeColumn(leftEntries), "top");
        add(makeColumn(rightEntries), "top, wrap");
        add(makeSubcommandBox(), "span, wrap");
        add(makeCommandBox(), "span");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private Component makeColumn(KensaEntry[] entries){
        JPanel col = new JPanel(new MigLayout("insets 0, gapy 0", "", ""));
        for(KensaEntry entry: entries){
            if( entry == KensaEntry.sep ){
                col.add(new JLabel("------"), "wrap");
            } else {
                JCheckBox check = new JCheckBox(entry.name);
                col.add(check, "wrap");
                kensaInputs.add(new KensaInput(entry.value, check, entry.preset));
            }
        }
        return col;
    }

    private Component makeSubcommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        Link presetLink = new Link("セット検査");
        presetLink.setCallback(event -> {
            kensaInputs.stream().filter(input -> input.preset).forEach(input -> input.checkBox.setSelected(true));
        });
        Link clearLink = new Link("クリア");
        clearLink.setCallback(event -> {
            kensaInputs.forEach(kensaInput -> kensaInput.checkBox.setSelected(false));
        });
        panel.add(presetLink);
        panel.add(clearLink);
        return panel;
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> doEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        panel.add(enterButton);
        panel.add(cancelButton);
        return panel;
    }

    private void doEnter() {
        List<String> selectedNames = new ArrayList<>();
        for(KensaInput kensaInput: kensaInputs){
            if( kensaInput.checkBox.isSelected() ){
                selectedNames.add(kensaInput.value);
            }
        }
        callback.onEnter(selectedNames);
    }
}
