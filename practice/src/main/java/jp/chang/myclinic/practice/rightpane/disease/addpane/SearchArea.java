package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class SearchArea extends JPanel {

    private enum Mode { BYOUMEI, SHUUSHOKUGO };

    private JRadioButton byoumeiButton = new JRadioButton("病名");
    private JRadioButton shuushokuButton = new JRadioButton("修飾語");

    SearchArea(int width){
        setLayout(new MigLayout("insets 0, gapy 0", String.format("[%dpx!]", width), ""));
        add(makeSearchBox(), "growx, wrap");
        add(makeSearchOpt(), "wrap");
    }

    private Component makeSearchBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow] [] []", ""));
        JTextField searchTextField = new JTextField();
        JButton searchButton = new JButton("検索");
        Link exampleLink = new Link("例");
        panel.add(searchTextField, "growx");
        panel.add(searchButton);
        panel.add(exampleLink);
        return panel;
    }

    private Component makeSearchOpt(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        ButtonGroup group = new ButtonGroup();
        group.add(byoumeiButton);
        group.add(shuushokuButton);
        byoumeiButton.setSelected(true);
        panel.add(byoumeiButton);
        panel.add(shuushokuButton);
        return panel;
    }

    private Mode getMode(){
        if( byoumeiButton.isSelected() ){
            return Mode.BYOUMEI;
        } else if( shuushokuButton.isSelected() ){
            return Mode.SHUUSHOKUGO;
        } else {
            throw new RuntimeException("cannot happen");
        }
    }
}
