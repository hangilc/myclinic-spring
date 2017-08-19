package jp.chang.myclinic.practice.leftpane.shinryou;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class AddRegularPane extends JPanel {

    private static String[] leftItems = new String[]{
            "再診",
            "外来管理加算",
            "特定疾患管理",
            "-",
            "尿便検査判断料",
            "血液検査判断料",
            "生化Ⅰ判断料",
            "生化Ⅱ判断料",
            "免疫検査判断料",
            "微生物検査判断料",
            "静脈採血",
    };

    private static String[] rightItems = new String[]{
            "尿一般",
            "便潜血",
            "-",
            "処方料",
            "処方料７",
            "手帳記載加算",
            "外来後発加算１",
            "特定疾患処方",
            "長期処方",
            "内服調剤",
            "外用調剤",
            "調剤基本",
            "薬剤情報提供",

    };

    private static String[] bottomItems = new String[]{
            "向精神薬",
            "心電図",
            "骨塩定量",
    };

    AddRegularPane(){
        setLayout(new MigLayout("insets 0, debug", "", ""));
        add(makeLeftCol(), "top");
        add(makeRightCol(), "top");
    }

    private JEditorPane makeLabel(String text){
        JEditorPane label = new JEditorPane("text/plain", text);
        label.setEditable(false);
        label.setBackground(getBackground());
        label.setBorder(BorderFactory.createEmptyBorder());
        return label;
    }

    private JComponent makeCol(String[] items){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "", ""));
        for(String text: items){
            if( text.startsWith("-") ){
                panel.add(new JLabel(""), "span, h 10, wrap");
            } else {
                JCheckBox check = new JCheckBox("");
                check.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                panel.add(check);
                panel.add(makeLabel(text), "w 100!, wrap");
            }
        }
        return panel;
    }

    private JComponent makeLeftCol(){
        return makeCol(leftItems);
    }

    private JComponent makeRightCol(){
        return makeCol(rightItems);
    }

}
