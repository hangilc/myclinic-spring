package jp.chang.myclinic.practice.leftpane.shinryou;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

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
        setLayout(new MigLayout("insets 0", "", ""));
        add(makeLeftCol(), "top, growx");
        add(makeRightCol(), "top, growx, wrap");
        add(makeCommandBox());
    }

    private JComponent makeLabel(String text){
        JEditorPane label = new JEditorPane("text/plain", text);
        label.setEditable(false);
        label.setBackground(getBackground());
        label.setBorder(BorderFactory.createEmptyBorder());
        label.setMinimumSize(new Dimension(10, 10));
        return label;
    }

    private JComponent makeCol(String[] items){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "", ""));
        for(String text: items){
            if( text.startsWith("-") ){
                panel.add(new JLabel(""), "span, h 10, wrap");
            } else {
                JCheckBox check = new JCheckBox(text);
                panel.add(check, "wrap");
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

    private JComponent makeCommandBox(){
        JPanel box = new JPanel(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        JButton cancelButton = new JButton("キャンセル");
        box.add(enterButton);
        box.add(cancelButton);
        return box;
    }

}
