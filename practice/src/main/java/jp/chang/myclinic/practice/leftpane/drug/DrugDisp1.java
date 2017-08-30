package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class DrugDisp1 extends JPanel {

    interface Callback {
        default void onClick(){}
    }

    private int drugId;
    private int index;
    private int width;
    private String drugRep;
    private Callback callback;

    DrugDisp1(DrugFullDTO drug, int index, int width){
        this.drugId = drug.drug.drugId;
        this.index = index;
        this.width = width;
        this.drugRep = DrugUtil.drugRep(drug);
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        add(makeDispPane(), "");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private JEditorPane makeDispPane(){
        String label = String.format("%d)%s", index, drugRep);
        JEditorPane dispPane = new JEditorPane();
        dispPane.setContentType("text/plain");
        dispPane.setSize(width, Integer.MAX_VALUE);
        dispPane.setText(label);
        dispPane.setEditable(false);
        dispPane.setBackground(getBackground());
        dispPane.setBorder(BorderFactory.createEmptyBorder());
        dispPane.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                callback.onClick();
            }
        });
        return dispPane;
    }

    int getDrugId() {
        return drugId;
    }

    int getIndex(){
        return index;
    }

    void update(DrugFullDTO drug){
        this.drugRep = DrugUtil.drugRep(drug);
        removeAll();
        add(makeDispPane(), "");
        repaint();
        revalidate();
    }

    void updateIndex(int index){
        this.index = index;
        removeAll();
        add(makeDispPane(), "");
        repaint();
        revalidate();
    }
}
