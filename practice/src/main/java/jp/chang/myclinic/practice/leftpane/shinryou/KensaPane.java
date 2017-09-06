package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
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

        static KensaEntry sep = new KensaEntry("-");

        KensaEntry(String name){
            this(name, name);
        }

        KensaEntry(String name, String value){
            this.name = name;
            this.value = name;
        }
    }

    private static KensaEntry[] leftEntries = new KensaEntry[]{
            new KensaEntry("血算"),
            new KensaEntry("末梢血液像"),
            new KensaEntry("ＨｂＡ１ｃ"),
            new KensaEntry("ＰＴ"),
            KensaEntry.sep,
            new KensaEntry("ＧＯＴ"),
            new KensaEntry("ＧＰＴ"),
            new KensaEntry("γＧＴＰ"),
            new KensaEntry("ＣＰＫ"),
            new KensaEntry("クレアチニン"),
            new KensaEntry("尿酸"),
            new KensaEntry("カリウム"),
            new KensaEntry("ＬＤＬ－Ｃｈ", "ＬＤＬ－コレステロール"),
            new KensaEntry("ＨＤＬ－Ｃｈ", "ＨＤＬ－コレステロール"),
            new KensaEntry("ＴＧ"),
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

    private static String[] presetNames = new String[]{"血算", "ＨｂＡ１ｃ", "ＧＯＴ", "ＧＰＴ", "γＧＴＰ",
            "クレアチニン", "尿酸", "ＬＤＬ－コレステロール", "ＨＤＬ－コレステロール", "ＴＧ"};


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
            }
        }
        return col;
    }

    private Component makeSubcommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        Link presetLink = new Link("セット検査");
        Link clearLink = new Link("クリア");
        panel.add(presetLink);
        panel.add(clearLink);
        return panel;
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        panel.add(enterButton);
        panel.add(cancelButton);
        return panel;
    }
}
